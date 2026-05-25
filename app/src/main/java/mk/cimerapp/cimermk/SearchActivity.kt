package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var filteredList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        firestore = FirebaseFirestore.getInstance()

        recyclerView =
            findViewById(R.id.recyclerSearch)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        postList = arrayListOf()

        filteredList = arrayListOf()

        adapter = PostAdapter(filteredList)

        recyclerView.adapter = adapter

        loadPosts()

        val searchEditText =
            findViewById<EditText>(R.id.etSearch)

        searchEditText.addTextChangedListener(
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

                    filterPosts(s.toString())
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        val bottomNavigation =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNavigation.selectedItemId =
            R.id.nav_search

        bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(
                        Intent(
                            this,
                            HomeActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_add -> {

                    startActivity(
                        Intent(
                            this,
                            CreatePostActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            ProfileActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_favorites -> {

                    startActivity(
                        Intent(
                            this,
                            FavoritesActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_search -> {

                    true
                }

                else -> false
            }
        }
    }

    private fun loadPosts() {

        firestore.collection("posts")
            .get()
            .addOnSuccessListener { documents ->

                postList.clear()

                filteredList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(Post::class.java)

                    post.documentId =
                        document.id

                    postList.add(post)

                    filteredList.add(post)
                }

                adapter.notifyDataSetChanged()
            }
    }

    private fun filterPosts(text: String) {

        filteredList.clear()

        for (post in postList) {

            if (
                post.city.lowercase()
                    .contains(text.lowercase())
            ) {

                filteredList.add(post)
            }
        }

        adapter.notifyDataSetChanged()
    }
}