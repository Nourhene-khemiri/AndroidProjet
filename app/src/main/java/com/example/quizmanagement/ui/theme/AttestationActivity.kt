package com.example.quizmanagement.ui.theme

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmanagement.R

import android.os.Environment
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AttestationActivity : AppCompatActivity() {

    // Déclaration de la constante PERMISSION_REQUEST_CODE à l'échelle de la classe
    val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        val PERMISSION_REQUEST_CODE = 1

            // Vérifier les permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            // Si la permission est déjà accordée, commencez à télécharger
            val fileUrl = "https://example.com/attestation.pdf"  // Remplacez par l'URL réelle
            downloadAttestation(fileUrl)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.attestation_activity)

        // Récupérer les informations passées via l'Intent
        val userName = intent.getStringExtra("USER_NAME")
        val userLastName = intent.getStringExtra("USER_LAST_NAME")

        // Afficher les informations dans votre activité
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val lastNameTextView = findViewById<TextView>(R.id.lastNameTextView)
        val attestationTextView = findViewById<TextView>(R.id.attestationTextView)
        val celebrationMessageTextView = findViewById<TextView>(R.id.celebrationMessage)

        // Ajouter le texte d'attestation
        nameTextView.text = "Nom: $userName"
        lastNameTextView.text = "Prénom: $userLastName"
        attestationTextView.text = "Attestation de réussite pour le quiz"

        // Ajouter le message de félicitations avec des émojis
        val message = "🌸 Félicitations, vous avez réussi le quiz! 🎉"
        celebrationMessageTextView.text = message

    }

    fun downloadAttestation(url: String) {
        Thread {
            try {
                val urlConnection = URL(url).openConnection()
                val inputStream: InputStream = BufferedInputStream(urlConnection.getInputStream())
                val filePath = Environment.getExternalStorageDirectory().absolutePath + "/Attestation.pdf"
                val outputStream = FileOutputStream(filePath)

                val data = ByteArray(1024)
                var count: Int
                while (inputStream.read(data).also { count = it } != -1) {
                    outputStream.write(data, 0, count)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()

                // Retourner sur le thread principal pour afficher un message
                runOnUiThread {
                    Toast.makeText(this, "Attestation téléchargée avec succès", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si la permission est accordée, commencer à télécharger
                val fileUrl = "https://example.com/attestation.pdf"  // Remplacez par l'URL réelle
                downloadAttestation(fileUrl)
            } else {
                Toast.makeText(this, "Permission refusée. Impossible de télécharger l'attestation.", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
