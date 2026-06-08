package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.AutoCompleteTextView

class EditPostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_post)

        firestore = FirebaseFirestore.getInstance()

        val title =
            findViewById<EditText>(R.id.etEditTitle)

        val citySpinner =
            findViewById<AutoCompleteTextView>(R.id.spEditCity)

        val price =
            findViewById<EditText>(R.id.etEditPrice)

        val description =
            findViewById<EditText>(R.id.etEditDescription)

        description.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)

            if (
                event.action == android.view.MotionEvent.ACTION_UP
                || event.action == android.view.MotionEvent.ACTION_CANCEL
            ) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }

            false
        }

        val genderSpinner =
            findViewById<AutoCompleteTextView>(R.id.spEditGender)

        val cbLookingForRoommate =
            findViewById<android.widget.CheckBox>(
                R.id.cbEditLookingForRoommate
            )

        val cbLookingForApartment =
            findViewById<android.widget.CheckBox>(
                R.id.cbEditLookingForApartment
            )

        val cbOfferingApartment =
            findViewById<android.widget.CheckBox>(
                R.id.cbEditOfferingApartment
            )

        val updateButton =
            findViewById<Button>(R.id.btnUpdatePost)

        val genderOptions = arrayOf(
            getString(R.string.select_gender),
            getString(R.string.male),
            getString(R.string.female)
        )

        val genderAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderOptions
        )

        genderAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        genderSpinner.setAdapter(genderAdapter)

        genderSpinner.setOnClickListener {
            genderSpinner.showDropDown()
        }

        val cityOptions = arrayOf(
            getString(R.string.select_city),
            "Аеродром",
            "Арачиново",
            "Берово",
            "Битола",
            "Богданци",
            "Боговиње",
            "Босилово",
            "Брвеница",
            "Бутел",
            "Валандово",
            "Василево",
            "Вевчани",
            "Велес",
            "Виница",
            "Вранештица",
            "Врапчиште",
            "Гази Баба",
            "Гевгелија",
            "Гостивар",
            "Градско",
            "Дебар",
            "Дебарца",
            "Делчево",
            "Демир Капија",
            "Демир Хисар",
            "Дојран",
            "Долнени",
            "Другово",
            "Желино",
            "Зајас",
            "Зелениково",
            "Зрновци",
            "Илинден",
            "Јегуновце",
            "Кавадарци",
            "Карбинци",
            "Карпош",
            "Кичево",
            "Кисела Вода",
            "Конче",
            "Кочани",
            "Кратово",
            "Крива Паланка",
            "Кривогаштани",
            "Крушево",
            "Куманово",
            "Липково",
            "Лозово",
            "Маврово и Ростуше",
            "Македонска Каменица",
            "Македонски Брод",
            "Могила",
            "Неготино",
            "Новаци",
            "Ново Село",
            "Осломеј",
            "Охрид",
            "Петровец",
            "Пехчево",
            "Пласница",
            "Прилеп",
            "Пробиштип",
            "Радовиш",
            "Ранковце",
            "Ресен",
            "Росоман",
            "Сарај",
            "Свети Николе",
            "Сопиште",
            "Старо Нагоричане",
            "Струга",
            "Струмица",
            "Студеничани",
            "Теарце",
            "Тетово",
            "Центар",
            "Центар Жупа",
            "Чаир",
            "Чашка",
            "Чешиново и Облешево",
            "Чучер Сандево",
            "Штип",
            "Шуто Оризари",
            "Ѓорче Петров"
        )

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cityOptions
        )

        cityAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        citySpinner.setAdapter(cityAdapter)

        citySpinner.setOnClickListener {
            citySpinner.showDropDown()
        }

        val documentId =
            intent.getStringExtra("documentId")

        title.setText(
            intent.getStringExtra("title")
        )

        price.setText(
            intent.getStringExtra("price")
        )

        description.setText(
            intent.getStringExtra("description")
        )

        val currentGender =
            intent.getStringExtra("gender")

        genderSpinner.setText(
            if (currentGender == "female") {
                getString(R.string.female)
            } else if (currentGender == "male") {
                getString(R.string.male)
            } else {
                ""
            },
            false
        )

        val currentCity =
            intent.getStringExtra("city")

        citySpinner.setText(
            currentCity ?: "",
            false
        )

        cbLookingForRoommate.isChecked =
            intent.getBooleanExtra(
                "lookingForRoommate",
                false
            )

        cbLookingForApartment.isChecked =
            intent.getBooleanExtra(
                "lookingForApartment",
                false
            )

        cbOfferingApartment.isChecked =
            intent.getBooleanExtra(
                "offeringApartment",
                false
            )

        updateButton.setOnClickListener {

            val genderValue =
                when (genderSpinner.text.toString()) {
                    getString(R.string.male) -> "male"
                    getString(R.string.female) -> "female"
                    else -> ""
                }

            val selectedCity =
                if (
                    citySpinner.text.toString()
                    == getString(R.string.select_city)
                ) {
                    ""
                } else {
                    citySpinner.text.toString().trim()
                }

            if (selectedCity.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val updatedPost = hashMapOf<String, Any>(

                "title" to title.text.toString(),

                "city" to selectedCity,

                "price" to price.text.toString(),

                "description" to description
                    .text
                    .toString(),

                "gender" to genderValue,

                "lookingForRoommate" to
                        cbLookingForRoommate.isChecked,

                "lookingForApartment" to
                        cbLookingForApartment.isChecked,

                "offeringApartment" to
                        cbOfferingApartment.isChecked
            )

            if (documentId != null) {

                firestore.collection("posts")
                    .document(documentId)
                    .update(updatedPost)

                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            getString(R.string.post_updated),
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }

                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            getString(R.string.update_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}