package com.example.mymovie.ui.movieVideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymovie.data.models.movieVideo.MovieVideo
import com.example.mymovie.databinding.FragmentMovieVideoBinding
import com.example.mymovie.ui.movieDetail.MovieDetailFragmentArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieVideoFragment : Fragment() {
    private var _binding: FragmentMovieVideoBinding? = null
    private var youtubePlayer: YouTubePlayer?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = MovieVideoFragment()
    }

    private val viewModel: MovieVideoViewModel by viewModels()
    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMovieVideo()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieVideoBinding.inflate(inflater, container, false)
        setUpUI()
        listener()

        return binding.root
    }

    private fun listener() {
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

    }

    private fun loadMovieVideo() {
        val bundle = arguments

        val args = bundle?.let { MovieDetailFragmentArgs.fromBundle(it) }
        movieId = args?.movieId?:0
        viewModel.getMovieVideos(movieId)

    }

    private fun setUpUI() {

        viewModel.movieVideos.observe(viewLifecycleOwner){
            if (it!=null){
                initYouTubePlayerView(it.results)
            }
        }
    }

    private fun initYouTubePlayerView(results: List<MovieVideo?>?) {
        binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youtubePlayer = youTubePlayer
                if (results?.isNotEmpty()==true){
                    results[0]?.key?.let { youTubePlayer.loadVideo(it, 0f) }
                }
            }
        })
    }



}