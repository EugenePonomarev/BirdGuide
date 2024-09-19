package com.greencodemoscow.redbook.redBook.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.greencodemoscow.redbook.redBook.data.model.RedBookItem
import com.greencodemoscow.redbook.redBook.data.model.RedBookTypes
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookAction
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

private const val ANIMAL_TAG = "Animal"
private const val PLANT_TAG = "Plant"
private const val OTHER_TAG = "Plant"

@HiltViewModel
class RedBookViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(RedBookState())
    val state: StateFlow<RedBookState> = _state.asStateFlow()

    init {
        initializeItems()
    }

    fun sendAction(action: RedBookAction) {
        when (action) {
            is RedBookAction.Search -> updateSearchResults(action.query)
            is RedBookAction.FilterByType -> filterByType(action.isAnimalsChecked, action.isPlantsChecked)
            is RedBookAction.FilterByPark -> filterByPark(action.selectedPark)
            is RedBookAction.ShowAllItems -> showAllItems()
            is RedBookAction.Initialize -> initializeItems()
        }
    }

    private fun updateSearchResults(query: String) {
        val normalizedQuery = normalizeSearchQuery(query)
        val filteredList = _state.value.items.filter {
            it.name.contains(Regex(normalizedQuery, RegexOption.IGNORE_CASE)) &&
                    ((_state.value.isAnimalsChecked && it.type == ANIMAL_TAG) ||
                            (_state.value.isPlantsChecked && it.type == PLANT_TAG)) &&
                    (_state.value.selectedPark == null || _state.value.selectedPark == it.habitatArea)
        }

        val finalList = if (_state.value.isAnimalsChecked && _state.value.isPlantsChecked) {
            _state.value.items.filter {
                it.name.contains(Regex(normalizedQuery, RegexOption.IGNORE_CASE)) &&
                        (_state.value.selectedPark == null || _state.value.selectedPark == it.habitatArea)
            }
        } else {
            filteredList
        }

        _state.value = _state.value.copy(searchList = finalList)
    }

    private fun normalizeSearchQuery(query: String): String {
        return query.replace("е", "[её]")
    }

    private fun filterByType(isAnimalsChecked: Boolean, isPlantsChecked: Boolean) {
        val filteredList = _state.value.items.filter {
            (isAnimalsChecked && it.type == ANIMAL_TAG) ||
                    (isPlantsChecked && it.type == PLANT_TAG)
        }

        val finalList = if (isAnimalsChecked && isPlantsChecked) {
            _state.value.items
        } else {
            filteredList
        }

        _state.value = _state.value.copy(
            isAnimalsChecked = isAnimalsChecked,
            isPlantsChecked = isPlantsChecked,
            searchList = finalList
        )
    }

    private fun initializeItems() {
        val items = generateTestData()
        _state.value = _state.value.copy(items = items, searchList = items)
    }

    private fun filterByPark(selectedPark: String) {
        val filteredList = _state.value.items.filter { item ->
            (_state.value.isAnimalsChecked && item.type == ANIMAL_TAG ||
                    _state.value.isPlantsChecked && item.type == PLANT_TAG) &&
                    (selectedPark.isEmpty() || item.habitatArea == selectedPark)
        }

        _state.value = _state.value.copy(selectedPark = selectedPark, searchList = filteredList)
    }

    private fun showAllItems() {
        _state.value = _state.value.copy(searchList = _state.value.items)
    }

    private val parks = listOf(
        "Александровский сад" to "55.7517, 37.6176 E",
        "Измайловский парк" to "55.7907 N, 37.7483 E",
        "Коломенское" to "55.6738 N, 37.6647 E",
        "Лосиный Остров" to "55.8350 N, 37.7600 E",
        "Сокольники" to "55.7949 N, 37.6745 E",
        "Парк Горького" to "55.7296 N, 37.6030 E",
        "Царицыно" to "55.6129 N, 37.6695 E",
        "Ботанический сад МГУ" to "55.7012 N, 37.5300 E",
        "Воробьёвы горы" to "55.7100 N, 37.5550 E",
        "Нескучный сад" to "55.7248 N, 37.5891 E"
    )

    fun getParksList(): List<String> {
        return parks.map { it.first }
    }

    private fun generateTestData(): List<RedBookItem> {
        val plants = listOf(
            RedBookItem(name = "Ландыш майский", image = "images/Ландыш_майский.jfif", description =  "Многолетнее травянистое растение с красивыми белыми ароматными цветами. Ландыш предпочитает светлые лиственные леса, но в последние годы его численность снижается из-за вырубки лесов и несанкционированного сбора.", type =  PLANT_TAG, habitatArea = "Александровский сад"),
            RedBookItem(name ="Венерин башмачок", image = "images/Венерин_башмачок.jfif", description = "Редкий вид орхидеи, который встречается в тенистых и влажных лесах. Обладает крупными желтыми цветами в форме туфельки. Находится под угрозой исчезновения из-за вырубки лесов и изменения их структуры.", habitatArea = "Александровский сад", type = PLANT_TAG),
            RedBookItem(name ="Берёза карликовая", image = "images/Венерин_башмачок.jfif", description = "Небольшое деревце или кустарник, который встречается в северных и арктических регионах. В Москве этот вид редок, его можно встретить на возвышенностях и открытых пространствах. Береза карликовая считается реликтом ледникового периода и занесена в Красную книгу из-за сокращения ареала обитания.", habitatArea = "Царицыно", type = PLANT_TAG),
            RedBookItem(      name = "Пальчатокоренник балтийский", description = "Редкое растение семейства орхидных, обитающее на влажных лугах, в болотах и поймах рек. Отличается красивыми розовыми или фиолетовыми цветами. Вид исчезает из-за изменения природных условий и осушения болот.", image = "images/Пальчатокоренник_балтийский.jpg", habitatArea = "Царицыно", type = PLANT_TAG),
            RedBookItem(name = "Купальница европейская", description = "Многолетнее травянистое растение с яркими желтыми цветами, произрастающее на влажных лугах, берегах рек и болотах. Сокращение численности этого растения связано с мелиорацией земель и изменением природных условий.", image ="images/Купальница_европейская.jpg", habitatArea = "Царицыно", type = PLANT_TAG),
            RedBookItem(name = "Ковыль перистый", description = "Травянистое растение, характерное для степных и полустепных зон. В Москве встречается редко, в основном на хорошо освещенных холмах и склонах. Угроза исчезновения связана с распашкой земель и сокращением естественных мест обитания.", image = "images/Ковыль_перистый.jpeg", type = PLANT_TAG, habitatArea = "Нескучный сад"),
            RedBookItem(name = "Лютик прудный", description = "Однолетнее или многолетнее растение, произрастающее на сырых лугах, берегах водоемов и в заболоченных местах. Включено в Красную книгу из-за изменения водного режима и деградации мест обитания.", image = "images/Лютик_прудный.jpg", habitatArea = "Нескучный сад", type = PLANT_TAG),
            RedBookItem(name = "Осока Дэвида", description = "Растет на влажных лугах и болотах. Обладает узкими листьями и мелкими соцветиями. Сокращение численности связано с осушением болот и изменением гидрологического режима.", image = "images/Осока_дэвида.jpg", habitatArea = "Парк Горького", type = PLANT_TAG),
            // Add other plant items here...
        )

        val animals = listOf(
            RedBookItem(name ="Лесная мышовка", image ="images/Лесная_мышовка.jpg", description = "Маленький грызун длиной до 7,6 см с длинным хвостом, достигающим 10 см. Обитает в лесах, зарослях кустарников и лесопарках, предпочитая старые гнилые пни для строительства своих гнезд. Питается насекомыми. В Москве встречается на территории Восточного административного округа (Измайловский парк, Лосиный Остров)", type = ANIMAL_TAG, habitatArea = "Нескучный сад"),
            RedBookItem(name ="Орешниковая соня", image ="images/Орешниковая_соня.jfif", description = "Небольшой грызун длиной до 15 см с пушистым хвостом. Живет в смешанных и широколиственных лесах, питается орехами и другими плодами. Вид исчезающий, в Москве обитает в Восточном и Юго-западном округах (Измайловский парк, Лосиный остров, Битцевский лес)",type =  ANIMAL_TAG, habitatArea = "Нескучный сад"),
            RedBookItem(name ="Северная летучая мышь", image ="images/Орешниковая_соня.jfif", description = "Небольшой вид летучих мышей, обитающий в лесах, парках и садах. Охотится на насекомых в ночное время. Численность сокращается из-за разрушения мест обитания и использования инсектицидов.",type =  ANIMAL_TAG, habitatArea = "Лосиный Остров"),
            RedBookItem(name ="Филин обыкновенный", image ="images/Орешниковая_соня.jfif", description = "Крупная ночная сова с характерными ушками. Обитает в лесах и охотится на мелких млекопитающих и птиц. Вид страдает из-за разрушения лесных массивов и беспокойства со стороны человека",type =  ANIMAL_TAG, habitatArea = "Измайловский парк"),
            RedBookItem(name ="Лебедь-кликун", image ="images/Орешниковая_соня.jfif", description = "Крупная водоплавающая птица, гнездящаяся на озерах и прудах. Популяция в Москве уменьшается из-за загрязнения водоемов и нарушения среды обитания",type =  ANIMAL_TAG, habitatArea = "Измайловский парк"),
            RedBookItem(name ="Зелёная жаба", image ="images/Орешниковая_соня.jfif", description = "Земноводное, обитающее в светлых лесах и на опушках. Предпочитает влажные участки. Численность снижается из-за утраты местообитаний и изменения гидрологического режима.",type =  ANIMAL_TAG, habitatArea = "Лосиный Остров"),
        )

        return plants + animals
    }
}
