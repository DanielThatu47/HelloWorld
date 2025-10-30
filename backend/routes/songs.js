// Backend Routes - Songs
// routes/songs.js

const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const { authenticateToken } = require('./auth');
require('dotenv').config();

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Get all songs
router.get('/', async (req, res) => {
    try {
        const result = await pool.query(
            'SELECT * FROM songs ORDER BY upload_date DESC'
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get songs error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get song by ID
router.get('/:id', async (req, res) => {
    try {
        const result = await pool.query(
            'SELECT * FROM songs WHERE id = $1',
            [req.params.id]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Song not found' });
        }

        res.json({ song: result.rows[0] });
    } catch (error) {
        console.error('Get song error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Search songs
router.get('/search', async (req, res) => {
    try {
        const query = req.query.q || '';
        const result = await pool.query(
            `SELECT * FROM songs 
             WHERE title ILIKE $1 OR artist ILIKE $1 OR album ILIKE $1
             ORDER BY play_count DESC, title ASC`,
            [`%${query}%`]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Search songs error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get songs by genre
router.get('/genre/:genre', async (req, res) => {
    try {
        const result = await pool.query(
            'SELECT * FROM songs WHERE genre = $1 ORDER BY play_count DESC',
            [req.params.genre]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get songs by genre error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get songs by artist
router.get('/artist/:artist', async (req, res) => {
    try {
        const result = await pool.query(
            'SELECT * FROM songs WHERE artist = $1 ORDER BY year DESC, title ASC',
            [req.params.artist]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get songs by artist error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get recommendations
router.get('/recommendations', authenticateToken, async (req, res) => {
    try {
        // Get user's most played genres
        const genreResult = await pool.query(
            `SELECT s.genre, COUNT(*) as play_count
             FROM listening_history lh
             JOIN songs s ON lh.song_id = s.id
             WHERE lh.user_id = $1 AND s.genre IS NOT NULL
             GROUP BY s.genre
             ORDER BY play_count DESC
             LIMIT 3`,
            [req.user.userId]
        );

        const genres = genreResult.rows.map(r => r.genre);

        // Get recommended songs
        let result;
        if (genres.length > 0) {
            result = await pool.query(
                `SELECT DISTINCT s.*, 
                 CASE WHEN s.genre = ANY($1::text[]) THEN 1 ELSE 0 END as score
                 FROM songs s
                 WHERE s.genre = ANY($1::text[])
                 ORDER BY score DESC, play_count DESC
                 LIMIT 20`,
                [genres]
            );
        } else {
            // Fallback to popular songs
            result = await pool.query(
                'SELECT * FROM songs ORDER BY play_count DESC LIMIT 20'
            );
        }

        const recommendations = result.rows.map(song => ({
            song,
            reason: genres.includes(song.genre) ? `Based on your ${song.genre} preference` : 'Popular songs',
            score: genres.includes(song.genre) ? 0.8 : 0.5
        }));

        res.json({ recommendations });
    } catch (error) {
        console.error('Get recommendations error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
