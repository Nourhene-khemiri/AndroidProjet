package com.example.quizmanagement.model

data class ReponseUtilisateur(
    val id: Int = 0,
    val utilisateur_id: Int,
    val quiz_id: Int,
    val question_id: Int,
    val choix_id: Int
)