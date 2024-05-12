package com.example.mymovie.ui.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mymovie.data.models.movieDetail.Genre
import com.example.mymovie.data.models.movieDetail.MovieDetails
import com.example.mymovie.databinding.FragmentMovieDetailBinding
import com.example.mymovie.utils.IMAGE_BASE_URL
import com.example.mymovie.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var movieId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        loadMovieDetail()
        setUI()
        listener()
        return binding.root

    }

    private fun listener() {
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

    }

    private fun loadMovieDetail() {
        val bundle = arguments

        val args = bundle?.let { MovieDetailFragmentArgs.fromBundle(it) }
        movieId = args?.movieId?:0
        movieDetailViewModel.getMovieDetail(movieId)
    }

    private fun setUI() {
        movieDetailViewModel.movieDetails.observe(viewLifecycleOwner){
            if (it!=null){
                binding.bindData(it)
            }
        }

        movieDetailViewModel.movieError.observe(viewLifecycleOwner){
            showToastMessage(requireContext(),it)
        }

    }
    private fun FragmentMovieDetailBinding.bindData(movie: MovieDetails?) {
        movie?.apply {
            Glide.with(requireContext()).load("$IMAGE_BASE_URL${posterPath}")
                .into(ivMovieDetailPoster)
            ctlDetail.title = title
            tvMovieName.text = title

            if (genres?.isNotEmpty() == true){
                bindForGenres(
                    genres
                )
            }

            tvOverview.text = overview
            tvOriginalTitle.text = title
            tvType.text = genres?.joinToString(", ") { it.name } ?: ""
            tvProduction.text = productionCountries?.joinToString(", ") { it.name } ?: ""
            tvPremiere.text = releaseDate
            tvDescription.text = this.overview
        }
    }

    private fun bindForGenres(genres: List<Genre>) {
        binding.apply {
            val size = genres.size
            if (size < 3) {
                tvThirdGenre.visibility = View.GONE
            } else {

                tvThirdGenre.visibility = View.VISIBLE
                tvSecondGenre.visibility = View.VISIBLE
                tvFirstGenre.visibility =View.VISIBLE
                genres.getOrNull(0)?.let { genreVO ->
                    tvFirstGenre.text = genreVO.name
                }
                genres.getOrNull(1)?.let { genreVO ->
                    tvSecondGenre.text = genreVO.name
                }
                genres.getOrNull(2)?.let { genreVO ->
                    tvThirdGenre.text = genreVO.name
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}