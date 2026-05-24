package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditPostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        firestore = FirebaseFirestore.getInstance()

        val title =
            findViewById<EditText>(R.id.etEditTitle)

        val city =
            findViewById<EditText>(R.id.etEditCity)

        val price =
            findViewById<EditText>(R.id.etEditPrice)

        val description =
            findViewById<EditText>(R.id.etEditDescription)

        val updateButton =
            findViewById<Button>(R.id.btnUpdatePost)

        val documentId =
            intent.getStringExtra("documentId")

        title.setText(
            intent.getStringExtra("title")
        )

        city.setText(
            intent.getStringExtra("city")
        )

        price.setText(
            intent.getStringExtra("price")
        )

        description.setText(
            intent.getStringExtra("description")
        )

        updateButton.setOnClickListener {

            val updatedPost = hashMapOf(

                "title" to title.text.toString(),

                "city" to city.text.toString(),

                "price" to price.text.toString(),

                "description" to description.text.toString()
            )

            firestore.collection("posts")
                .document(documentId!!)
                .update(updatedPost as Map<String, Any>)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Post updated",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
        }
    }
}