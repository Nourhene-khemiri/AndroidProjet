package com.example.quizmanagement.ui.theme

import Quiz
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmanagement.R
import com.example.quizmanagement.database.QuizDatabaseHelper


class EditQuizActivity : AppCompatActivity() {

    private lateinit var quiz: Quiz
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quiz)

        quizDatabaseHelper = QuizDatabaseHelper(this)

        // Récupérer les données du quiz depuis l'intent
        quiz = intent.getParcelableExtra("QUIZ_DATA") ?: return

        // Initialiser le formulaire avec les données du quiz
        initViews()

        // Définir le bouton de modification
        findViewById<Button>(R.id.ModifyQuizButton).setOnClickListener {
            updateQuiz()
        }

        // Ajoute cet écouteur de clic pour le bouton Annuler
        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            annuler() // Appelle la méthode annuler
        }
    }

    private fun initViews() {
        // Supposons que vous avez des EditText pour le titre et la description
        findViewById<EditText>(R.id.QuizTitleEditText).setText(quiz.title)
        findViewById<EditText>(R.id.QuizDescriptionEditText).setText(quiz.description)
    }

    private fun annuler() {
        val intent = Intent(this, ListQuizActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateQuiz() {
        val newTitle = findViewById<EditText>(R.id.QuizTitleEditText).text.toString()
        val newDescription = findViewById<EditText>(R.id.QuizDescriptionEditText).text.toString()

        // Vérifiez que les champs ne sont pas vides
        if (newTitle.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Mettre à jour le quiz dans la base de données
        quiz.title = newTitle
        quiz.description = newDescription

        val updatedRows = quizDatabaseHelper.updateQuiz(quiz) // Assurez-vous que cette méthode existe

        if (updatedRows > 0) {
            Toast.makeText(this, "Quiz modifié avec succès", Toast.LENGTH_SHORT).show()
            finish() // Fermez l'activité après la mise à jour
        } else {
            Toast.makeText(this, "Échec de la modification du quiz", Toast.LENGTH_SHORT).show()
        }
    }
}
