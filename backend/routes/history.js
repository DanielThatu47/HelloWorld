// Backend Routes - Listening History
// routes/history.js

const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const { authenticateToken } = require('./auth');
require('dotenv').config();

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Record playback
router.post('/', authenticateToken, async (req, res) => {
    try {
        const { songId, playDuration, completed } = req.body;

        if (!songId) {
            return res.status(400).json({ error: 'Song ID is required' });
        }

        await pool.query(
            'INSERT INTO listening_history (user_id, song_id, play_duration, completed) VALUES ($1, $2, $3, $4)',
            [req.user.userId, songId, playDuration, completed || false]
        );

        // Update song play count
        await pool.query(
            'UPDATE songs SET play_count = play_count + 1 WHERE id = $1',
            [songId]
        );

        res.status(201).json({ message: 'Playback recorded' });
    } catch (error) {
        console.error('Record playback error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get recently played
router.get('/recent', authenticateToken, async (req, res) => {
    try {
        const limit = parseInt(req.query.limit) || 50;
        const result = await pool.query(
            `SELECT DISTINCT s.* FROM songs s
             JOIN listening_history lh ON s.id = lh.song_id
             WHERE lh.user_id = $1
             ORDER BY lh.played_at DESC
             LIMIT $2`,
            [req.user.userId, limit]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get recently played error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get most played
router.get('/most-played', authenticateToken, async (req, res) => {
    try {
        const limit = parseInt(req.query.limit) || 50;
        const result = await pool.query(
            `SELECT s.*, COUNT(lh.id) as play_count
             FROM songs s
             JOIN listening_history lh ON s.id = lh.song_id
             WHERE lh.user_id = $1
             GROUP BY s.id
             ORDER BY play_count DESC
             LIMIT $2`,
            [req.user.userId, limit]
        );
        res.json({ songs: result.rows });
    } catch (error) {
        console.error('Get most played error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
