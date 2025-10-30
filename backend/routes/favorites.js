// Backend Routes - Favorites
// routes/favorites.js

const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const { authenticateToken } = require('./auth');
require('dotenv').config();

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Get favorites
router.get('/', authenticateToken, async (req, res) => {
    try {
        const result = await pool.query(
            `SELECT s.* FROM songs s
             JOIN favorites f ON s.id = f.song_id
             WHERE f.user_id = $1
             ORDER BY f.added_at DESC`,
            [req.user.userId]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get favorites error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Add to favorites
router.post('/', authenticateToken, async (req, res) => {
    try {
        const { songId } = req.body;

        if (!songId) {
            return res.status(400).json({ error: 'Song ID is required' });
        }

        await pool.query(
            'INSERT INTO favorites (user_id, song_id) VALUES ($1, $2) ON CONFLICT DO NOTHING',
            [req.user.userId, songId]
        );

        res.status(201).json({ message: 'Added to favorites' });
    } catch (error) {
        console.error('Add to favorites error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Remove from favorites
router.delete('/:songId', authenticateToken, async (req, res) => {
    try {
        await pool.query(
            'DELETE FROM favorites WHERE user_id = $1 AND song_id = $2',
            [req.user.userId, req.params.songId]
        );

        res.status(204).send();
    } catch (error) {
        console.error('Remove from favorites error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
