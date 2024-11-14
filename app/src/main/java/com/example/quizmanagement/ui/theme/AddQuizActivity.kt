package com.example.quizmanagement.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmanagement.MainActivity
import com.example.quizmanagement.database.QuizDatabaseHelper

class AddQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddQuizScreen(
                onSubmitClick = { title, description -> ajouterQuiz(title, description) },
                onCancelClick = { annuler() }
            )
        }

    }

    private fun ajouterQuiz(quizTitle: String, quizDescription: String) {
        val dbHelper = QuizDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("title", quizTitle)
            put("description", quizDescription)
        }

        val newRowId = db.insert(QuizDatabaseHelper.TABLE_QUIZZES, null, values)

        if (newRowId == -1L) {
            Toast.makeText(this, "Erreur lors de l'ajout du quiz", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Quiz ajouté avec succès!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        db.close()
    }

    private fun annuler() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun AddQuizScreen(onSubmitClick: (String, String) -> Unit, onCancelClick: () -> Unit) {
    val quizTitle = remember { mutableStateOf("") }
    val quizDescription = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre verticale avec le titre "Ajouter un quiz"
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
                text = "Ajouter un quiz",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color(0xFF37474F)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Champ pour le titre du quiz
        TextField(
            value = quizTitle.value,
            onValueChange = { quizTitle.value = it },
            label = { Text("Titre du quiz") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Champ pour la description du quiz
        TextField(
            value = quizDescription.value,
            onValueChange = { quizDescription.value = it },
            label = { Text("Description du quiz") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton Ajouter Quiz
        Button(
            onClick = { onSubmitClick(quizTitle.value, quizDescription.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ajouter quiz", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bouton Annuler
        Button(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Annuler", color = Color.White)
        }
    }
}
