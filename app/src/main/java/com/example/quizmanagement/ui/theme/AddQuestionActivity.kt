package com.example.quizmanagement.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.quizmanagement.database.QuizDatabaseHelper
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private var quizId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            quizDatabaseHelper = QuizDatabaseHelper(this)
            quizId = intent.getIntExtra("QUIZ_ID", -1)

            MaterialTheme {
                QuestionForm(onSubmit = { questionText ->
                    if (quizId != null) {
                        // Logique pour ajouter la question associée au quiz
                        quizDatabaseHelper.addQuestion(quizId!!, questionText)
                        Toast.makeText(this, "Question ajoutée avec succès", Toast.LENGTH_SHORT).show()
                        finish() // Fermer l'activité
                    }
                }, onCancel = {
                    // Retour à la ListQuizActivity
                    finish() // Ou startActivity pour une meilleure pratique
                    val intent = Intent(this, ListQuizActivity::class.java)
                    startActivity(intent)
                })
            }
        }
    }

    @Composable
    fun QuestionForm(onSubmit: (String) -> Unit, onCancel: () -> Unit) {
        // Implémentez votre formulaire ici, similaire à celui que vous avez pour le quiz
        Column(modifier = Modifier.padding(16.dp)
        ) {
            // Barre verticale avec le titre "La liste des quiz"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF37474F)) // Couleur similaire au bouton
                        .width(8.dp)
                        .fillMaxHeight()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Ajouter une question",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color(0xFF37474F)
                )
            }

            var questionText by remember { mutableStateOf("") }

            TextField(
                value = questionText,
                onValueChange = { questionText = it },
                label = { Text("Contenu de la question") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onSubmit(questionText) }) {
                Text("Ajouter")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espacement entre les boutons

            // Ajout du bouton "Annuler"
            Button(onClick = { onCancel() }) {
                Text("Annuler")
            }

        }
    }
}
