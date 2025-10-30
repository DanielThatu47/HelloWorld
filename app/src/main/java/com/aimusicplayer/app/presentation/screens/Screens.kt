package com.aimusicplayer.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import coil.compose.AsyncImage
import com.aimusicplayer.app.presentation.viewmodel.AuthViewModel
import com.aimusicplayer.app.presentation.viewmodel.PlayerViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            // Email and password fields would go here
            // For now, placeholder
            
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Button(
                onClick = {
                    // Login logic
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Login")
                }
            }
            
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Button(
                onClick = {
                    // Signup logic
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Sign Up")
                }
            }
            
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login")
            }
        }
    }
}

@Composable
fun PlayerScreen(
    navController: NavController,
    musicService: com.aimusicplayer.app.service.MusicPlayerService?,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerState by viewModel.playerState.collectAsState()
    val currentSong = playerState.currentSong

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            currentSong?.let { song ->
                AsyncImage(
                    model = song.coverImageUrl ?: "",
                    contentDescription = song.title,
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.playPrevious() }) {
                        Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Previous")
                    }
                    
                    IconButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = if (playerState.isPlaying) {
                                Icons.Default.Pause
                            } else {
                                Icons.Default.PlayArrow
                            },
                            contentDescription = if (playerState.isPlaying) "Pause" else "Play"
                        )
                    }
                    
                    IconButton(onClick = { viewModel.playNext() }) {
                        Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Next")
                    }
                }
            } ?: run {
                Text("No song playing")
            }
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Search") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Search Screen")
        }
    }
}

@Composable
fun LibraryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Library") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Library Screen")
        }
    }
}

@Composable
fun LyricsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lyrics") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Lyrics Screen")
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Settings Screen")
        }
    }
}

@Composable
fun AdminScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Panel") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Admin Panel")
        }
    }
}
