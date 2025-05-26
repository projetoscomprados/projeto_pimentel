package com.example.projeto_pimentel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputSenha: EditText
    private lateinit var btnCadastrar: Button
    private lateinit var btnEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        inputEmail = findViewById(R.id.emailField)
        inputSenha = findViewById(R.id.passwordField)
        btnCadastrar = findViewById(R.id.signupButton)
        btnEntrar = findViewById(R.id.signinButton)

        btnCadastrar.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val senha = inputSenha.text.toString().trim()
            criarConta(email, senha)
        }

        btnEntrar.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val senha = inputSenha.text.toString().trim()
            autenticar(email, senha)
        }
    }

    private fun criarConta(email: String, senha: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun autenticar(email: String, senha: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, segundatela::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
