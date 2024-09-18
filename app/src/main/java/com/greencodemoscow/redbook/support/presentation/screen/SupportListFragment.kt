package com.greencodemoscow.redbook.support.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentSupportListBinding
import com.greencodemoscow.redbook.support.data.model.InquiriesData
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

        binding.floatingActionButton.setOnClickListener {
            // Навигация к ReportFragment
            findNavController().navigate(R.id.action_supportListFragment_to_reportFragment)
        }
        val dummyInquiries = listOf(
            InquiriesData(name = "Ландышь Майский", descritption = "Обнаружено редкое растение", date = "2024-09-19", location = "", status = "Отправлено", photo = R.drawable.lily),
            InquiriesData(name = "Орешниковая соня", descritption = "Обнаружено редкое животное", date = "2024-09-18", location = "", status = "Принято к рассмотрению", photo = R.drawable.hazel),
        )

        if (dummyInquiries.isEmpty()) {
            binding.inquiriesRecyclerView.visibility = View.GONE
            binding.inquiriesNote.visibility = View.VISIBLE
        } else {
            binding.inquiriesRecyclerView.visibility = View.VISIBLE
            binding.inquiriesNote.visibility = View.GONE
            inquiriesAdapter.submitList(dummyInquiries)
        }
    }
}