package com.example.projeto_pimentel

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class segundatela : AppCompatActivity() {

    private val httpClient = OkHttpClient()
    private val apiKey = "AIzaSyA7rYx5JaW-otwLu1sEW37yDj-XmQrO0KI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segundatela)

        val btnPedido = findViewById<Button>(R.id.btnGenerateApology)
        val btnPresente = findViewById<Button>(R.id.btnGenerateGift)
        val txtPedido = findViewById<TextView>(R.id.outputApology)
        val txtPresente = findViewById<TextView>(R.id.outputGift)

        btnPedido.setOnClickListener {
            gerarTexto(
                prompt = "Fale-me um pedido de desculpas para uma namorada ou namorado, um pedido que não seja longo e sincero"
            ) { resposta -> runOnUiThread { txtPedido.text = resposta } }
        }

        btnPresente.setOnClickListener {
            gerarTexto(
                prompt = "Fale-me um presente para pedir desculpas para uma namorada ou namorado. Um presente curto e criativo"
            ) { resposta -> runOnUiThread { txtPresente.text = resposta } }
        }
    }

    private fun gerarTexto(prompt: String, callback: (String) -> Unit) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", prompt)
                ))
            ))
        }

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder().url(url).post(body).build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Erro: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val resposta = response.body?.string() ?: "Sem resposta"
                    val texto = JSONObject(resposta)
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    callback(texto)
                } catch (e: Exception) {
                    callback("Erro ao processar resposta: ${e.message}")
                }
            }
        })
    }
}
