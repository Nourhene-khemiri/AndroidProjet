package com.example.quizmanagement.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizmanagement.database.QuizDatabaseHelper
import com.example.quizmanagement.model.Choix

class AddChoixActivity : AppCompatActivity() {
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private var questionId: Int = -1  // Changer le type à Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        questionId = intent.getIntExtra("QUESTION_ID", -1) // Récupérer l'ID en tant qu'Int
        if (questionId == -1) {
            Toast.makeText(this, "Erreur de chargement de la question", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        quizDatabaseHelper = QuizDatabaseHelper(this)

        setContent {
            MaterialTheme {
                AddChoixScreen(questionId)
            }
        }
    }

    @Composable
    fun AddChoixScreen(questionId: Int) {  // Utiliser Int au lieu de Long
        var choixText by remember { mutableStateOf("") }
        var isCorrect by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ajouter un Choix", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = choixText,
                onValueChange = { choixText = it },
                label = { Text("Texte du choix") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Checkbox(
                    checked = isCorrect,
                    onCheckedChange = { isCorrect = it }
                )
                Text("Correct")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                addChoix(questionId, choixText, isCorrect)
            }) {
                Text("Ajouter Choix")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espacement entre les boutons

            // Ajout du bouton "Annuler"
            Button(onClick = {
                // Retour à DisplayQuestionActivity
                finish() // Fermer l'activité actuelle
                val intent = Intent(this@AddChoixActivity, DisplayQuestionsActivity::class.java)
                intent.putExtra("QUESTION_ID", questionId) // Optionnel : passer l'ID de la question
                startActivity(intent)
            }) {
                Text("Annuler")
            }
        }
    }

    private fun addChoix(questionId: Int, texte: String, isCorrect: Boolean) { // Utiliser Int au lieu de Long
        val newChoix = Choix(0, questionId, texte, isCorrect)
        quizDatabaseHelper.addChoix(newChoix)
        Toast.makeText(this, "Choix ajouté", Toast.LENGTH_SHORT).show()
        finish()
    }
}
