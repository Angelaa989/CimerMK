package mk.cimerapp.cimermk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.launch
import android.widget.ImageButton
import java.util.UUID
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics

    private lateinit var callbackManager: CallbackManager

    private val GOOGLE_SIGN_IN_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        loadSavedLanguage()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        analytics = FirebaseAnalytics.getInstance(this)
        callbackManager = CallbackManager.Factory.create()

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)

        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnGoToRegister)

        val googleButton =
            findViewById<ImageButton>(R.id.btnGoogle)

        val facebookButton =
            findViewById<ImageButton>(R.id.btnFacebook)

        val anonymousButton =
            findViewById<ImageButton>(R.id.btnAnonymous)

        val languageButton =
            findViewById<ImageButton>(R.id.btnLanguage)

        val forgotPasswordText =
            findViewById<TextView>(R.id.tvForgotPassword)

        registerButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        anonymousButton.setOnClickListener {

            auth.signInAnonymously()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        analytics.logEvent(
                            "guest_login",
                            null
                        )

                        val anonymousSessionId =
                            UUID.randomUUID().toString()

                        getSharedPreferences(
                            "app_settings",
                            MODE_PRIVATE
                        )
                            .edit()
                            .putString(
                                "anonymousSessionId",
                                anonymousSessionId
                            )
                            .apply()

                        val guestId =
                            auth.currentUser?.uid

                        if (guestId != null) {

                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.IO
                            ).launch {

                                DatabaseProvider.getDatabase(this@LoginActivity)
                                    .savedPostDao()
                                    .deletePostsForUser(guestId)
                            }
                        }

                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            )
                        )

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

        forgotPasswordText.setOnClickListener {

            val userEmail =
                email.text.toString().trim()

            if (userEmail.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.enter_email_first),
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                auth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            Toast.makeText(
                                this,
                                getString(R.string.password_reset_sent),
                                Toast.LENGTH_LONG
                            ).show()

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

        googleButton.setOnClickListener {

            val googleSignInOptions =
                GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                    .requestIdToken(
                        getString(R.string.default_web_client_id)
                    )
                    .requestEmail()
                    .build()

            val googleSignInClient =
                GoogleSignIn.getClient(
                    this,
                    googleSignInOptions
                )

            googleSignInClient.signOut()
                .addOnCompleteListener {

                    startActivityForResult(
                        googleSignInClient.signInIntent,
                        GOOGLE_SIGN_IN_REQUEST_CODE
                    )
                }
        }

        facebookButton.setOnClickListener {

            LoginManager.getInstance()
                .logInWithReadPermissions(
                    this,
                    listOf("email", "public_profile")
                )
        }

        LoginManager.getInstance()
            .registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {

                        val credential =
                            FacebookAuthProvider.getCredential(
                                result.accessToken.token
                            )

                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    analytics.logEvent(
                                        "facebook_login",
                                        null
                                    )

                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            HomeActivity::class.java
                                        )
                                    )

                                    finish()

                                } else {

                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }

                    override fun onCancel() {
                        Toast.makeText(
                            this@LoginActivity,
                            "Facebook login cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(
                            this@LoginActivity,
                            error.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )

        languageButton.setOnClickListener {

            val popupMenu =
                PopupMenu(this, languageButton)

            popupMenu.menu.add("Македонски")
            popupMenu.menu.add("English")

            popupMenu.setOnMenuItemClickListener {

                if (it.title == "Македонски") {

                    saveLanguage("mk")

                } else {

                    saveLanguage("en")
                }

                recreate()

                true
            }

            popupMenu.show()
        }

        loginButton.setOnClickListener {

            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                auth.signInWithEmailAndPassword(
                    userEmail,
                    userPassword
                ).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            getString(R.string.login_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        analytics.logEvent(
                            "email_login",
                            null
                        )

                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            )
                        )

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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account =
                    task.result

                val credential =
                    GoogleAuthProvider.getCredential(
                        account.idToken,
                        null
                    )

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->

                        if (authTask.isSuccessful) {

                            analytics.logEvent(
                                "google_login",
                                null
                            )

                            startActivity(
                                Intent(this, HomeActivity::class.java)
                            )

                            finish()
                        }
                    }

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun saveLanguage(languageCode: String) {

        val prefs =
            getSharedPreferences(
                "app_settings",
                MODE_PRIVATE
            )

        prefs.edit()
            .putString(
                "language",
                languageCode
            )
            .apply()
    }

    private fun loadSavedLanguage() {

        val prefs =
            getSharedPreferences(
                "app_settings",
                MODE_PRIVATE
            )

        val language =
            prefs.getString(
                "language",
                "en"
            )

        val locale =
            Locale(language ?: "en")

        Locale.setDefault(locale)

        val config =
            resources.configuration

        config.setLocale(locale)

        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )
    }
}