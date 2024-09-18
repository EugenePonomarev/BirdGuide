package com.greencodemoscow.redbook.redBook.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.app.presentation.screen.MainActivity
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentRedBookBinding
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookAction
import com.greencodemoscow.redbook.redBook.presentation.screen.adapter.RedBookItemsAdapter
import com.greencodemoscow.redbook.redBook.presentation.viewmodel.RedBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedBookFragment : Fragment(R.layout.fragment_red_book) {
    private val binding by viewBinding(FragmentRedBookBinding::bind)
    private val viewModel: RedBookViewModel by viewModels()
    private lateinit var adapter: RedBookItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendAction(RedBookAction.Initialize(requireContext()))

        initItemsList()
        initCustomFilterSearchView()
        stateSubscribe()
    }

    private fun initCustomFilterSearchView() {
        val parks = viewModel.getParksList()
        binding.customFilterSearchView.setParks(parks)

        binding.customFilterSearchView.onFilterChanged = { isAnimalsChecked, isPlantsChecked ->
            viewModel.sendAction(RedBookAction.FilterByType(isAnimalsChecked, isPlantsChecked))
        }

        binding.customFilterSearchView.onParkSelected = { selectedPark ->
            if (selectedPark.isEmpty()) {
                viewModel.sendAction(RedBookAction.ShowAllItems)
            } else {
                viewModel.sendAction(RedBookAction.FilterByPark(selectedPark))
            }
        }

        binding.customFilterSearchView.onSearchTextChanged = { text ->
            viewModel.sendAction(RedBookAction.Search(text))
        }
    }

    private fun initItemsList() {
        binding.redBookList.setHasFixedSize(true)
        binding.redBookList.layoutManager = LinearLayoutManager(requireContext())
        adapter = RedBookItemsAdapter { item ->
            val action = RedBookFragmentDirections.actionRedBookFragmentToRedBookItemFragment(item)
            findNavController().navigate(action)
        }
        binding.redBookList.adapter = adapter

        binding.redBookList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    hideBottomNavigationView()
                } else if (dy < 0) {
                    showBottomNavigationView()
                }
            }
        })
    }

    private fun stateSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    adapter.submitList(state.searchList)
                    showBottomNavigationView()
                }
            }
        }
    }

    private fun hideBottomNavigationView() {
        (activity as? MainActivity)?.binding?.bottomNavigationView?.let { bottomNav ->
            bottomNav.animate().translationY(bottomNav.height.toFloat()).duration = 200
        }
    }

    private fun showBottomNavigationView() {
        (activity as? MainActivity)?.binding?.bottomNavigationView?.let { bottomNav ->
            bottomNav.animate().translationY(0f).duration = 200
        }
    }
}