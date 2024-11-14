package com.example.quizmanagement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizFormScreen(
    title: String,
    description: String,
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    // State pour le titre et la description du quiz
    val quizTitle = remember { mutableStateOf(title) }
    val quizDescription = remember { mutableStateOf(description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre verticale avec le titre "Ajouter un quiz" ou "Modifier un quiz"
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
                text = if (title.isNotEmpty()) "Modifier un quiz" else "Ajouter un quiz",
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

        // Bouton Ajouter ou Modifier Quiz
        Button(
            onClick = { onSubmitClick(quizTitle.value, quizDescription.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (title.isNotEmpty()) "Modifier quiz" else "Ajouter quiz", color = Color.White)
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
