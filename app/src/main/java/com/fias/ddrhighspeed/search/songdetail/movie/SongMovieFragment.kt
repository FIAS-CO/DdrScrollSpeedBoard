package com.fias.ddrhighspeed.search.songdetail.movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fias.ddrhighspeed.database.SongApplication
import com.google.accompanist.themeadapter.material.MdcTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class SongMovieFragment(private val songName: String, songId: Long) : Fragment() {

    private val viewModel: SongMovieViewModel by viewModels {
        SongMovieViewModelFactory(
            (activity?.application as SongApplication).db,
            songId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    Surface {
                        LazyColumn {
                            itemsIndexed(viewModel.movieList) { _, movie ->
                                YoutubeItem(movie)
                            }
                            item {
                                OpenYoutubeButton(songName)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun YoutubeItem(movie: MovieModel) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "■ ${movie.label}",
                color = movie.color,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            YoutubeScreen(movie.movieId)
        }
    }

    @Composable
    fun YoutubeScreen(videoId: String) {
        AndroidView(factory = {
            YouTubePlayerView(it).apply {
                this.addYouTubePlayerListener(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    }
                )
            }
        })
    }

    @Composable
    fun OpenYoutubeButton(songName: String) {
        val context = LocalContext.current
        val uri = Uri.parse("https://www.youtube.com/results?search_query=DDR+${songName}")
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                    setPackage("com.google.android.youtube")
                }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(fallbackIntent)
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Search \"${songName}\" on YouTube")
        }
    }
}
