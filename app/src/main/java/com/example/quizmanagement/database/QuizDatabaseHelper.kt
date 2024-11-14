package com.example.quizmanagement.database

import Quiz
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quizmanagement.model.Choix
import com.example.quizmanagement.model.Question
import com.example.quizmanagement.model.ReponseUtilisateur


class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz_management.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_QUIZZES = "quizzes"
        const val TABLE_QUESTIONS = "questions"
        const val TABLE_CHOIX = "choix" // Nom de la table Choix
        const val TABLE_UTILISATEUR = "utilisateur"
        const val TABLE_RESULTAT = "resultat"
        const val TABLE_REPONSE_UTILISATEUR = "reponseUtilisateur"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Créer les tables ici
        val createQuizTable = "CREATE TABLE quizzes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)"
        db.execSQL(createQuizTable)

        // Ajoutez la création de la table questions
        db.execSQL("CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, quiz_id INTEGER, question_text TEXT, FOREIGN KEY(quiz_id) REFERENCES quizzes(id))")

        // Créer la table des choix
        val createChoixTable = "CREATE TABLE choix (id INTEGER PRIMARY KEY AUTOINCREMENT, question_id INTEGER, choix_text TEXT, is_correct INTEGER, FOREIGN KEY(question_id) REFERENCES questions(id))"
        db.execSQL(createChoixTable)

        // Création de la table Utilisateur
        val createUtilisateurTable = "CREATE TABLE utilisateur (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, prenom TEXT, email TEXT)"
        db.execSQL(createUtilisateurTable)

        // Création de la table Resultat
        val createResultatTable = "CREATE TABLE resultat (id INTEGER PRIMARY KEY AUTOINCREMENT, utilisateur_id INTEGER, quiz_id INTEGER, score INTEGER, FOREIGN KEY(utilisateur_id) REFERENCES $TABLE_UTILISATEUR(id), FOREIGN KEY(quiz_id) REFERENCES $TABLE_QUIZZES(id))"
        db.execSQL(createResultatTable)

        // Création de la table ReponseUtilisateur
        val createReponseUtilisateurTable = "CREATE TABLE reponseUtilisateur (id INTEGER PRIMARY KEY AUTOINCREMENT, utilisateur_id INTEGER, quiz_id INTEGER, question_id INTEGER, choix_id INTEGER, FOREIGN KEY(utilisateur_id) REFERENCES $TABLE_UTILISATEUR(id), FOREIGN KEY(quiz_id) REFERENCES $TABLE_QUIZZES(id), FOREIGN KEY(question_id) REFERENCES $TABLE_QUESTIONS(id), FOREIGN KEY(choix_id) REFERENCES $TABLE_CHOIX(id))"
        db.execSQL(createReponseUtilisateurTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Gérer la mise à jour de la base de données
        db.execSQL("DROP TABLE IF EXISTS quizzes")
        onCreate(db)

        // Gérer la mise à jour de la base de données
        db.execSQL("DROP TABLE IF EXISTS questions")
        onCreate(db)

        db.execSQL("DROP TABLE IF EXISTS choix") // Supprimer la table Choix si elle existe
        onCreate(db)

        db.execSQL("DROP TABLE IF EXISTS utilisateur")
        onCreate(db)

        db.execSQL("DROP TABLE IF EXISTS resultat")
        onCreate(db)

        db.execSQL("DROP TABLE IF EXISTS reponseUtilisateur")
        onCreate(db)
    }
    fun getAllQuizzes(): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM quizzes", null)

        if (cursor.moveToFirst()) {
            do {
                val quiz = Quiz(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    questions = mutableListOf()
                )
                quizList.add(quiz)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return quizList
    }

    fun updateQuiz(quiz: Quiz): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put("title", quiz.title)
            put("description", quiz.description)
        }

        // Remplacez "quiz_id" par le nom de la colonne ID de votre table
        return db.update("quizzes", contentValues, "id = ?", arrayOf(quiz.id.toString()))
    }

    fun deleteQuiz(quizId: Int): Int {
        val db = this.writableDatabase
        return db.delete("quizzes", "id = ?", arrayOf(quizId.toString()))
    }

    fun addQuestion(quizId: Int, questionText: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("quiz_id", quizId) // Assurez-vous que le champ correspond bien à votre table
        contentValues.put("question_text", questionText)
        return db.insert("questions", null, contentValues)
    }

    fun getQuestionsByQuizId(quiz_id: Int): List<Question> {
        val questionList = mutableListOf<Question>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM Questions WHERE quiz_id = ?", arrayOf(quiz_id.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val texte = cursor.getString(cursor.getColumnIndexOrThrow("question_text"))
                questionList.add(Question(id = id, question_text = texte, quiz_id = quiz_id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return questionList
    }

    fun updateQuestion(question: Question): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put("question_text", question.question_text)
        }

        // Mettre à jour la question dans la base de données en utilisant l'ID de la question
        val rowsAffected = db.update(
            TABLE_QUESTIONS,
            contentValues,
            "id = ?",
            arrayOf(question.id.toString())
        )

        db.close()
        return rowsAffected
    }

    fun deleteQuestion(questionId: Int): Int {
        val db = this.writableDatabase

        // Supprimer la question en utilisant son ID
        val rowsDeleted = db.delete(
            TABLE_QUESTIONS,
            "id = ?",
            arrayOf(questionId.toString())
        )

        db.close()
        return rowsDeleted
    }

    fun addChoix(choix: Choix): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("question_id", choix.question_id) // questionId est maintenant un Int
            put("choix_text", choix.choix_text)       // Corriger l'attribut texte dans la base de données
            put("is_correct", if (choix.is_correct) 1 else 0)
        }
        return db.insert(TABLE_CHOIX, null, contentValues)
    }

    fun getChoicesByQuestionId(questionId: Int): List<Choix> {
        val choicesList = mutableListOf<Choix>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM choix WHERE question_id = ?", arrayOf(questionId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val choix = Choix(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    question_id = cursor.getInt(cursor.getColumnIndexOrThrow("question_id")), // Utilise question_id
                    choix_text = cursor.getString(cursor.getColumnIndexOrThrow("choix_text")),
                    is_correct = cursor.getInt(cursor.getColumnIndexOrThrow("is_correct")) != 0
                )
                choicesList.add(choix)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return choicesList
    }

    fun deleteChoice(choiceId: Int) {
        val db = this.writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(choiceId.toString())

        // Exécutez la requête de suppression
        db.delete(TABLE_CHOIX, whereClause, whereArgs)
        db.close()
    }

    fun getQuestionsForQuiz(quizId: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val db = this.readableDatabase

        // Requête pour récupérer les questions associées à un quiz
        val cursor = db.rawQuery("SELECT * FROM questions WHERE quiz_id = ?", arrayOf(quizId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    quiz_id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id")), // Utilise question_id
                    question_text = cursor.getString(cursor.getColumnIndexOrThrow("question_text")),
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    fun insertUserResponse(reponseUtilisateur: ReponseUtilisateur): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("utilisateur_id", reponseUtilisateur.utilisateur_id)
            put("quiz_id", reponseUtilisateur.quiz_id)
            put("question_id", reponseUtilisateur.question_id)
            put("choix_id", reponseUtilisateur.choix_id)
        }

        val id = db.insert("reponseUtilisateur", null, values)
        return id != -1L // Retourne vrai si l'insertion a réussi
    }


    fun insertResult(userId: Int, quizId: Int, score: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("utilisateur_id", userId)
        values.put("quiz_id", quizId)
        values.put("score", score)

        db.insert("resultat", null, values)
    }

    fun getScoreForUserAndQuiz(userId: Int, quizId: Int): Int? {
        var score: Int? = null
        val db = this.readableDatabase
        val cursor = db.query(
            "resultat",
            arrayOf("score"),
            "utilisateur_id=? AND quiz_id=?",
            arrayOf(userId.toString(), quizId.toString()),
            null,
            null,
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                score = cursor.getInt(cursor.getColumnIndexOrThrow("score"))
            }
            cursor.close()
        }
        return score
    }

    fun getAllChoicesForQuiz(quizId: Int): List<Choix> {
        val choices: MutableList<Choix> = ArrayList()
        val db = this.readableDatabase

        // Récupérer toutes les questions pour le quiz donné
        val questionCursor =
            db.query("questions", null, "quiz_id=?", arrayOf(quizId.toString()), null, null, null)

        if (questionCursor != null) {
            while (questionCursor.moveToNext()) {
                val questionId = questionCursor.getInt(questionCursor.getColumnIndexOrThrow("id"))

                // Récupérer tous les choix pour chaque question
                val choiceCursor = db.query(
                    "choix",
                    null,
                    "question_id=?",
                    arrayOf(questionId.toString()),
                    null,
                    null,
                    null
                )
                if (choiceCursor != null) {
                    while (choiceCursor.moveToNext()) {
                        val id = choiceCursor.getInt(choiceCursor.getColumnIndexOrThrow("id"))
                        val choiceText =
                            choiceCursor.getString(choiceCursor.getColumnIndexOrThrow("choix_text"))
                        val isCorrect =
                            choiceCursor.getInt(choiceCursor.getColumnIndexOrThrow("is_correct")) > 0

                        val choix = Choix(id, questionId, choiceText, isCorrect)
                        choices.add(choix)
                    }
                    choiceCursor.close()
                }
            }
            questionCursor.close()
        }
        return choices
    }


}