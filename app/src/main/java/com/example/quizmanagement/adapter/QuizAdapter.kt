package com.example.quizmanagement.adapter

import Quiz
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizmanagement.R

class QuizAdapter(private val quizList: List<Quiz>, private val onQuizClick: (Quiz) -> Unit) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizTitle: TextView = itemView.findViewById(R.id.quizTitle)
        val quizDescription: TextView = itemView.findViewById(R.id.quizDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.quizTitle.text = quiz.title
        holder.quizDescription.text = quiz.description
        holder.itemView.setOnClickListener { onQuizClick(quiz) }
    }

    override fun getItemCount() = quizList.size
}
