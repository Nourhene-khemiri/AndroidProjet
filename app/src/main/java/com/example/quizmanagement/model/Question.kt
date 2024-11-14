package com.example.quizmanagement.model

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val id: Int,
    var question_text: String,
    val quiz_id: Int,  // Ajout de quizId pour lier la question à un quiz
    val choix: MutableList<Choix> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
              parcel.readInt(),  // lecture de quizId
        parcel.createTypedArrayList(Choix.CREATOR)?.toMutableList() ?: mutableListOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question_text)
        parcel.writeInt(quiz_id)  // écriture de quizId
        parcel.writeTypedList(choix)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}