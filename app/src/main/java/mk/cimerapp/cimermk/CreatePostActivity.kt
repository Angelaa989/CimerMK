package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreatePostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        firestore = FirebaseFirestore.getInstance()

        val title = findViewById<EditText>(R.id.etTitle)
        val city = findViewById<EditText>(R.id.etCity)
        val price = findViewById<EditText>(R.id.etPrice)
        val description = findViewById<EditText>(R.id.etDescription)

        val saveButton =
            findViewById<Button>(R.id.btnSavePost)

        saveButton.setOnClickListener {

            val postData = hashMapOf(

                "title" to title.text.toString(),
                "city" to city.text.toString(),
                "price" to price.text.toString(),
                "description" to description.text.toString(),
                "userId" to FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid
            )

            firestore.collection("posts")
                .add(postData)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Post saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Error saving post",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}