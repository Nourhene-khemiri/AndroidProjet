package com.example.quizmanagement

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizmanagement.ui.AddQuizActivity
import com.example.quizmanagement.ui.theme.QuizManagementTheme
import com.example.quizmanagement.ui.theme.ListQuizActivity
import com.example.quizmanagement.ui.theme.ListQuizActivityUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        onCreateQuizClick = {
                            // Lancer l'activité d'ajout de quiz
                            val intent = Intent(this, AddQuizActivity::class.java)
                            startActivity(intent)
                        },
                        onViewQuizzesClick = {
                            // Lancer l'activité de liste des quiz
                            val intent = Intent(this, ListQuizActivity::class.java)
                            startActivity(intent)
                        },
                        onPassQuizzesClick = {
                            // Lancer l'activité pour passer les quiz
                            val intent = Intent(this, ListQuizActivityUser::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onCreateQuizClick: () -> Unit,
    onViewQuizzesClick: () -> Unit,
    onPassQuizzesClick: () -> Unit,
    modifier: Modifier = Modifier // Gardez le modificateur ici
) {
    Column(
        modifier = modifier // Utilisez le modificateur reçu
            .fillMaxSize()
            .padding(16.dp), // Ajoutez un padding interne si nécessaire
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenue dans l'application Quiz Management",
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = onCreateQuizClick, // Utiliser le callback ici
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Créer un nouveau quiz")
        }
        Button(
            onClick = onViewQuizzesClick // Vous pouvez ajouter une logique ici
        ) {
            Text("Voir les quiz")
        }
        Button(
            onClick = onPassQuizzesClick // Appeler la nouvelle fonction ici
        ) {
            Text("Passer les quiz")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuizManagementTheme {
        HomeScreen(
            onCreateQuizClick = { /* Action pour créer un quiz */ },
            onViewQuizzesClick = { /* Action pour voir les quiz */ },
            onPassQuizzesClick = { /* Action pour passer les quiz */ }
        )
    }
}

@Composable
fun AddQuizScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ajouter un nouveau quiz")
        // Ajoutez ici les champs pour ajouter un quiz
    }
}

