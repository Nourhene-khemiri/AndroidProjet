package com.example.quizmanagement.model

data class Resultat(
    val id: Int = 0,
    val utilisateur_id: Int,
    val quiz_id: Int,
    val score: Int
)
