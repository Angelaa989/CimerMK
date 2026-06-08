package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvFullName: TextView

    private lateinit var tvAvatar: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvFaculty: TextView
    private lateinit var tvContact: TextView

    private lateinit var recyclerMyPosts: RecyclerView

    private lateinit var myPostsList: ArrayList<Post>

    private lateinit var myPostsAdapter: PostAdapter

    private lateinit var tvMyPostsCount: TextView

    private lateinit var tvSavedPostsCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        tvFullName =
            findViewById(R.id.tvFullName)

        tvAvatar =
            findViewById(R.id.tvAvatar)

        tvEmail =
            findViewById<TextView>(R.id.tvEmail)

        tvGender =
            findViewById<TextView>(R.id.tvGender)

        tvCity =
            findViewById<TextView>(R.id.tvCity)

        tvAge =
            findViewById<TextView>(R.id.tvAge)

        tvFaculty =
            findViewById<TextView>(R.id.tvFaculty)

        tvContact =
            findViewById<TextView>(R.id.tvContact)

        tvMyPostsCount =
            findViewById(R.id.tvMyPostsCount)

        tvSavedPostsCount =
            findViewById(R.id.tvSavedPostsCount)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadProfileData()

        val logoutButton =
            findViewById<Button>(R.id.btnLogout)

        val editProfileButton =
            findViewById<Button>(R.id.btnEditProfile)

        recyclerMyPosts =
            findViewById(R.id.recyclerMyPosts)

        recyclerMyPosts.layoutManager =
            LinearLayoutManager(this)

        recyclerMyPosts.isNestedScrollingEnabled = false
        recyclerMyPosts.setHasFixedSize(false)

        myPostsList =
            arrayListOf()

        myPostsAdapter =
            PostAdapter(myPostsList)

        recyclerMyPosts.adapter =
            myPostsAdapter


        logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }

        editProfileButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EditProfileActivity::class.java
                )
            )
        }

    }

    private fun loadProfileData() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid

        if (uid != null) {

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    val firstName =
                        document.getString("firstName") ?: ""

                    val lastName =
                        document.getString("lastName") ?: ""

                    val email =
                        document.getString("email") ?: ""

                    val gender =
                        document.getString("gender") ?: ""

                    val city =
                        document.getString("city") ?: ""

                    val age =
                        document.getString("age") ?: ""

                    val faculty =
                        document.getString("faculty") ?: ""

                    val contactType =
                        document.getString("contactType") ?: ""

                    val contactInfo =
                        document.getString("contactInfo") ?: ""

                    tvFullName.text =
                        "$firstName $lastName"

                    tvAvatar.text =
                        getInitials(firstName, lastName)

                    val genderText =
                        if (gender == "female") {
                            getString(R.string.female)
                        } else {
                            getString(R.string.male)
                        }

                    tvGender.text =
                        "${getString(R.string.gender_label)}: $genderText"

                    tvEmail.text =
                        "${getString(R.string.email_label)}: $email"

                    tvCity.text =
                        "${getString(R.string.city_label)}: $city"

                    tvAge.text =
                        "${getString(R.string.age_label)}: $age"

                    tvFaculty.text =
                        "${getString(R.string.faculty_label)}: $faculty"

                    tvContact.text =
                        if (contactType.isNotEmpty() && contactInfo.isNotEmpty()) {
                            "$contactType: $contactInfo"
                        } else {
                            getString(R.string.contact_not_added)
                        }
                }
        }
    }

    private fun loadMyPosts() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo(
                "userId",
                uid
            )
            .get()
            .addOnSuccessListener { documents ->

                myPostsList.clear()

                for (document in documents) {

                    val post =
                        document.toObject(
                            Post::class.java
                        )

                    post.documentId =
                        document.id

                    myPostsList.add(post)
                }

                myPostsAdapter.notifyDataSetChanged()
            }
    }

    private fun loadStatistics() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { documents ->

                tvMyPostsCount.text =
                    getString(
                        R.string.my_posts_count,
                        documents.size()
                    )
            }

        kotlinx.coroutines.CoroutineScope(
            kotlinx.coroutines.Dispatchers.IO
        ).launch {

            val savedCount =
                DatabaseProvider
                    .getDatabase(this@ProfileActivity)
                    .savedPostDao()
                    .getPostsForUser(uid)
                    .size

            runOnUiThread {

                tvSavedPostsCount.text =
                    getString(
                        R.string.saved_posts_count,
                        savedCount
                    )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadProfileData()

        loadMyPosts()

        loadStatistics()
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }

    private fun getInitials(
        firstName: String,
        lastName: String
    ): String {

        val first =
            firstName.trim()
                .firstOrNull()
                ?.uppercaseChar()
                ?.toString() ?: ""

        val last =
            lastName.trim()
                .firstOrNull()
                ?.uppercaseChar()
                ?.toString() ?: ""

        return if (
            first.isNotEmpty()
            || last.isNotEmpty()
        ) {
            first + last
        } else {
            "AN"
        }
    }
}