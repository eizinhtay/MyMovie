package com.example.mymovie.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovie.databinding.FragmentMovieBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val moviesViewModel:MoviesViewModel by viewModels()
    private lateinit var mAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        moviesViewModel.getMovies()
        setUpRecyclerview()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setUpRecyclerview() {
        mAdapter = MoviesAdapter(object :MoviesDelegate{
            override fun onMovieItemClick(movieId: Int) {
                findNavController().navigate(MoviesFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId))
            }

        })
        binding.rvMovies.layoutManager =
            GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false)
        binding.rvMovies.adapter = mAdapter
        mAdapter.setItems(moviesViewModel.getLocalMovies().toMutableList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}