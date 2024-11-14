package com.example.quizmanagement.ui.theme

import Quiz
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizmanagement.QuizActivity
import com.example.quizmanagement.R
import com.example.quizmanagement.adapter.QuizAdapter
import com.example.quizmanagement.database.QuizDatabaseHelper
import com.example.quizmanagement.ui.AddQuizActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchQuizzes()

        val addQuizButton = findViewById<Button>(R.id.add_quiz_button)
        addQuizButton.setOnClickListener {
            val intent = Intent(this, AddQuizActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchQuizzes() {
        val dbHelper = QuizDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query("quizzes", null, null, null, null, null, null)

        val quizzes = mutableListOf<Quiz>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

            quizzes.add(Quiz(id, title, description, mutableListOf())) // Empty list for questions
        }

        cursor.close()
        db.close()

        quizAdapter = QuizAdapter(quizzes) { quiz ->
            val intent = Intent(this@HomeActivity, QuizActivity::class.java)
            intent.putExtra("quizId", quiz.id)
            startActivity(intent)
        }
        recyclerView.adapter = quizAdapter
    }
}