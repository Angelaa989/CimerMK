package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.TextView

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var tvTotalPosts: TextView

    private lateinit var postList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firestore = FirebaseFirestore.getInstance()

        recyclerView =
            findViewById(R.id.recyclerPosts)

        tvTotalPosts =
            findViewById(R.id.tvTotalPosts)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        postList = arrayListOf()

        adapter = PostAdapter(postList)

        recyclerView.adapter = adapter

        loadPosts()

        val bottomNavigation =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

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

                    startActivity(
                        Intent(
                            this,
                            SearchActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadPosts()

        val bottomNavigation =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNavigation.selectedItemId =
            R.id.nav_home
    }

    private fun loadPosts() {

        firestore.collection("posts")
            .orderBy(
                "createdAt",
                com.google.firebase.firestore.Query.Direction.DESCENDING
            )
            .get()
            .addOnSuccessListener { documents ->

                postList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(Post::class.java)

                    post.documentId = document.id

                    post.isFavorite = false

                    postList.add(post)
                }

                val currentUserId =
                    FirebaseAuth.getInstance()
                        .currentUser
                        ?.uid

                if (currentUserId != null) {

                    val database =
                        DatabaseProvider.getDatabase(this)

                    CoroutineScope(Dispatchers.IO).launch {

                        val postsSnapshot =
                            postList.toList()

                        for (post in postsSnapshot) {

                            post.isFavorite =
                                database.savedPostDao()
                                    .isPostSaved(
                                        post.documentId,
                                        currentUserId
                                    )
                        }

                        withContext(Dispatchers.Main) {

                            tvTotalPosts.text =
                                getString(
                                    R.string.total_posts,
                                    postList.size
                                )

                            adapter.notifyDataSetChanged()
                        }
                    }

                } else {

                    adapter.notifyDataSetChanged()
                }
            }
    }
}