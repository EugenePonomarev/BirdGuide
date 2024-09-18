package com.greencodemoscow.redbook.core.presentation.widgets

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import com.greencodemoscow.redbook.databinding.CustomFilterSearchViewBinding

@SuppressLint("ClickableViewAccessibility")
class CustomFilterSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomFilterSearchViewBinding =
        CustomFilterSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    var onSearchTextChanged: ((String) -> Unit)? = null
    var onFilterChanged: ((Boolean, Boolean) -> Unit)? = null
    var onParkSelected: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL
        setupFilterIcon()
        setupSearchInput()
        setupParkSelection()
        setupFilterButtons()
    }

    private fun setupFilterIcon() {
        binding.filterIcon.setOnClickListener {
            toggleFilterPanelVisibility()
        }
    }

    private fun setupSearchInput() {
        binding.searchInput.setOnEditorActionListener { _, _, _ ->
            onSearchTextChanged?.invoke(binding.searchInput.text.toString())
            true
        }


        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            onSearchTextChanged?.invoke(text.toString())
        }
    }

    private fun setupParkSelection() {
        binding.autoCompleteParks.setOnClickListener {
            binding.autoCompleteParks.showDropDown()
        }

        binding.autoCompleteParks.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedPark = parent.getItemAtPosition(position) as String
                onParkSelected?.invoke(selectedPark)
                binding.clearButton.visibility = View.VISIBLE
            }

        binding.clearButton.setOnClickListener {
            binding.autoCompleteParks.text.clear()
            binding.clearButton.visibility = View.GONE
            onParkSelected?.invoke("")
        }
    }

    private fun setupFilterButtons() {
        binding.btnAnimals.isChecked = true
        binding.btnPlants.isChecked = true

        binding.toggleButtonGroup.addOnButtonCheckedListener { _, _, _ ->
            val isAnimalSelected = binding.btnAnimals.isChecked
            val isPlantSelected = binding.btnPlants.isChecked

            // Callback with current state of buttons
            onFilterChanged?.invoke(isAnimalSelected, isPlantSelected)
        }
    }

    private fun toggleFilterPanelVisibility() {
        val isVisible = binding.filterPanel.visibility == View.VISIBLE
        binding.filterPanel.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    fun setParks(parks: List<String>) {
        if (parks.isEmpty()) {
            binding.textInputLayoutParks.visibility = View.GONE
            binding.autoCompleteParks.visibility = View.GONE
        } else {
            binding.textInputLayoutParks.visibility = View.VISIBLE
            binding.autoCompleteParks.visibility = View.VISIBLE
            val adapter = ArrayAdapter(context, R.layout.simple_list_item_1, parks)
            binding.autoCompleteParks.setAdapter(adapter)
            binding.autoCompleteParks.threshold = 0
        }
    }
}