package mk.cimerapp.cimermk

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class CreatePostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var citySpinner: AutoCompleteTextView

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                getCurrentCity()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_post)

        firestore = FirebaseFirestore.getInstance()
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        val title =
            findViewById<EditText>(R.id.etTitle)

        citySpinner =
            findViewById(R.id.spCity)

        val price =
            findViewById<EditText>(R.id.etPrice)

        val description =
            findViewById<EditText>(R.id.etDescription)

        val genderSpinner =
            findViewById<AutoCompleteTextView>(R.id.spGender)

        val lookingForRoommate =
            findViewById<CheckBox>(R.id.cbLookingForRoommate)

        val lookingForApartment =
            findViewById<CheckBox>(R.id.cbLookingForApartment)

        val offeringApartment =
            findViewById<CheckBox>(R.id.cbOfferingApartment)

        val saveButton =
            findViewById<Button>(R.id.btnSavePost)

        val locationButton =
            findViewById<Button>(R.id.btnUseCurrentLocation)

        locationButton.setOnClickListener {
            checkLocationPermissionAndGetCity()
        }

        val cities = arrayOf(
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
            cities
        )

        cityAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        citySpinner.setAdapter(cityAdapter)

        citySpinner.setOnClickListener {
            citySpinner.showDropDown()
        }

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

        saveButton.setOnClickListener {

            val currentUser =
                FirebaseAuth.getInstance()
                    .currentUser

            if (currentUser == null) {
                return@setOnClickListener
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

            val genderValue =
                when (genderSpinner.text.toString()) {
                    getString(R.string.male) -> "male"
                    getString(R.string.female) -> "female"
                    else -> ""
                }

            if (currentUser.isAnonymous) {

                savePost(
                    "Anonymous",
                    currentUser.uid,
                    genderValue,
                    title.text.toString(),
                    selectedCity,
                    price.text.toString(),
                    description.text.toString(),
                    lookingForRoommate.isChecked,
                    lookingForApartment.isChecked,
                    offeringApartment.isChecked
                )

            } else {

                firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .addOnSuccessListener { document ->

                        val firstName =
                            document.getString("firstName") ?: ""

                        val lastName =
                            document.getString("lastName") ?: ""

                        val authorName =
                            if (
                                firstName.isNotEmpty()
                                && lastName.isNotEmpty()
                            ) {
                                "$firstName $lastName"
                            } else {
                                "User"
                            }

                        savePost(
                            authorName,
                            currentUser.uid,
                            genderValue,
                            title.text.toString(),
                            selectedCity,
                            price.text.toString(),
                            description.text.toString(),
                            lookingForRoommate.isChecked,
                            lookingForApartment.isChecked,
                            offeringApartment.isChecked
                        )
                    }
            }
        }
    }

    private fun checkLocationPermissionAndGetCity() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentCity()
        } else {
            locationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getCurrentCity() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location == null) {
                    Toast.makeText(
                        this,
                        getString(R.string.turn_on_location),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                val geocoder =
                    Geocoder(this, Locale.getDefault())

                val addresses =
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                val detectedCity =
                    addresses
                        ?.firstOrNull()
                        ?.locality
                        ?: addresses
                            ?.firstOrNull()
                            ?.subAdminArea

                if (!detectedCity.isNullOrEmpty()) {
                    citySpinner.setText(detectedCity, false)

                    Toast.makeText(
                        this,
                        getString(
                            R.string.city_detected,
                            detectedCity
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.could_not_detect_city),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.location_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun savePost(
        authorName: String,
        userId: String,
        genderValue: String,
        title: String,
        city: String,
        price: String,
        description: String,
        lookingForRoommate: Boolean,
        lookingForApartment: Boolean,
        offeringApartment: Boolean
    ) {

        val postData = hashMapOf(

            "title" to title,

            "city" to city,

            "price" to price,

            "description" to description,

            "gender" to genderValue,

            "userId" to userId,

            "authorName" to authorName,

            "anonymousSessionId" to
                    getSharedPreferences(
                        "app_settings",
                        MODE_PRIVATE
                    ).getString(
                        "anonymousSessionId",
                        ""
                    ),

            "createdAt" to System.currentTimeMillis(),

            "lookingForRoommate" to lookingForRoommate,

            "lookingForApartment" to lookingForApartment,

            "offeringApartment" to offeringApartment
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