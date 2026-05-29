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

class CreatePostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_post)

        firestore = FirebaseFirestore.getInstance()

        val title =
            findViewById<EditText>(R.id.etTitle)

        val citySpinner =
            findViewById<Spinner>(R.id.spCity)

        val price =
            findViewById<EditText>(R.id.etPrice)

        val description =
            findViewById<EditText>(R.id.etDescription)

        val genderSpinner =
            findViewById<Spinner>(R.id.spGender)

        val saveButton =
            findViewById<Button>(R.id.btnSavePost)

        val cities = arrayOf(
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
            cities
        )

        cityAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        citySpinner.adapter = cityAdapter

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

        saveButton.setOnClickListener {

            val genderValue =
                if (genderSpinner.selectedItemPosition == 0) {
                    "male"
                } else {
                    "female"
                }

            val postData = hashMapOf(

                "title" to title.text.toString(),

                "city" to citySpinner
                    .selectedItem
                    .toString(),

                "price" to price.text.toString(),

                "description" to description.text.toString(),

                "gender" to genderValue,

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
                        getString(R.string.post_saved),
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        getString(R.string.save_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}