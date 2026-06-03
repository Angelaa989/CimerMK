package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        super.onCreate(savedInstanceState)

        com.google.firebase.messaging.FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                val token = task.result

                android.util.Log.d(
                    "FCM_TOKEN",
                    token
                )
            }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}