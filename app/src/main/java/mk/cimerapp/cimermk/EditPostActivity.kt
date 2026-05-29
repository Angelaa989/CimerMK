package mk.cimerapp.cimermk

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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

        val citySpinner =
            findViewById<Spinner>(R.id.spEditCity)

        val price =
            findViewById<EditText>(R.id.etEditPrice)

        val description =
            findViewById<EditText>(R.id.etEditDescription)

        val genderSpinner =
            findViewById<Spinner>(R.id.spEditGender)

        val updateButton =
            findViewById<Button>(R.id.btnUpdatePost)

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

        if (currentGender == "female") {

            genderSpinner.setSelection(1)

        } else {

            genderSpinner.setSelection(0)
        }

        val currentCity =
            intent.getStringExtra("city")

        val cityPosition =
            cityOptions.indexOf(currentCity)

        if (cityPosition >= 0) {

            citySpinner.setSelection(cityPosition)
        }

        updateButton.setOnClickListener {

            val genderValue =
                if (genderSpinner.selectedItemPosition == 0) {
                    "male"
                } else {
                    "female"
                }

            val updatedPost = hashMapOf<String, Any>(

                "title" to title.text.toString(),

                "city" to citySpinner
                    .selectedItem
                    .toString(),

                "price" to price.text.toString(),

                "description" to description
                    .text
                    .toString(),

                "gender" to genderValue
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