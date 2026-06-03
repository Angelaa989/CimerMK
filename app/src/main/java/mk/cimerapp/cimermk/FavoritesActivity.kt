package mk.cimerapp.cimermk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        loadFavoritesFromRoom()
    }

    override fun onResume() {
        super.onResume()

        loadFavoritesFromRoom()
    }

    private fun loadFavoritesFromRoom() {

        val database =
            DatabaseProvider.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {

            val currentUserId =
                com.google.firebase.auth.FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid

            if (currentUserId == null) {
                return@launch
            }

            val savedPosts =
                database.savedPostDao()
                    .getPostsForUser(currentUserId)

            val posts =
                savedPosts.map { savedPost ->

                    Post(
                        documentId = savedPost.documentId,
                        title = savedPost.title,
                        city = savedPost.city,
                        price = savedPost.price,
                        gender = savedPost.gender,
                        description = savedPost.description,
                        isFavorite = true
                    )
                }

            withContext(Dispatchers.Main) {

                postList.clear()

                postList.addAll(posts)

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}