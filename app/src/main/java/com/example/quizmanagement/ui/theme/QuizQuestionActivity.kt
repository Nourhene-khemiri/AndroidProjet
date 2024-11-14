package com.example.quizmanagement.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.quizmanagement.model.ReponseUtilisateur
import com.example.quizmanagement.model.Resultat
import com.example.quizmanagement.model.Utilisateur


class QuizQuestionsActivity : AppCompatActivity() {

    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private var currentQuestionIndex by mutableStateOf(0)
    private lateinit var questions: List<Question>
    private var currentChoices = mutableStateListOf<Choix>()

    private var selectedAnswers = mutableStateMapOf<Int, MutableList<Int>>()
    private var choiceSelection = mutableStateMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer l'ID du quiz et l'ID de l'utilisateur depuis l'intent
        val quizId = intent.getIntExtra("QUIZ_ID", -1) // Valeur par défaut si non trouvé
        val userId = intent.getIntExtra("USER_ID", -1) // Récupérer l'ID de l'utilisateur

        // Vérifier si les ID ont été correctement récupérés
        if (quizId != -1 && userId != -1) {
            // Utiliser quizId et userId dans la logique de ton activité
            Log.d("QuizQuestionsActivity", "Quiz ID: $quizId, User ID: $userId")
        } else {
            // Gérer l'erreur si l'ID est manquant
            Log.e("QuizQuestionsActivity", "Quiz ID ou User ID manquant")
        }

        quizDatabaseHelper = QuizDatabaseHelper(this)
        questions = quizDatabaseHelper.getQuestionsForQuiz(quizId)

        loadCurrentChoices()

        setContent {
            MaterialTheme {
                QuizQuestionsScreen(quizId)
            }
        }
    }

    private fun loadCurrentChoices() {
        if (questions.isNotEmpty()) {
            val questionId = questions[currentQuestionIndex].id
            currentChoices.clear()
            currentChoices.addAll(quizDatabaseHelper.getChoicesByQuestionId(questionId))

            // Initialiser l'état de sélection des choix
            choiceSelection.clear()
            currentChoices.forEach { choice ->
                choiceSelection[choice.id] = selectedAnswers[questionId]?.contains(choice.id) == true
            }
        }
    }

    @Composable
    fun QuizQuestionsScreen(quizId: Int) {

        if (questions.isEmpty()) {
            Text("Aucune question disponible.")
            return
        }

        val currentQuestion = questions[currentQuestionIndex]

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
                    text = "Commençant le quiz",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color(0xFF37474F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Question ${currentQuestionIndex + 1} / ${questions.size}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = currentQuestion.question_text,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Liste des choix pour la question courante
            currentChoices.forEach { choice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = choiceSelection[choice.id] ?: false,
                        onCheckedChange = { isChecked ->
                            choiceSelection[choice.id] = isChecked

                            val selected = selectedAnswers.getOrPut(currentQuestion.id) { mutableListOf() }
                            if (isChecked) {
                                selected.add(choice.id)
                            } else {
                                selected.remove(choice.id)
                            }
                        }
                    )
                    Text(text = choice.choix_text, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Afficher le bouton "Suivant" si ce n'est pas la dernière question
            if (currentQuestionIndex < questions.size - 1) {
                Button(
                    onClick = {
                        currentQuestionIndex++
                        loadCurrentChoices()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Suivant")
                }
            } else {
                Button(
                    onClick = {
                        val userId = 1 // ID de l'utilisateur (mis à 1 comme tu l'as mentionné)

                        // Enregistrer les réponses de l'utilisateur avec l'ID du quiz
                        saveUserResponses(userId, quizId)

                        // Calculer le score
                        val score = calculateScore(quizId)

                        // Enregistrer le score
                        saveScore(score, quizId)

                        // Rediriger vers une autre activité ou montrer un message de succès
                        startActivity(Intent(this@QuizQuestionsActivity, ListQuizActivityUser::class.java))
                        finish()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Enregistrer")
                }
            }

            // Bouton Annuler
            Button(
                onClick = {
                    startActivity(Intent(this@QuizQuestionsActivity, ListQuizActivityUser::class.java))
                    finish()
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("Annuler")
            }
        }
    }

    private fun saveUserResponses(userId: Int,quizId: Int) {
        var insertionSuccessful = true // Pour suivre le succès global des insertions
        selectedAnswers.forEach { (questionId, choices) ->
            choices.forEach { choiceId ->
                val reponseUtilisateur = ReponseUtilisateur(
                    utilisateur_id = userId,
                    quiz_id = quizId,
                    question_id = questionId,
                    choix_id = choiceId
                )
                val result = quizDatabaseHelper.insertUserResponse(reponseUtilisateur)
                if (!result) {
                    insertionSuccessful = false // Si une insertion échoue
                }
            }
        }

        // Afficher un message à l'utilisateur sur le succès ou l'échec de l'insertion
        if (insertionSuccessful) {
            Toast.makeText(this, "Réponses enregistrées avec succès !", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement des réponses.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateScore(quizId: Int): Int {
        var score = 0
        val correctAnswers = mutableMapOf<Int, Int>() // Pour stocker les choix corrects

        // Récupérer tous les choix corrects pour le quiz
        val allChoices = quizDatabaseHelper.getAllChoicesForQuiz(quizId) // Implémente cette méthode pour récupérer les choix

        allChoices.forEach { choice ->
            if (choice.is_correct) {
                correctAnswers[choice.question_id] = choice.id // Associer la question à son choix correct
            }
        }

        // Comparer les réponses de l'utilisateur avec les choix corrects
        selectedAnswers.forEach { (questionId, choices) ->
            choices.forEach { choiceId ->
                if (correctAnswers[questionId] == choiceId) {
                    score += 1 // On ajoute 1 point pour chaque bonne réponse
                }
            }
        }

        return score
    }

    private fun saveScore(score: Int, quizId: Int) {
        val resultat = Resultat(
            utilisateur_id = 1, // ID statique de l'utilisateur
            quiz_id = quizId,
            score = score
        )
        quizDatabaseHelper.insertResult(1, quizId, score) // Implémente cette méthode pour insérer le résultat
    }











}

