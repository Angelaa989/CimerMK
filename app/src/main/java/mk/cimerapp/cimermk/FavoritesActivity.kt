package mk.cimerapp.cimermk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorites)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView =
            findViewById(R.id.recyclerFavorites)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        postList = arrayListOf()

        adapter = PostAdapter(
            postList,
            true
        )

        recyclerView.adapter = adapter

        loadFavorites()
    }

    override fun onResume() {
        super.onResume()

        loadFavorites()
    }

    private fun loadFavorites() {

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("favorite", true)
            .get()
            .addOnSuccessListener { documents ->

                postList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(Post::class.java)

                    post.documentId = document.id

                    postList.add(post)
                }

                adapter.notifyDataSetChanged()
            }
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}