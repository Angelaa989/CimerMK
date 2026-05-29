package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_language)

        val btnMacedonian =
            findViewById<Button>(R.id.btnMacedonian)

        val btnEnglish =
            findViewById<Button>(R.id.btnEnglish)

        btnMacedonian.setOnClickListener {

            saveLanguage("mk")

            openLogin()
        }

        btnEnglish.setOnClickListener {

            saveLanguage("en")

            openLogin()
        }
    }

    private fun saveLanguage(languageCode: String) {

        val sharedPreferences =
            getSharedPreferences(
                "app_settings",
                MODE_PRIVATE
            )

        sharedPreferences.edit()
            .putString("language", languageCode)
            .apply()
    }

    private fun openLogin() {

        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )

        finish()
    }
}