package com.greencodemoscow.redbook.support.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentSupportListBinding
import com.greencodemoscow.redbook.support.presentation.adapter.InquiriesAdapter

class SupportListFragment : Fragment(R.layout.fragment_support_list) {
    private val binding by viewBinding(FragmentSupportListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inquiriesAdapter = InquiriesAdapter {

        }

        binding.inquiriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inquiriesAdapter
        }

    }
}