package com.fias.ddrhighspeed.search.songdetail.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fias.ddrhighspeed.database.SongApplication
import com.google.accompanist.themeadapter.material.MdcTheme
import androidx.compose.ui.graphics.Color as ComposeColor

class SongMovieFragment(songId: Long) : Fragment() {

    private val viewModel: SongMovieViewModel by viewModels {
        SongMovieViewModelFactory(
            (activity?.application as SongApplication).db,
            songId
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    Surface {
                        val list = viewModel.movieList

                        Column {
                            list.forEach { movie ->
                                val composeColor = ComposeColor(movie.color.toArgb())
                                ListItem(
                                    text = { Text(movie.label) },
                                    trailing = { YoutubeVideo(url = movie.url) },
                                    icon = {
                                        var expanded by remember { mutableStateOf(false) }
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(
                                                imageVector = if (expanded) Icons.Filled.ArrowForward else Icons.Filled.ArrowDropDown,
                                                contentDescription = null,
                                                tint = composeColor
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun YoutubeVideo(url: String) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            },
            update = { view -> view.loadUrl(url) }
        )
    }
}
