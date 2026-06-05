package mk.cimerapp.cimermk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var tvSavedCount: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorites)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView =
            findViewById(R.id.recyclerFavorites)

        tvSavedCount =
            findViewById(R.id.tvSavedCount)

        recyclerView.layoutManager =
            if (
                resources.configuration.orientation ==
                Configuration.ORIENTATION_LANDSCAPE
                ||
                resources.configuration.smallestScreenWidthDp >= 600
            ) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

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

                tvSavedCount.text =
                    getString(
                        R.string.saved_posts_count,
                        posts.size
                    )

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}