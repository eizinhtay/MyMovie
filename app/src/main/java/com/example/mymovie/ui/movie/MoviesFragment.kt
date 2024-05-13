package com.example.mymovie.ui.movie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovie.databinding.FragmentMovieBinding
import com.example.mymovie.utils.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private lateinit var mAdapter: MovieRecyclerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        setUpNetwork()
        setUpRecyclerview()
        listeners()
        return binding.root

    }

    private fun setUpNetwork() {

    }

    private fun listeners() {

        // Search functionality
        binding.searchMovie.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    filterMovies(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterMovies(newText)
                }
                return true
            }
        })


        binding.searchMovie.setOnCloseListener {
            loadMovies()
            false
        }


    }


    private fun filterMovies(newText: String) {
        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                // Network is available
                moviesViewModel.searchMovies(newText)

                moviesViewModel.moviePagingList.observe(viewLifecycleOwner){
                    CoroutineScope(Dispatchers.Main).launch {
                        mAdapter.submitData(viewLifecycleOwner.lifecycle,it)

                    }
                }

            } else {
                // Network is not available
                moviesViewModel.searchMoviesLocalPaging(newText)

                moviesViewModel.movieLocalPagingList.observe(viewLifecycleOwner){
                    CoroutineScope(Dispatchers.Main).launch {
                        mAdapter.submitData(viewLifecycleOwner.lifecycle,it)
                    }
                }


            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setUpRecyclerview() {
        mAdapter = MovieRecyclerAdapter(requireContext(),object :MoviesDelegate{
            override fun onMovieItemClick(movieId: Int) {
                findNavController().navigate(MoviesFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId))
            }

        })
        binding.rvMovies.layoutManager =
            GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false)
        binding.rvMovies.adapter = mAdapter


        loadMovies()




    }

    private fun loadMovies() {
        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner, Observer {isConnected ->

            if (isConnected) {
                // Network is available
                moviesViewModel.getAllMovies(query="")

                moviesViewModel.moviePagingList.observe(viewLifecycleOwner){
                    CoroutineScope(Dispatchers.Main).launch {
                        mAdapter.submitData(viewLifecycleOwner.lifecycle,it)
                    }
                }
            } else {
                // Network is not available
                /* val localMovies= moviesViewModel.getLocalMovies()
                  mAdapter.setItems(localMovies.toMutableList())*/

                moviesViewModel.getMoviesLocalPaging()
                moviesViewModel.movieLocalPagingList.observe(viewLifecycleOwner){
                    CoroutineScope(Dispatchers.Main).launch {
                        mAdapter.submitData(viewLifecycleOwner.lifecycle,it)

                    }
                }


            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun onResume() {
        loadMovies()
        super.onResume()
    }
}