package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AutoCompleteTextView

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val firestore = FirebaseFirestore.getInstance()
        val email = findViewById<EditText>(R.id.etRegisterEmail)
        val password = findViewById<EditText>(R.id.etRegisterPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val firstName = findViewById<EditText>(R.id.etFirstName)
        val lastName = findViewById<EditText>(R.id.etLastName)
        val genderSpinner =
            findViewById<AutoCompleteTextView>(R.id.spRegisterGender)

        val citySpinner =
            findViewById<AutoCompleteTextView>(R.id.spRegisterCity)

        val contactTypeSpinner =
            findViewById<AutoCompleteTextView>(R.id.spContactType)
        val age = findViewById<EditText>(R.id.etAge)
        val faculty = findViewById<EditText>(R.id.etFaculty)
        val contactInfo = findViewById<EditText>(R.id.etContactInfo)

        val genderOptions = arrayOf(
            getString(R.string.select_gender),
            getString(R.string.male),
            getString(R.string.female)
        )

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

        val contactOptions = arrayOf(
            getString(R.string.select_contact_type),
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
        citySpinner.setAdapter(cityAdapter)


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

        citySpinner.setOnClickListener {
            citySpinner.showDropDown()
        }

        contactTypeSpinner.setOnClickListener {
            contactTypeSpinner.showDropDown()
        }

        registerButton.setOnClickListener {

            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val userFirstName = firstName.text.toString().trim()
            val userLastName = lastName.text.toString().trim()
            val genderValue =
                when (genderSpinner.text.toString()) {
                    getString(R.string.male) -> "male"
                    getString(R.string.female) -> "female"
                    else -> ""
                }

            val userCity =
                if (
                    citySpinner.text.toString()
                    == getString(R.string.select_city)
                ) {
                    ""
                } else {
                    citySpinner.text.toString().trim()
                }

            val userAge = age.text.toString().trim()
            val userFaculty = faculty.text.toString().trim()

            val userContactType =
                if (
                    contactTypeSpinner.text.toString()
                    == getString(R.string.select_contact_type)
                ) {
                    ""
                } else {
                    contactTypeSpinner.text.toString().trim()
                }

            val userContactInfo =
                contactInfo.text.toString().trim()



            if (
                userFirstName.isEmpty()
                ||
                userLastName.isEmpty()
                ||
                userEmail.isEmpty()
                ||
                userPassword.isEmpty()
                ||
                genderValue.isEmpty()
                ||
                userCity.isEmpty()
                ||
                userContactType.isEmpty()
                ||
                userContactInfo.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                auth.createUserWithEmailAndPassword(
                    userEmail,
                    userPassword
                ).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val uid =
                            auth.currentUser?.uid

                        if (uid != null) {

                            val userData = hashMapOf(

                                "firstName" to userFirstName,

                                "lastName" to userLastName,

                                "email" to userEmail,

                                "gender" to genderValue,

                                "city" to userCity,

                                "age" to userAge,

                                "faculty" to userFaculty,

                                "contactType" to userContactType,

                                "contactInfo" to userContactInfo
                            )

                            firestore.collection("users")
                                .document(uid)
                                .set(userData)
                        }

                        Toast.makeText(
                            this,
                            getString(R.string.registration_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            task.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}