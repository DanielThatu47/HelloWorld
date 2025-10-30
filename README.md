# 🎵 AI Music Player

A modern Android music player application with AI-generated synchronized lyrics, real-time animations, and intelligent music recommendations.

## ✨ Features

### Core Music Player Functionality
- **Full Playback Controls**: Play, pause, skip, rewind, repeat, and shuffle
- **Queue Management**: Up next songs displayed in a draggable bottom sheet (YouTube Music style)
- **Background Playback**: Continue playing music while using other apps
- **Lock Screen Controls**: Media controls on lock screen and notifications
- **Audio Focus Management**: Smart handling of audio interruptions
- **Equalizer**: Built-in equalizer with presets and custom settings
- **Sleep Timer**: Auto-stop music after specified time
- **Crossfade**: Smooth transitions between tracks

### AI-Powered Lyrics & Animations
- **Real-time Lyrics Sync**: AI-generated synchronized lyrics with word-level timing
- **Animated Transitions**: Smooth lyric animations similar to YouTube Music
- **Highlighting**: Currently playing line/word highlighted with animations
- **Lyric Scrolling**: Auto-scroll with manual override capability
- **Multiple Display Modes**: Full-screen lyrics, mini-player with lyrics, card view

### Personalization & Discovery
- **AI Recommendations**: Intelligent song suggestions based on listening history
- **Smart Playlists**: Auto-generated playlists (Recently Played, Most Played, Favorites)
- **Genre-based Discovery**: Explore music by genre, mood, or artist
- **Search**: Fast search across songs, artists, albums, and playlists

### Theming & Customization
- **Theme Modes**: Light, Dark, and AMOLED Black themes
- **Dynamic Colors**: Material You dynamic theming (Android 12+)
- **Custom Color Palettes**: Pre-designed color schemes
- **Font Customization**: Multiple font families and sizes
- **Album Art Themes**: Extract colors from album art for adaptive UI

### Admin Panel
- **Song Upload**: Admin dashboard for uploading music files
- **Metadata Management**: Edit song information, album art, and genres
- **User Management**: View and manage user accounts
- **Analytics Dashboard**: Track app usage and popular songs
- **Bulk Operations**: Upload and manage multiple songs at once

### Security & Authentication
- **Email/Password Authentication**: Secure login system
- **Google Sign-In**: One-tap authentication
- **Password Recovery**: Forgot password with email verification
- **JWT Token Authentication**: Secure API communication
- **Rate Limiting**: Protection against brute force attacks
- **Input Validation**: SQL injection and XSS prevention
- **Encrypted Storage**: Secure local data storage

## 🎨 Design System

### Color Palettes

**Spotify-inspired Dark Theme**
- Primary: `#1DB954` (Spotify Green)
- Background: `#121212`
- Surface: `#181818`
- Surface Variant: `#282828`
- Text Primary: `#FFFFFF`
- Text Secondary: `#B3B3B3`

**YouTube Music-inspired Theme**
- Primary: `#FF0000` (YouTube Red)
- Background: `#0F0F0F`
- Surface: `#212121`
- Surface Variant: `#303030`
- Accent: `#3EA6FF`
- Text Primary: `#FFFFFF`
- Text Secondary: `#AAAAAA`

**Modern Gradient Theme**
- Primary Gradient: `#667eea → #764ba2`
- Background: `#1a1a2e`
- Surface: `#16213e`
- Accent: `#0f3460`
- Text Primary: `#FFFFFF`
- Text Secondary: `#E0E0E0`

**Minimalist Light Theme**
- Primary: `#6200EE`
- Background: `#FAFAFA`
- Surface: `#FFFFFF`
- Surface Variant: `#F5F5F5`
- Text Primary: `#000000`
- Text Secondary: `#666666`

### UI Components
- **Material Design 3**: Modern Material You components
- **Custom Animations**: Lottie animations for enhanced UX
- **Glassmorphism**: Frosted glass effect for overlays
- **Smooth Transitions**: Shared element transitions between screens
- **Responsive Layout**: Adaptive UI for tablets and foldables

## 🛠️ Tech Stack

### Frontend (Android)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt/Dagger
- **Async Operations**: Coroutines + Flow
- **Navigation**: Compose Navigation
- **Media Playback**: ExoPlayer/Media3
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Local Database**: Room
- **Animations**: Lottie, Compose Animations
- **State Management**: StateFlow, ViewModel

### Backend
- **Framework**: Node.js + Express.js / FastAPI (Python)
- **Authentication**: JWT + OAuth 2.0
- **API**: RESTful API + GraphQL (optional)
- **Real-time**: Socket.io for live features

### Database
- **Primary Database**: PostgreSQL
  - Scalable and reliable
  - ACID compliant
  - Advanced indexing for fast queries
  - Support for JSON data types
  
- **Caching**: Redis
  - Session management
  - API response caching
  - Real-time analytics

- **Search Engine**: Elasticsearch
  - Fast full-text search
  - Fuzzy matching for song/artist search
  - Aggregations for recommendations

### Storage Solutions (10GB+ Free Tier)

**Recommended Primary Storage:**

1. **Supabase Storage** (Free: 1GB, Upgrade: Affordable)
   - Built-in CDN
   - Direct file uploads from client
   - Image transformations
   - Access control with RLS
   - Best Integration: Pairs well with Supabase Auth & Database

2. **Cloudflare R2** (Free: 10GB)
   - Zero egress fees
   - S3-compatible API
   - Global CDN
   - Excellent performance
   - **Recommended Choice**

**Alternative Options:**

3. **Backblaze B2** (Free: 10GB)
   - Very affordable beyond free tier
   - S3-compatible
   - Cloudflare CDN integration available

4. **Tebi.io** (Free: 25GB)
   - S3-compatible
   - No egress fees
   - Good for testing

5. **Firebase Storage** (Free: 5GB, 1GB/day downloads)
   - Easy integration with Firebase ecosystem
   - Good security rules
   - Real-time capabilities

**Hybrid Approach (Recommended):**
- **Cloudflare R2**: Music files (large storage needed)
- **Supabase Storage**: Album art & thumbnails (with image transformations)
- **CDN**: CloudFlare for global distribution

### AI/ML Services
- **Lyrics Generation**: OpenAI API / Anthropic Claude
- **Lyrics Sync**: Custom ML model or Lyrics.ovh API
- **Recommendations**: TensorFlow Lite for on-device ML
- **Audio Analysis**: Essentia.js for audio features

### DevOps & Infrastructure
- **Hosting**: Railway / Render / DigitalOcean
- **CI/CD**: GitHub Actions
- **Monitoring**: Sentry, Firebase Crashlytics
- **Analytics**: Firebase Analytics, Mixpanel
- **Version Control**: Git + GitHub

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    profile_image_url TEXT,
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    theme_preference VARCHAR(50) DEFAULT 'dark',
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE INDEX idx_users_email ON users(email);
```

### Songs Table
```sql
CREATE TABLE songs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    album VARCHAR(255),
    duration INTEGER NOT NULL,
    file_url TEXT NOT NULL,
    cover_image_url TEXT,
    genre VARCHAR(100),
    year INTEGER,
    lyrics TEXT,
    lyrics_sync JSONB,
    play_count INTEGER DEFAULT 0,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by UUID REFERENCES users(id),
    file_size BIGINT,
    bitrate INTEGER,
    format VARCHAR(10)
);

CREATE INDEX idx_songs_title ON songs(title);
CREATE INDEX idx_songs_artist ON songs(artist);
CREATE INDEX idx_songs_genre ON songs(genre);
CREATE INDEX idx_songs_play_count ON songs(play_count DESC);
```

### Playlists Table
```sql
CREATE TABLE playlists (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cover_image_url TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_playlists_user_id ON playlists(user_id);
```

### Playlist_Songs Table
```sql
CREATE TABLE playlist_songs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    playlist_id UUID REFERENCES playlists(id) ON DELETE CASCADE,
    song_id UUID REFERENCES songs(id) ON DELETE CASCADE,
    position INTEGER NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(playlist_id, song_id)
);

CREATE INDEX idx_playlist_songs_playlist_id ON playlist_songs(playlist_id);
```

### Listening_History Table
```sql
CREATE TABLE listening_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    song_id UUID REFERENCES songs(id) ON DELETE CASCADE,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    play_duration INTEGER,
    completed BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_listening_history_user_id ON listening_history(user_id);
CREATE INDEX idx_listening_history_played_at ON listening_history(played_at DESC);
```

### Favorites Table
```sql
CREATE TABLE favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    song_id UUID REFERENCES songs(id) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, song_id)
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 24+ (Target: API 34)
- Gradle 8.0+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/ai-music-player.git
cd ai-music-player
```

2. **Configure API keys**
Create `local.properties` file:
```properties
SUPABASE_URL=your_supabase_url
SUPABASE_ANON_KEY=your_supabase_key
OPENAI_API_KEY=your_openai_key
CLOUDFLARE_ACCOUNT_ID=your_cf_account_id
CLOUDFLARE_R2_ACCESS_KEY=your_r2_key
```

3. **Build the project**
```bash
./gradlew build
```

4. **Run the app**
```bash
./gradlew installDebug
```

## 📱 App Architecture

```
app/
├── data/
│   ├── local/          # Room database, DataStore
│   ├── remote/         # API services
│   └── repository/     # Repository implementations
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic
├── presentation/
│   ├── auth/           # Login, signup screens
│   ├── home/           # Main music browser
│   ├── player/         # Now playing screen
│   ├── lyrics/         # Lyrics display
│   ├── playlist/       # Playlist management
│   ├── admin/          # Admin dashboard
│   └── settings/       # App settings
├── di/                 # Dependency injection
└── util/               # Utilities & extensions
```

## 🎯 Key Features Implementation

### AI Lyrics Synchronization
```kotlin
// Lyrics sync data structure
data class LyricLine(
    val startTime: Long,        // Milliseconds
    val endTime: Long,
    val text: String,
    val words: List<WordTiming>
)

data class WordTiming(
    val word: String,
    val startTime: Long,
    val endTime: Long
)
```

### Recommendation Algorithm
- Collaborative Filtering: User behavior patterns
- Content-Based: Audio features, genre, tempo
- Hybrid Approach: Combine both methods
- On-device ML: TensorFlow Lite model

### Queue Management
- Drag-to-reorder functionality
- Add to up-next
- Play next vs add to queue
- Smart shuffle with history tracking

## 🔒 Security Features

- **Encrypted Communication**: TLS 1.3
- **Secure Storage**: Android Keystore for sensitive data
- **Password Policy**: Minimum 8 characters, complexity requirements
- **Session Management**: Auto-logout after inactivity
- **Rate Limiting**: Prevent API abuse
- **Input Sanitization**: Prevent injection attacks
- **Certificate Pinning**: Prevent MITM attacks

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run UI tests
./gradlew connectedCheck
```

## 📦 Build Variants

- **Debug**: Development build with logging
- **Staging**: Pre-production testing
- **Release**: Production build with ProGuard

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Project Lead**: Your Name
- **Backend Developer**: TBD
- **UI/UX Designer**: TBD
- **AI/ML Engineer**: TBD

## 📞 Support

For support, email support@aimusicplayer.com or join our Discord server.

## 🗺️ Roadmap

### Phase 1 (Q1 2025)
- ✅ Basic music playback
- ✅ User authentication
- ✅ Local storage support
- ⏳ Admin panel basic features

### Phase 2 (Q2 2025)
- ⏳ AI lyrics integration
- ⏳ Recommendation system
- ⏳ Cloud storage integration
- ⏳ Social features (sharing)

### Phase 3 (Q3 2025)
- ⏳ Podcast support
- ⏳ Collaborative playlists
- ⏳ Advanced analytics
- ⏳ Offline lyrics

### Phase 4 (Q4 2025)
- ⏳ AI voice commands
- ⏳ Cross-device sync
- ⏳ High-res audio support
- ⏳ Widget customization

## 🙏 Acknowledgments

- Material Design guidelines by Google
- ExoPlayer team for excellent media library
- Open source community for amazing libraries

---

**Made with ❤️ by the AI Music Player Team**
