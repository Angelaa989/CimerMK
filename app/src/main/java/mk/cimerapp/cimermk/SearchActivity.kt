package mk.cimerapp.cimermk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var filteredList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

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

    private fun filterPosts(text: String) {

        filteredList.clear()

        for (post in postList) {

            if (
                post.city.contains(text, true)
                ||
                post.title.contains(text, true)
                ||
                post.price.contains(text, true)
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