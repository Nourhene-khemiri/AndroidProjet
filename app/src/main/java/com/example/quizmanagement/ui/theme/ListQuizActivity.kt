package com.example.quizmanagement.ui.theme

import Quiz
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.quizmanagement.R
import com.example.quizmanagement.database.QuizDatabaseHelper
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight


class ListQuizActivity : AppCompatActivity() {

    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private lateinit var quizList: List<Quiz>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quiz)

        quizDatabaseHelper = QuizDatabaseHelper(this)

        // Obtenir tous les quiz
        quizList = quizDatabaseHelper.getAllQuizzes()

        // Définir le contenu avec Compose
        setContent {
            MaterialTheme {
                QuizListScreen(
                    quizList = quizList,
                    onQuizUpdated = { updatedQuiz ->
                        updateQuiz(updatedQuiz) // Mettez à jour le quiz dans la base de données
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }

    private fun navigateToAddQuestion(quiz: Quiz) {
        val intent = Intent(this, AddQuestionActivity::class.java)
        intent.putExtra("QUIZ_ID", quiz.id) // Passez l'ID du quiz
        startActivity(intent)
    }

    // ListQuizActivity.kt
    private fun navigateToDisplayQuestions(quiz: Quiz) {
        val intent = Intent(this, DisplayQuestionsActivity::class.java)
        intent.putExtra("QUIZ_ID", quiz.id)
        startActivity(intent)
    }

    private fun updateQuiz(updatedQuiz: Quiz) {
        val updatedRows = quizDatabaseHelper.updateQuiz(updatedQuiz) // Assurez-vous d'avoir une méthode pour mettre à jour le quiz

        if (updatedRows > 0) {
            Toast.makeText(this, "Quiz mis à jour avec succès", Toast.LENGTH_SHORT).show()
            // Mettre à jour la liste des quiz après la mise à jour
            quizList = quizDatabaseHelper.getAllQuizzes() // Récupérer la liste mise à jour
            setContent {
                MaterialTheme {
                    QuizListScreen(
                        quizList = quizList,
                        onQuizUpdated = { updatedQuiz -> updateQuiz(updatedQuiz) },
                        onBackClick = { finish() }
                    )
                }
            }
        } else {
            Toast.makeText(this, "Échec de la mise à jour du quiz", Toast.LENGTH_SHORT).show()
        }
    }



    // Ajouter la méthode de suppression et la mise à jour de la liste
    private fun deleteQuiz(quiz: Quiz) {
        val deletedRows = quizDatabaseHelper.deleteQuiz(quiz.id) // Assurez-vous que `quiz.id` existe

        if (deletedRows > 0) {
            Toast.makeText(this, "Quiz supprimé avec succès", Toast.LENGTH_SHORT).show()
            // Mettre à jour la liste des quiz après la suppression
            quizList = quizDatabaseHelper.getAllQuizzes() // Récupérer la liste mise à jour
            setContent {
                MaterialTheme {
                    QuizListScreen(
                        quizList = quizList,
                        onQuizUpdated = { updatedQuiz -> updateQuiz(updatedQuiz) },
                        onBackClick = { finish() }
                    )
                }
            }
        } else {
            Toast.makeText(this, "Échec de la suppression du quiz", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun EditQuizDialog(
        quiz: Quiz,
        onDismiss: () -> Unit,
        onSave: (Quiz) -> Unit
    ) {
        var quizTitle by remember { mutableStateOf(quiz.title) }
        var quizDescription by remember { mutableStateOf(quiz.description) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Modifier le Quiz") },
            text = {
                Column {
                    TextField(
                        value = quizTitle,
                        onValueChange = { quizTitle = it },
                        label = { Text("Titre du Quiz") }
                    )
                    TextField(
                        value = quizDescription,
                        onValueChange = { quizDescription = it },
                        label = { Text("Description du Quiz") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val updatedQuiz = quiz.copy(title = quizTitle, description = quizDescription)
                    onSave(updatedQuiz)
                    onDismiss()
                }) {
                    Text("Sauvegarder")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Annuler")
                }
            }
        )
    }


    @Composable
    fun QuizItem(quiz: Quiz, onQuizUpdated: (Quiz) -> Unit) {
        var showDialog by remember { mutableStateOf(false) }

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
                }

                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Modifier")
                }

                IconButton(onClick = { deleteQuiz(quiz) }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Supprimer")
                }

                IconButton(onClick = { navigateToAddQuestion(quiz) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Ajouter une question")
                }

                IconButton(onClick = { navigateToDisplayQuestions(quiz) }) {
                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Afficher les questions")
                }
            }
        }

        // Affichez le dialogue de modification
        if (showDialog) {
            EditQuizDialog(
                quiz = quiz,
                onDismiss = { showDialog = false },
                onSave = { updatedQuiz ->
                    onQuizUpdated(updatedQuiz) // Gérer la mise à jour du quiz ici
                    showDialog = false
                }
            )
        }
    }


    // Le nouveau composable QuizListScreen
    @Composable
    fun QuizListScreen(quizList: List<Quiz>, onQuizUpdated: (Quiz) -> Unit, onBackClick: () -> Unit) {
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

            // Votre contenu pour afficher la liste des quiz
            quizList.forEach { quiz ->
                QuizItem(quiz = quiz, onQuizUpdated = onQuizUpdated)
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


}
