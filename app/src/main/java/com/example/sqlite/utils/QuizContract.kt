package com.example.sqlite.utils

import android.provider.BaseColumns

class QuizContract {
     class QuestionsTable:BaseColumns{
        companion object{
            const val TABLE_NAME="quiz_questions"
            const val COLUMN_QUESTION ="question"
            const val COLUMN_OPTION1 ="option1"
            const val COLUMN_OPTION2 ="option2"
            const val COLUMN_OPTION3 ="option3"
            const val COLUMN_ANSWER_NR ="answer_nr"


        }
    }
}