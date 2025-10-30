// Backend Routes - Admin
// routes/admin.js

const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const { authenticateToken } = require('./auth');
require('dotenv').config();

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Middleware to check admin
async function requireAdmin(req, res, next) {
    try {
        const result = await pool.query(
            'SELECT is_admin FROM users WHERE id = $1',
            [req.user.userId]
        );

        if (result.rows.length === 0 || !result.rows[0].is_admin) {
            return res.status(403).json({ error: 'Admin access required' });
        }

        next();
    } catch (error) {
        console.error('Admin check error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
}

// Upload song
router.post('/songs/upload', authenticateToken, requireAdmin, async (req, res) => {
    try {
        const {
            title,
            artist,
            album,
            genre,
            year,
            fileUrl,
            coverImageUrl,
            duration,
            fileSize,
            bitrate,
            format
        } = req.body;

        if (!title || !artist || !fileUrl) {
            return res.status(400).json({ error: 'Title, artist, and file URL are required' });
        }

        const result = await pool.query(
            `INSERT INTO songs (
                title, artist, album, genre, year, file_url, cover_image_url,
                duration, file_size, bitrate, format, uploaded_by
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12) RETURNING *`,
            [title, artist, album, genre, year, fileUrl, coverImageUrl, duration, fileSize, bitrate, format, req.user.userId]
        );

        res.status(201).json({ song: result.rows[0] });
    } catch (error) {
        console.error('Upload song error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// Get analytics
router.get('/analytics', authenticateToken, requireAdmin, async (req, res) => {
    try {
        const songsResult = await pool.query('SELECT COUNT(*) as count FROM songs');
        const usersResult = await pool.query('SELECT COUNT(*) as count FROM users');
        const playsResult = await pool.query('SELECT COUNT(*) as count FROM listening_history');

        res.json({
            totalSongs: parseInt(songsResult.rows[0].count),
            totalUsers: parseInt(usersResult.rows[0].count),
            totalPlays: parseInt(playsResult.rows[0].count)
        });
    } catch (error) {
        console.error('Get analytics error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
