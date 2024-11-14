package com.example.quizmanagement.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmanagement.database.QuizDatabaseHelper
import com.example.quizmanagement.model.Choix
import com.example.quizmanagement.model.Question

class DisplayQuestionsActivity : AppCompatActivity() {
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private var questionList by mutableStateOf(listOf<Question>())
    private var selectedQuestion: Question? = null
    private var selectedChoice: Choix? = null // Ajoutez cette ligne
    private var showEditDialog by mutableStateOf(false)
    private var quizId: Int = -1
    private var expandedQuestionId by mutableStateOf(-1) // ID de la question sélectionnée


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizId = intent.getIntExtra("QUIZ_ID", -1)
        if (quizId == -1) {
            Toast.makeText(this, "Erreur de chargement des questions", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        quizDatabaseHelper = QuizDatabaseHelper(this)
        questionList = quizDatabaseHelper.getQuestionsByQuizId(quizId)

        setContent {
            MaterialTheme {
                QuestionListScreen(
                    questionList = questionList,
                    onBackClick = { finish() },
                    onEditQuestion = { openEditQuestionDialog(it) },
                    onDeleteQuestion = { deleteQuestion(it) },
                    onToggleChoices = { questionId -> toggleChoicesDisplay(questionId) },
                    quizDatabaseHelper = quizDatabaseHelper // Passe l'instance ici
                )

                if (showEditDialog && selectedQuestion != null) {
                    EditQuestionDialog(
                        question = selectedQuestion!!,
                        onDismiss = { showEditDialog = false },
                        onSave = { newText ->
                            updateQuestion(selectedQuestion!!, newText)
                            showEditDialog = false
                        }
                    )
                }
            }
        }
    }

    private fun toggleChoicesDisplay(questionId: Int) {
        expandedQuestionId = if (expandedQuestionId == questionId) -1 else questionId
    }

    private fun openEditQuestionDialog(question: Question) {
        selectedQuestion = question
        showEditDialog = true
    }

    private fun updateQuestion(question: Question, newText: String) {
        question.question_text = newText
        quizDatabaseHelper.updateQuestion(question)
        questionList = quizDatabaseHelper.getQuestionsByQuizId(quizId)
    }

    private fun deleteQuestion(question: Question) {
        quizDatabaseHelper.deleteQuestion(question.id)
        questionList = quizDatabaseHelper.getQuestionsByQuizId(quizId)
    }

    private fun navigateToAddChoix(question: Question) {
        val intent = Intent(this, AddChoixActivity::class.java).apply {
            putExtra("QUESTION_ID", question.id) // Passez l'ID de la question
        }
        startActivity(intent)
    }


    @Composable
    fun EditQuestionDialog(
        question: Question,
        onDismiss: () -> Unit,
        onSave: (String) -> Unit
    ) {
        var newText by remember { mutableStateOf(question.question_text) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Modifier la question") },
            text = {
                TextField(
                    value = newText,
                    onValueChange = { newText = it },
                    label = { Text("Nouvelle question") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onSave(newText)
                }) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Annuler")
                }
            }
        )
    }



    @Composable
    fun QuestionItem(
        question: Question,
        onEditClick: (Question) -> Unit,
        onDeleteClick: (Question) -> Unit,
        onToggleChoices: (Int) -> Unit,
        isExpanded: Boolean,
        quizDatabaseHelper: QuizDatabaseHelper // Ajoute ce paramètre
    ) {
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
                        text = "Question :",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                    Text(
                        text = question.question_text,
                        fontSize = 18.sp,
                        color = Color(0xFF37474F),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Icône de modification
                IconButton(onClick = { onEditClick(question) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Modifier la question",
                        tint = Color(0xFF37474F)
                    )
                }

                // Icône de suppression
                IconButton(onClick = { onDeleteClick(question) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Supprimer la question",
                        tint = Color(0xFF37474F)
                    )
                }

                IconButton(onClick = { navigateToAddChoix(question) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Ajouter un choix")
                }

                // Icone pour afficher/masquer les choix
                IconButton(onClick = { onToggleChoices(question.id) }) {
                    Icon(
                        imageVector = Icons.Filled.List, // Icône d'affichage (peut être changée selon le design)
                        contentDescription = "Afficher les choix",
                        tint = Color(0xFF37474F)
                    )
                }

            }
            // Affiche la liste des choix si la question est sélectionnée
            if (isExpanded) {
                ChoiceList(
                    questionId = question.id,
                    quizDatabaseHelper = quizDatabaseHelper,
                    onEditChoice = { choice -> openEditChoiceDialog(choice) },
                    onDeleteChoice = { choice -> deleteChoice(choice) }
                    )
            }
        }
    }

    @Composable
    fun ChoiceList(
        questionId: Int,
        quizDatabaseHelper: QuizDatabaseHelper,
        onEditChoice: (Choix) -> Unit,
        onDeleteChoice: (Choix) -> Unit
    ) {
        // Utilisation de `remember` avec `mutableStateOf` pour conserver la liste des choix
        val choices = remember { quizDatabaseHelper.getChoicesByQuestionId(questionId) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(choices) { choice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icône de modification
                        IconButton(onClick = { onEditChoice(choice) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Modifier le choix",
                                tint = Color(0xFF37474F)
                            )
                        }

                        // Icône de suppression
                        IconButton(onClick = { onDeleteChoice(choice) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Supprimer le choix",
                                tint = Color(0xFF37474F)
                            )
                        }

                        Text(
                            text = choice.choix_text,
                            fontSize = 16.sp,
                            color = Color(0xFF37474F)
                        )

                        // Ajout d'un Spacer pour l'espacement
                        Spacer(modifier = Modifier.width(8.dp)) // Ajustez la largeur selon vos besoins

                        Text(
                            text = if (choice.is_correct) "Correct" else "Non Correct",
                            fontSize = 16.sp,
                            color = if (choice.is_correct) Color.Green else Color.Red
                        )
                    }
                }
            }
        }
    }

    private fun deleteChoice(choice: Choix) {
        quizDatabaseHelper.deleteChoice(choice.id)
        // Mettez à jour la liste des choix
        questionList = quizDatabaseHelper.getQuestionsByQuizId(quizId) // Recharger les questions
    }

    private fun openEditChoiceDialog(choice: Choix) {
        // Afficher le dialogue de modification pour le choix
        showEditDialog = true
        selectedChoice = choice
    }

    @Composable
    fun EditChoiceDialog(
        choice: Choix,
        onDismiss: () -> Unit,
        onSave: (String) -> Unit
    ) {
        var newText by remember { mutableStateOf(choice.choix_text) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Modifier le choix") },
            text = {
                TextField(
                    value = newText,
                    onValueChange = { newText = it },
                    label = { Text("Nouveau choix") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onSave(newText)
                }) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Annuler")
                }
            }
        )
    }



    @Composable
    fun QuestionListScreen(
        questionList: List<Question>,
        onBackClick: () -> Unit,
        onEditQuestion: (Question) -> Unit,
        onDeleteQuestion: (Question) -> Unit,
        onToggleChoices: (Int) -> Unit,
        quizDatabaseHelper: QuizDatabaseHelper // Ajoute ce paramètre
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF37474F))
                        .width(8.dp)
                        .fillMaxHeight()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "La liste des questions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color(0xFF37474F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            questionList.forEach { question ->
                QuestionItem(
                    question = question,
                    onEditClick = { onEditQuestion(question) },
                    onDeleteClick = { onDeleteQuestion(question) },
                    onToggleChoices = onToggleChoices,
                    isExpanded = (expandedQuestionId == question.id),
                    quizDatabaseHelper = quizDatabaseHelper // Passe l'instance ici
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
