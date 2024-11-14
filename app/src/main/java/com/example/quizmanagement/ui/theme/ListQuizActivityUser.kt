package com.example.quizmanagement.ui.theme

import Quiz
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmanagement.R
import com.example.quizmanagement.database.QuizDatabaseHelper

class ListQuizActivityUser : AppCompatActivity() {

    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private lateinit var quizList: List<Quiz>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quiz_user)

        // Récupérer l'ID du quiz depuis l'intent
        val quizId = intent.getIntExtra("QUIZ_ID", -1) // -1 comme valeur par défaut si pas trouvé

        quizDatabaseHelper = QuizDatabaseHelper(this)

        // Obtenir tous les quiz
        quizList = quizDatabaseHelper.getAllQuizzes()

        // Définir le contenu avec Compose
        setContent {
            MaterialTheme {
                // Remplace DisplayQuizList par QuizListScreen
                QuizListScreen(quizList = quizList, onBackClick = { finish() })
            }
        }
    }

    // Fonction pour gérer le clic sur "Attestation"
    private fun onAttestationClick(quiz: Quiz) {
        // Exemple statique d'utilisateur, vous pouvez adapter en fonction de l'utilisateur connecté
        val userName = "Ghazoua" // Nom de l'utilisateur
        val userLastName = "Saidani" // Prénom de l'utilisateur

        // Démarrer l'activité AttestationActivity
        val intent = Intent(this, AttestationActivity::class.java).apply {
            putExtra("USER_NAME", userName)
            putExtra("USER_LAST_NAME", userLastName)
        }
        startActivity(intent)
    }


    @Composable
    fun QuizItem(quiz: Quiz, userId: Int, onPassClick: (Quiz) -> Unit) {

        // Récupérer toutes les questions du quiz pour compter le total des questions
        val questions = quizDatabaseHelper.getQuestionsForQuiz(quiz.id)
        val totalQuestions = questions.size // Le nombre total de questions

        val score = quizDatabaseHelper.getScoreForUserAndQuiz(userId, quiz.id)

        // Calculer le pourcentage de bonnes réponses
        val correctAnswers = score ?: 0 // Si le score est null, on met 0 comme score
        val percentage = if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions) * 100 else 0f

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Titre :",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                    Text(
                        text = quiz.title,
                        fontSize = 20.sp,
                        color = Color(0xFF37474F),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Description :",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF607D8B)
                    )
                    Text(
                        text = quiz.description,
                        fontSize = 14.sp,
                        color = Color(0xFF607D8B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display the score if available
                    Text(
                        text = "Score: ${score ?: "Non évalué"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF607D8B)
                    )
                }
                // Vérification du score pour afficher ou cacher les boutons
                when {
                    score == null -> { // Si non évalué
                        Button(
                            onClick = { onPassClick(quiz) },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(text = "Passer")
                        }
                    }
                    percentage >= 50 -> { // Si le score est suffisant (par exemple 50%)
                        Button(
                            onClick = { onAttestationClick(quiz) },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(text = "Attestation")
                        }
                    }
                }
            }
        }
    }


    // Le nouveau composable QuizListScreen
    @Composable
    fun QuizListScreen(quizList: List<Quiz>, onBackClick: () -> Unit) {

        val userId = 1 // L'ID de l'utilisateur (exemple statique pour l'instant)

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
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
                    text = "La liste des quiz",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color(0xFF37474F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))



            // Liste des quiz
            quizList.forEach { quiz ->
                // Passer l'ID utilisateur ici
                QuizItem(quiz = quiz, userId = userId) { selectedQuiz ->
                    val intent = Intent(this@ListQuizActivityUser, QuizQuestionsActivity::class.java).apply {
                        putExtra("QUIZ_ID", selectedQuiz.id) // Assurez-vous que votre modèle Quiz a un ID
                        putExtra("USER_ID", userId) // Ajouter l'ID utilisateur à l'intent
                    }
                    startActivity(intent)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton Retour
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Retour", color = Color.White)
            }
        }
    }

    private fun displayScore(quizId: Int) {
        val score = quizDatabaseHelper.getScoreForUserAndQuiz(1, quizId) // Implémente cette méthode pour récupérer le score
        Toast.makeText(this, "Votre score est : $score", Toast.LENGTH_SHORT).show()
    }







}
