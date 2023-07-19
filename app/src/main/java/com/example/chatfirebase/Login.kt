package com.example.chatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatfirebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

	lateinit var binding: ActivityLoginBinding
	lateinit var mAuth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.hide()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

		binding.textRegister.setOnClickListener {
			val intent = Intent(this, Signup::class.java)
			finish()
			startActivity(intent)
		}

		//initializing auth
		mAuth = FirebaseAuth.getInstance()

		binding.loginBtn.setOnClickListener {
			val email = binding.email.text.toString()
			val pass = binding.pass.text.toString()

			login(email, pass)
			hideKeybaord(it)
		}
	}

	private fun login(email: String, pass: String) {
		mAuth.signInWithEmailAndPassword(email, pass)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					val intent = Intent(this@Login, MainActivity::class.java)
					startActivity(intent)
				} else {
					Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT)
						.show()
				}
			}
	}
	private fun hideKeybaord(v: View) {
		val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
		inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
	}
}