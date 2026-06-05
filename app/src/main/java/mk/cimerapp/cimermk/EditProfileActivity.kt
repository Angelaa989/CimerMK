package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.AutoCompleteTextView

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val firstName = findViewById<EditText>(R.id.etEditFirstName)
        val lastName = findViewById<EditText>(R.id.etEditLastName)
        val genderSpinner = findViewById<AutoCompleteTextView>(R.id.spEditProfileGender)
        val citySpinner = findViewById<AutoCompleteTextView>(R.id.spEditProfileCity)
        val age = findViewById<EditText>(R.id.etEditAge)
        val faculty = findViewById<EditText>(R.id.etEditFaculty)
        val contactTypeSpinner =  findViewById<AutoCompleteTextView>(R.id.spEditContactType)
        val contactInfo = findViewById<EditText>(R.id.etEditContactInfo)
        val saveButton = findViewById<Button>(R.id.btnSaveProfile)
        val email = findViewById<EditText>(R.id.etEditEmail)

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

        val contactOptions = arrayOf(
            getString(R.string.phone),
            getString(R.string.instagram),
            getString(R.string.facebook),
            getString(R.string.viber)
        )

        val contactAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            contactOptions
        )

        contactAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        contactTypeSpinner.setAdapter(contactAdapter)

        contactTypeSpinner.setOnClickListener {
            contactTypeSpinner.showDropDown()
        }

        val uid = auth.currentUser?.uid

        if (uid != null) {

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    firstName.setText(document.getString("firstName") ?: "")
                    lastName.setText(document.getString("lastName") ?: "")
                    age.setText(document.getString("age") ?: "")
                    faculty.setText(document.getString("faculty") ?: "")
                    contactInfo.setText(document.getString("contactInfo") ?: "")
                    email.setText(document.getString("email") ?: "")

                    val gender =
                        document.getString("gender") ?: ""

                    genderSpinner.setText(
                        if (gender == "female") {
                            getString(R.string.female)
                        } else if (gender == "male") {
                            getString(R.string.male)
                        } else {
                            ""
                        },
                        false
                    )

                    citySpinner.setText(
                        document.getString("city") ?: "",
                        false
                    )

                    contactTypeSpinner.setText(
                        document.getString("contactType") ?: "",
                        false
                    )
                }
        }

        saveButton.setOnClickListener {

            val genderValue =
                when (genderSpinner.text.toString()) {
                    getString(R.string.male) -> "male"
                    getString(R.string.female) -> "female"
                    else -> ""
                }

            val updatedUser = hashMapOf<String, Any>(
                "firstName" to firstName.text.toString().trim(),
                "lastName" to lastName.text.toString().trim(),
                "gender" to genderValue,
                "city" to citySpinner.text.toString().trim(),
                "age" to age.text.toString().trim(),
                "faculty" to faculty.text.toString().trim(),
                "contactType" to contactTypeSpinner.text.toString().trim(),
                "contactInfo" to contactInfo.text.toString().trim(),
                "email" to email.text.toString().trim()
            )

            if (uid != null) {

                firestore.collection("users")
                    .document(uid)
                    .update(updatedUser)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            getString(R.string.changes_saved),
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