package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var tvUserFullName: TextView
    private lateinit var tvUserCity: TextView
    private lateinit var tvUserFaculty: TextView
    private lateinit var tvUserContact: TextView
    private lateinit var recyclerUserPosts: RecyclerView

    private lateinit var tvUserEmail: TextView

    private lateinit var tvUserGender: TextView

    private lateinit var tvUserAge: TextView

    private val userPosts =
        ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)

        tvUserFullName =
            findViewById(R.id.tvUserFullName)

        tvUserCity =
            findViewById(R.id.tvUserCity)

        tvUserFaculty =
            findViewById(R.id.tvUserFaculty)

        tvUserContact =
            findViewById(R.id.tvUserContact)

        tvUserEmail =
            findViewById(R.id.tvUserEmail)

        tvUserGender =
            findViewById(R.id.tvUserGender)

        tvUserAge =
            findViewById(R.id.tvUserAge)

        recyclerUserPosts =
            findViewById(R.id.recyclerUserPosts)

        recyclerUserPosts.layoutManager =
            LinearLayoutManager(this)

        recyclerUserPosts.isNestedScrollingEnabled = false

        recyclerUserPosts.setHasFixedSize(false)

        val userId =
            intent.getStringExtra("userId")
                ?: return

        loadUser(userId)

    }

    private fun loadUser(userId: String) {

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                val firstName =
                    document.getString("firstName") ?: ""

                val lastName =
                    document.getString("lastName") ?: ""

                val fullName =
                    "$firstName $lastName".trim()

                val email =
                    document.getString("email") ?: ""

                val gender =
                    document.getString("gender") ?: ""

                val age =
                    document.getString("age") ?: ""

                val genderText =
                    if (gender == "female") {
                        getString(R.string.female)
                    } else {
                        getString(R.string.male)
                    }

                tvUserEmail.text =
                    "${getString(R.string.email_label)}: $email"

                tvUserGender.text =
                    "${getString(R.string.gender_label)}: $genderText"

                tvUserAge.text =
                    "${getString(R.string.age_label)}: $age"

                tvUserFullName.text =
                    if (fullName.isNotEmpty()) {
                        fullName
                    } else {
                        "User"
                    }

                tvUserCity.text =
                    getString(R.string.city) +
                            ": " +
                            (document.getString("city") ?: "-")

                tvUserFaculty.text =
                    getString(R.string.faculty_label) +
                            ": " +
                            (document.getString("faculty") ?: "-")

                val contactType =
                    document.getString("contactType") ?: ""

                val contactInfo =
                    document.getString("contactInfo") ?: ""

                tvUserContact.text =
                    if (
                        contactType.isNotEmpty()
                        &&
                        contactInfo.isNotEmpty()
                    ) {
                        "$contactType: $contactInfo"
                    } else {
                        getString(R.string.contact_not_added)
                    }

                loadUserPosts(userId)
            }
    }

    private fun loadUserPosts(userId: String) {

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->

                android.util.Log.d(
                    "USER_PROFILE",
                    "Posts found: ${result.size()}"
                )

                userPosts.clear()

                for (document in result) {

                    android.util.Log.d(
                        "USER_PROFILE",
                        "Post: ${document.getString("title")}"
                    )

                    val post =
                        document.toObject(Post::class.java)

                    post.documentId =
                        document.id

                    val authorNameFromPost =
                        document.getString("authorName") ?: ""

                    post.authorName =
                        if (
                            authorNameFromPost.isNotEmpty()
                            &&
                            authorNameFromPost != "User"
                            &&
                            authorNameFromPost != "Anonymous"
                        ) {
                            authorNameFromPost
                        } else {
                            tvUserFullName.text.toString()
                        }

                    userPosts.add(post)
                }

                recyclerUserPosts.adapter =
                    PostAdapter(userPosts)
            }
    }
}