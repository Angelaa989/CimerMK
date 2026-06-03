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
        val genderSpinner = findViewById<Spinner>(R.id.spEditProfileGender)
        val citySpinner = findViewById<Spinner>(R.id.spEditProfileCity)
        val age = findViewById<EditText>(R.id.etEditAge)
        val faculty = findViewById<EditText>(R.id.etEditFaculty)
        val contactTypeSpinner = findViewById<Spinner>(R.id.spEditContactType)
        val contactInfo = findViewById<EditText>(R.id.etEditContactInfo)
        val saveButton = findViewById<Button>(R.id.btnSaveProfile)

        val genderOptions = arrayOf(
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

        genderSpinner.adapter = genderAdapter

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

        citySpinner.adapter = cityAdapter

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

        contactTypeSpinner.adapter = contactAdapter

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

                    val gender = document.getString("gender") ?: "male"

                    if (gender == "female") {
                        genderSpinner.setSelection(1)
                    } else {
                        genderSpinner.setSelection(0)
                    }

                    val city = document.getString("city") ?: ""

                    val cityPosition =
                        cityOptions.indexOf(city)

                    if (cityPosition >= 0) {
                        citySpinner.setSelection(cityPosition)
                    }

                    val contactType =
                        document.getString("contactType") ?: ""

                    val contactPosition =
                        contactOptions.indexOf(contactType)

                    if (contactPosition >= 0) {
                        contactTypeSpinner.setSelection(contactPosition)
                    }
                }
        }

        saveButton.setOnClickListener {

            val genderValue =
                if (genderSpinner.selectedItemPosition == 0) {
                    "male"
                } else {
                    "female"
                }

            val updatedUser = hashMapOf<String, Any>(
                "firstName" to firstName.text.toString().trim(),
                "lastName" to lastName.text.toString().trim(),
                "gender" to genderValue,
                "city" to citySpinner.selectedItem.toString(),
                "age" to age.text.toString().trim(),
                "faculty" to faculty.text.toString().trim(),
                "contactType" to contactTypeSpinner.selectedItem.toString(),
                "contactInfo" to contactInfo.text.toString().trim()
            )

            if (uid != null) {

                firestore.collection("users")
                    .document(uid)
                    .set(updatedUser)
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