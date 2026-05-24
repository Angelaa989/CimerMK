package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var postList: ArrayList<Post>

    private lateinit var adapter: PostAdapter

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firestore = FirebaseFirestore.getInstance()

        recyclerView =
            findViewById(R.id.recyclerPosts)

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

                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadPosts()
    }

    private fun loadPosts() {

        firestore.collection("posts")
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
}