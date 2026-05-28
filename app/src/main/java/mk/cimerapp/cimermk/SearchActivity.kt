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

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var filteredList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var searchEditText: EditText

    private lateinit var minPriceEditText: EditText

    private lateinit var maxPriceEditText: EditText

    private lateinit var genderSpinner: Spinner

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

        filteredList.clear()

        for (post in postList) {

            val postPrice =
                post.price.toIntOrNull() ?: 0

            val matchesSearch =

                post.title.lowercase().contains(searchText)
                        ||
                        post.city.lowercase().contains(searchText)
                        ||
                        post.gender.lowercase().contains(searchText)

            val matchesPrice =

                postPrice >= minPrice
                        &&
                        postPrice <= maxPrice

            val matchesGender =

                selectedGender == getString(R.string.all)
                        ||
                        post.gender.equals(
                            selectedGender,
                            true
                        )

            if (
                matchesSearch
                &&
                matchesPrice
                &&
                matchesGender
            ) {

                filteredList.add(post)
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}
