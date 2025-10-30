// Backend Routes - Playlists
// routes/playlists.js

const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const { authenticateToken } = require('./auth');
require('dotenv').config();

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Get user playlists
router.get('/', authenticateToken, async (req, res) => {
    try {
        const userId = req.query.userId || req.user.userId;
        const result = await pool.query(
            `SELECT p.*, COUNT(ps.song_id) as song_count
             FROM playlists p
             LEFT JOIN playlist_songs ps ON p.id = ps.playlist_id
             WHERE p.user_id = $1
             GROUP BY p.id
             ORDER BY p.created_at DESC`,
            [userId]
        );
        res.json({ playlists: result.rows });
    } catch (error) {
        console.error('Get playlists error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get playlist by ID
router.get('/:id', async (req, res) => {
    try {
        const result = await pool.query(
            `SELECT p.*, COUNT(ps.song_id) as song_count
             FROM playlists p
             LEFT JOIN playlist_songs ps ON p.id = ps.playlist_id
             WHERE p.id = $1
             GROUP BY p.id`,
            [req.params.id]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Playlist not found' });
        }

        res.json({ playlist: result.rows[0] });
    } catch (error) {
        console.error('Get playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Create playlist
router.post('/', authenticateToken, async (req, res) => {
    try {
        const { name, description } = req.body;

        if (!name) {
            return res.status(400).json({ error: 'Playlist name is required' });
        }

        const result = await pool.query(
            'INSERT INTO playlists (user_id, name, description) VALUES ($1, $2, $3) RETURNING *',
            [req.user.userId, name, description]
        );

        res.status(201).json({ playlist: result.rows[0] });
    } catch (error) {
        console.error('Create playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Update playlist
router.put('/:id', authenticateToken, async (req, res) => {
    try {
        const { name, description } = req.body;

        // Verify ownership
        const playlistCheck = await pool.query(
            'SELECT user_id FROM playlists WHERE id = $1',
            [req.params.id]
        );

        if (playlistCheck.rows.length === 0) {
            return res.status(404).json({ error: 'Playlist not found' });
        }

        if (playlistCheck.rows[0].user_id !== req.user.userId) {
            return res.status(403).json({ error: 'Unauthorized' });
        }

        const result = await pool.query(
            'UPDATE playlists SET name = COALESCE($1, name), description = COALESCE($2, description) WHERE id = $3 RETURNING *',
            [name, description, req.params.id]
        );

        res.json({ playlist: result.rows[0] });
    } catch (error) {
        console.error('Update playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Delete playlist
router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        // Verify ownership
        const playlistCheck = await pool.query(
            'SELECT user_id FROM playlists WHERE id = $1',
            [req.params.id]
        );

        if (playlistCheck.rows.length === 0) {
            return res.status(404).json({ error: 'Playlist not found' });
        }

        if (playlistCheck.rows[0].user_id !== req.user.userId) {
            return res.status(403).json({ error: 'Unauthorized' });
        }

        await pool.query('DELETE FROM playlists WHERE id = $1', [req.params.id]);
        res.status(204).send();
    } catch (error) {
        console.error('Delete playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get playlist songs
router.get('/:id/songs', async (req, res) => {
    try {
        const result = await pool.query(
            `SELECT s.* FROM songs s
             JOIN playlist_songs ps ON s.id = ps.song_id
             WHERE ps.playlist_id = $1
             ORDER BY ps.position ASC`,
            [req.params.id]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get playlist songs error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Add song to playlist
router.post('/:id/songs', authenticateToken, async (req, res) => {
    try {
        const { songId, position } = req.body;

        if (!songId) {
            return res.status(400).json({ error: 'Song ID is required' });
        }

        // Verify ownership
        const playlistCheck = await pool.query(
            'SELECT user_id FROM playlists WHERE id = $1',
            [req.params.id]
        );

        if (playlistCheck.rows.length === 0) {
            return res.status(404).json({ error: 'Playlist not found' });
        }

        if (playlistCheck.rows[0].user_id !== req.user.userId) {
            return res.status(403).json({ error: 'Unauthorized' });
        }

        // Get max position if not provided
        let finalPosition = position;
        if (!finalPosition) {
            const positionResult = await pool.query(
                'SELECT COALESCE(MAX(position), 0) + 1 as next_position FROM playlist_songs WHERE playlist_id = $1',
                [req.params.id]
            );
            finalPosition = positionResult.rows[0].next_position;
        }

        await pool.query(
            'INSERT INTO playlist_songs (playlist_id, song_id, position) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING',
            [req.params.id, songId, finalPosition]
        );

        res.status(201).json({ message: 'Song added to playlist' });
    } catch (error) {
        console.error('Add song to playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Remove song from playlist
router.delete('/:id/songs/:songId', authenticateToken, async (req, res) => {
    try {
        // Verify ownership
        const playlistCheck = await pool.query(
            'SELECT user_id FROM playlists WHERE id = $1',
            [req.params.id]
        );

        if (playlistCheck.rows.length === 0) {
            return res.status(404).json({ error: 'Playlist not found' });
        }

        if (playlistCheck.rows[0].user_id !== req.user.userId) {
            return res.status(403).json({ error: 'Unauthorized' });
        }

        await pool.query(
            'DELETE FROM playlist_songs WHERE playlist_id = $1 AND song_id = $2',
            [req.params.id, req.params.songId]
        );

        res.status(204).send();
    } catch (error) {
        console.error('Remove song from playlist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
