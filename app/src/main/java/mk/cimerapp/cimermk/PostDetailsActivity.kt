package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_details)

        val title =
            findViewById<TextView>(R.id.tvTitle)

        val author =
            findViewById<TextView>(R.id.tvAuthor)

        val city =
            findViewById<TextView>(R.id.tvCity)

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

        city.text =
            intent.getStringExtra("city")

        price.text =
            intent.getStringExtra("price") + " €"

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