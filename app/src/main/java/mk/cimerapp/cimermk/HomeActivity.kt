package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val logoutButton =
            findViewById<Button>(R.id.btnLogout)

        val createPostButton =
            findViewById<Button>(R.id.btnCreatePost)

        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }

        createPostButton.setOnClickListener {

            startActivity(
                Intent(this, CreatePostActivity::class.java)
            )
        }
    }

    private fun loadPosts() {

        firestore.collection("posts")
            .get()
            .addOnSuccessListener { documents ->

                postList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(Post::class.java)

                    postList.add(post)
                }

                adapter.notifyDataSetChanged()
            }
    }
}