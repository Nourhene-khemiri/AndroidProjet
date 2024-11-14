package com.example.quizmanagement.model

import android.os.Parcel
import android.os.Parcelable

data class Choix(
    val id: Int,
    val question_id: Int, // Lié à l'ID de la question
    val choix_text: String,
    val is_correct: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        question_id = parcel.readInt(), // Lire questionId
        choix_text = parcel.readString() ?: "", // Lire texte
        is_correct = parcel.readByte() != 0.toByte() // Lire isCorrect
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(question_id) // Écrire questionId
        parcel.writeString(choix_text)
        parcel.writeByte(if (is_correct) 1 else 0) // Écrire isCorrect
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Choix> {
        override fun createFromParcel(parcel: Parcel): Choix {
            return Choix(parcel)
        }

        override fun newArray(size: Int): Array<Choix?> {
            return arrayOfNulls(size)
        }
    }
}