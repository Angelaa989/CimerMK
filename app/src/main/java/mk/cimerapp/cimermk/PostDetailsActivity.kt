package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_details)

        val title =
            findViewById<TextView>(R.id.tvTitle)

        val author =
            findViewById<TextView>(R.id.tvAuthor)

        val createdAt =
            findViewById<TextView>(R.id.tvCreatedAt)

        val authorLayout =
            findViewById<android.view.View>(R.id.layoutAuthor)

        val userId =
            intent.getStringExtra("userId") ?: ""

        val city =
            findViewById<TextView>(R.id.tvCity)

        val mapButton =
            findViewById<android.view.View>(R.id.layoutOpenMap)

        val price =
            findViewById<TextView>(R.id.tvPrice)

        val gender =
            findViewById<TextView>(R.id.tvGender)

        val description =
            findViewById<TextView>(R.id.tvDescription)

        val postTypes =
            findViewById<TextView>(R.id.tvPostTypes)

        title.text =
            intent.getStringExtra("title")

        author.text =
            intent.getStringExtra("authorName")

        val createdAtValue =
            intent.getLongExtra("createdAt", 0L)

        createdAt.text =
            if (createdAtValue > 0) {
                SimpleDateFormat(
                    "dd.MM.yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date(createdAtValue))
            } else {
                ""
            }

        authorLayout.setOnClickListener {

            if (userId.isNotEmpty()) {

                val intent =
                    Intent(
                        this,
                        UserProfileActivity::class.java
                    )

                intent.putExtra(
                    "userId",
                    userId
                )

                startActivity(intent)
            }
        }

        author.setOnClickListener {

            if (userId.isNotEmpty()) {

                val intent =
                    Intent(
                        this,
                        UserProfileActivity::class.java
                    )

                intent.putExtra(
                    "userId",
                    userId
                )

                startActivity(intent)
            }
        }

        city.text =
            intent.getStringExtra("city")

        val cityName =
            intent.getStringExtra("city") ?: ""

        mapButton.setOnClickListener {

            val uri =
                Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=$cityName, North Macedonia"
                )

            val mapIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    uri
                )

            startActivity(mapIntent)
        }

        val priceValue =
            intent.getStringExtra("price") ?: ""

        if (priceValue.isNotBlank()) {

            price.text =
                "$priceValue €"

        } else {

            price.visibility =
                android.view.View.GONE
        }

        val genderValue =
            intent.getStringExtra("gender")

        val genderText =
            if (genderValue == "female") {
                getString(R.string.female)
            } else {
                getString(R.string.male)
            }

        gender.text =
            getString(R.string.gender) + ": " + genderText

        description.text =
            intent.getStringExtra("description")

        val types =
            mutableListOf<String>()

        if (intent.getBooleanExtra("lookingForRoommate", false)) {
            types.add(getString(R.string.looking_for_roommate))
        }

        if (intent.getBooleanExtra("lookingForApartment", false)) {
            types.add(getString(R.string.looking_for_apartment))
        }

        if (intent.getBooleanExtra("offeringApartment", false)) {
            types.add(getString(R.string.offering_apartment))
        }

        postTypes.text =
            "• " + types.joinToString(" • ")

        postTypes.visibility =
            if (types.isEmpty()) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }
    }
}