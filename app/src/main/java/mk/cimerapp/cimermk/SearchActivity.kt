package mk.cimerapp.cimermk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.CheckBox
import android.widget.TextView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var filteredList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var searchEditText: EditText

    private lateinit var minPriceEditText: EditText

    private lateinit var maxPriceEditText: EditText

    private lateinit var genderSpinner: Spinner

    private lateinit var citySpinner: Spinner

    private lateinit var tvShownPosts: TextView

    private lateinit var cbRoommate: CheckBox

    private lateinit var cbApartment: CheckBox

    private lateinit var cbOffering: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView =
            findViewById(R.id.recyclerSearch)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        postList = arrayListOf()

        filteredList = arrayListOf()

        adapter = PostAdapter(filteredList)

        recyclerView.adapter = adapter

        searchEditText =
            findViewById(R.id.etSearch)

        minPriceEditText =
            findViewById(R.id.etMinPrice)

        maxPriceEditText =
            findViewById(R.id.etMaxPrice)

        genderSpinner =
            findViewById(R.id.spGender)

        citySpinner =
            findViewById(R.id.spSearchCity)

        tvShownPosts =
            findViewById(R.id.tvShownSearchPosts)

        cbRoommate =
            findViewById(R.id.cbSearchRoommate)

        cbApartment =
            findViewById(R.id.cbSearchApartment)

        cbOffering =
            findViewById(R.id.cbSearchOffering)

        val genderOptions = arrayOf(
            getString(R.string.all),
            getString(R.string.male),
            getString(R.string.female)
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderOptions
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        genderSpinner.adapter = spinnerAdapter

        val cityOptions = arrayOf(
            getString(R.string.all_cities),
            "Аеродром",
            "Арачиново",
            "Берово",
            "Битола",
            "Богданци",
            "Боговиње",
            "Босилово",
            "Брвеница",
            "Бутел",
            "Валандово",
            "Василево",
            "Вевчани",
            "Велес",
            "Виница",
            "Вранештица",
            "Врапчиште",
            "Гази Баба",
            "Гевгелија",
            "Гостивар",
            "Градско",
            "Дебар",
            "Дебарца",
            "Делчево",
            "Демир Капија",
            "Демир Хисар",
            "Дојран",
            "Долнени",
            "Другово",
            "Желино",
            "Зајас",
            "Зелениково",
            "Зрновци",
            "Илинден",
            "Јегуновце",
            "Кавадарци",
            "Карбинци",
            "Карпош",
            "Кичево",
            "Кисела Вода",
            "Конче",
            "Кочани",
            "Кратово",
            "Крива Паланка",
            "Кривогаштани",
            "Крушево",
            "Куманово",
            "Липково",
            "Лозово",
            "Маврово и Ростуше",
            "Македонска Каменица",
            "Македонски Брод",
            "Могила",
            "Неготино",
            "Новаци",
            "Ново Село",
            "Осломеј",
            "Охрид",
            "Петровец",
            "Пехчево",
            "Пласница",
            "Прилеп",
            "Пробиштип",
            "Радовиш",
            "Ранковце",
            "Ресен",
            "Росоман",
            "Сарај",
            "Свети Николе",
            "Сопиште",
            "Старо Нагоричане",
            "Струга",
            "Струмица",
            "Студеничани",
            "Теарце",
            "Тетово",
            "Центар",
            "Центар Жупа",
            "Чаир",
            "Чашка",
            "Чешиново и Облешево",
            "Чучер Сандево",
            "Штип",
            "Шуто Оризари",
            "Ѓорче Петров"
        )

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cityOptions
        )

        cityAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        citySpinner.adapter = cityAdapter

        loadPosts()

        searchEditText.addTextChangedListener(searchWatcher)

        minPriceEditText.addTextChangedListener(searchWatcher)

        maxPriceEditText.addTextChangedListener(searchWatcher)

        genderSpinner.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    filterPosts()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
        )

        citySpinner.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    filterPosts()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
        )

        cbRoommate.setOnCheckedChangeListener { _, _ ->
            filterPosts()
        }

        cbApartment.setOnCheckedChangeListener { _, _ ->
            filterPosts()
        }

        cbOffering.setOnCheckedChangeListener { _, _ ->
            filterPosts()
        }
    }

    private val searchWatcher =
        object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                filterPosts()
            }

            override fun afterTextChanged(
                s: Editable?
            ) {
            }
        }

    private fun loadPosts() {

        FirebaseFirestore.getInstance()
            .collection("posts")
            .get()
            .addOnSuccessListener { documents ->

                postList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(Post::class.java)

                    post.documentId = document.id

                    postList.add(post)
                }

                filteredList.clear()

                filteredList.addAll(postList)

                tvShownPosts.text =
                    getString(
                        R.string.shown_posts,
                        filteredList.size
                    )

                adapter.notifyDataSetChanged()
            }
    }

    private fun filterPosts() {

        val searchText =
            searchEditText.text.toString()
                .trim()
                .lowercase()

        val minPrice =
            minPriceEditText.text.toString()
                .toIntOrNull() ?: 0

        val maxPrice =
            maxPriceEditText.text.toString()
                .toIntOrNull() ?: Int.MAX_VALUE

        val selectedGender =
            genderSpinner.selectedItem.toString()


        val selectedGenderCode =
            when (selectedGender) {
                getString(R.string.male) -> "male"
                getString(R.string.female) -> "female"
                else -> "all"
            }

        val selectedCity =
            citySpinner.selectedItem.toString()

        val roommateChecked =
            cbRoommate.isChecked

        val apartmentChecked =
            cbApartment.isChecked

        val offeringChecked =
            cbOffering.isChecked

        filteredList.clear()

        for (post in postList) {

            val postPrice =
                post.price.toIntOrNull() ?: 0

            val postGenderCode =
                when (post.gender.lowercase()) {
                    "female", "женско" -> "female"
                    "male", "машко" -> "male"
                    else -> post.gender.lowercase()
                }

            val genderText =
                if (postGenderCode == "female") {
                    getString(R.string.female)
                } else {
                    getString(R.string.male)
                }

            val matchesSearch =
                post.title.lowercase().contains(searchText)
                        ||
                        post.city.lowercase().contains(searchText)
                        ||
                        genderText.lowercase().contains(searchText)

            val matchesPrice =
                postPrice >= minPrice
                        &&
                        postPrice <= maxPrice

            val matchesGender =
                selectedGenderCode == "all"
                        ||
                        postGenderCode == selectedGenderCode

            val matchesCity =
                selectedCity == getString(R.string.all_cities)
                        ||
                        post.city == selectedCity

            val matchesType =

                (!roommateChecked && !apartmentChecked && !offeringChecked)

                        ||

                        (roommateChecked && post.lookingForRoommate)

                        ||

                        (apartmentChecked && post.lookingForApartment)

                        ||

                        (offeringChecked && post.offeringApartment)

            if (
                matchesSearch
                &&
                matchesPrice
                &&
                matchesGender
                &&
                matchesCity
                &&
                matchesType
            ) {

                filteredList.add(post)
            }
        }

        tvShownPosts.text =
            getString(
                R.string.shown_posts,
                filteredList.size
            )

        adapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}
