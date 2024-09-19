package com.greencodemoscow.redbook.support.presentation.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentRedBookBinding
import com.greencodemoscow.redbook.databinding.FragmentReportBinding
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookAction
import com.greencodemoscow.redbook.redBook.presentation.viewmodel.RedBookViewModel
import com.greencodemoscow.redbook.support.presentation.adapter.PhotosAdapter
import com.greencodemoscow.redbook.support.presentation.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ReportFragment : Fragment(R.layout.fragment_report) {

    private val binding by viewBinding(FragmentReportBinding::bind)
    private val viewModel: ReportViewModel by viewModels()

    private lateinit var photosAdapter: PhotosAdapter
    private val selectedPhotos = mutableListOf<Uri>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Настройка Spinner (AutoCompleteTextView) для категории
        val categories = resources.getStringArray(R.array.category_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.autoCompleteCategory.setAdapter(adapter)
        binding.autoCompleteCategory.threshold = 0

        // Обработка выбора категории
        binding.autoCompleteCategory.setOnClickListener {
            binding.autoCompleteCategory.showDropDown()
        }
//        binding.autoCompleteCategory.setOnItemClickListener { parent, view, position, id ->
//            binding.autoCompleteCategory.showDropDown()
//            val selectedCategory = categories[position]
//            if (selectedCategory == "Другое" || selectedCategory == "Сообщение о новом растении") {
//                binding.layoutCustomCategory.visibility = View.VISIBLE
//            } else {
//                binding.layoutCustomCategory.visibility = View.GONE
//            }
//        }

        // Настройка RecyclerView для фотографий
        photosAdapter = PhotosAdapter(selectedPhotos) { position ->
            // Удаление фото при клике
            selectedPhotos.removeAt(position)
            photosAdapter.notifyItemRemoved(position)
            if (selectedPhotos.isEmpty()) {
                binding.recyclerViewPhotos.visibility = View.GONE
            }
        }
        binding.recyclerViewPhotos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPhotos.adapter = photosAdapter

        // Лаунчер для выбора изображений
        val pickImagesLauncher = registerForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) { uris: List<Uri> ->
            if (uris.isNotEmpty()) {
                selectedPhotos.addAll(uris)
                photosAdapter.notifyDataSetChanged()
                binding.recyclerViewPhotos.visibility = View.VISIBLE
            }
        }

        // Обработка кнопки добавления фотографий
        binding.buttonAddPhotos.setOnClickListener {
            pickImagesLauncher.launch("image/*")
        }

        // Обработка выбора даты
        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        // Обработка выбора времени
        binding.buttonSelectTime.setOnClickListener {
            showTimePicker()
        }

        // Обработка кнопок местоположения
        binding.buttonAutoLocation.setOnClickListener {
            // Здесь реализуйте логику автоматического определения местоположения
            // Для примера показываем Toast
            Toast.makeText(requireContext(), "Автоматически определено местоположение", Toast.LENGTH_SHORT).show()
            binding.textSelectedLocation.text = "Москва, Россия"
        }

        binding.buttonManualLocation.setOnClickListener {
            // Здесь реализуйте логику ручного ввода местоположения
            // Например, открытие карты или другого поля ввода
            Toast.makeText(requireContext(), "Функция в разработке", Toast.LENGTH_SHORT).show()
        }

        // Обработка кнопки отправки
        binding.buttonSubmit.setOnClickListener {
            submitReport()
        }

        // Обработка кнопки "Назад"
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Обработка кнопки информации
        binding.buttonInfo.setOnClickListener {
            // Здесь можно открыть диалог или другую активность с информацией
            Toast.makeText(requireContext(), "Информация о заполнении формы", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                binding.textSelectedDate.text = "$dayOfMonth.${month + 1}.$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                binding.textSelectedTime.text = String.format("%02d:%02d", hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun submitReport() {
        // Сбор данных из формы
        val category = binding.autoCompleteCategory.text.toString()
        val customCategory = binding.editTextCustomCategory.text.toString()
        val objectName = binding.editTextObjectName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val location = binding.textSelectedLocation.text.toString()
        val date = binding.textSelectedDate.text.toString()
        val time = binding.textSelectedTime.text.toString()
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val publishConsent = binding.checkboxPublish.isChecked
        val receiveNotifications = binding.checkboxNotifications.isChecked

        // Валидация обязательных полей
        if (category.isEmpty() || objectName.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Пожалуйста, заполните обязательные поля", Toast.LENGTH_SHORT).show()
            return
        }

        // Здесь добавьте логику отправки данных на сервер или другую обработку
        // Для примера показываем Toast с собранными данными
        Toast.makeText(requireContext(), "Обращение отправлено", Toast.LENGTH_SHORT).show()

        // Очистка формы после отправки
        clearForm()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun clearForm() {
        binding.autoCompleteCategory.text.clear()
        binding.editTextCustomCategory.text?.clear()
        binding.layoutCustomCategory.visibility = View.GONE
        binding.editTextObjectName.text?.clear()
        binding.editTextDescription.text?.clear()
        binding.textSelectedLocation.text = ""
        binding.textSelectedDate.text = ""
        binding.textSelectedTime.text = ""
        binding.editTextName.text?.clear()
        binding.editTextEmail.text?.clear()
        binding.editTextPhone.text?.clear()
        binding.checkboxPublish.isChecked = false
        binding.checkboxNotifications.isChecked = false
        selectedPhotos.clear()
        photosAdapter.notifyDataSetChanged()
        binding.recyclerViewPhotos.visibility = View.GONE
    }
}