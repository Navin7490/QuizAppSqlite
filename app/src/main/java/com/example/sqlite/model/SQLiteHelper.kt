package com.example.sqlite.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite.utils.Question
import com.example.sqlite.utils.QuizContract

const val COLUMN_FIRST_NAME = "First_name"
const val COLUMN_LAST_NAME = "Last_name"
const val COLUMN_EMAIL = "Email"
const val COLUMN_MOBILE = "Mobile"
const val Column_PASSWORD ="Password"
const val COLUMN_ID = "Id"
const val TABLE_NAME = "Registration"

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME_QUESTIONS, null, DATABASE_version) {
    var db:SQLiteDatabase?=null

    override fun onCreate(db: SQLiteDatabase?) {
        this.db=db
        db?.execSQL(CREATE_TABLE_QUESTION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${QuizContract.QuestionsTable.TABLE_NAME}")
        onCreate(db)
    }


    companion object {

        const val DATABASE_NAME = "Database.db"
        const val DATABASE_version = 1
        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FIRST_NAME TEXT, " +
                "$COLUMN_LAST_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_MOBILE TEXT)"
        const val DATABASE_NAME_QUESTIONS = "Questions.db"
        const val CREATE_TABLE_QUESTION = "CREATE TABLE ${QuizContract.QuestionsTable.TABLE_NAME}(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${QuizContract.QuestionsTable.COLUMN_QUESTION} TEXT, " +
                "${QuizContract.QuestionsTable.COLUMN_OPTION1} TEXT, " +
                "${QuizContract.QuestionsTable.COLUMN_OPTION2} TEXT, " +
                "${QuizContract.QuestionsTable.COLUMN_OPTION3} TEXT, " +

                "${QuizContract.QuestionsTable.COLUMN_ANSWER_NR} INTEGER)"


    }

    fun fillQuestionsTable(){

        val q1 = Question("A is correct","A","B","C",1)
        addQuestions(q1)
        val q2 = Question("B is correct","A","B","C",2)
        addQuestions(q2)
        val q3 = Question("C is correct","A","B","C",3)
        addQuestions(q3)
        val q4 = Question("A is correct again","A","B","C",1)
        addQuestions(q4)
        val q5 = Question("B is correct again","A","B","C",2)
        addQuestions(q5)
    }

    private fun  addQuestions(question: Question){
        val value= ContentValues().apply {
            put(QuizContract.QuestionsTable.COLUMN_QUESTION,question.question)
            put(QuizContract.QuestionsTable.COLUMN_OPTION1,question.option1)
            put(QuizContract.QuestionsTable.COLUMN_OPTION2,question.option2)
            put(QuizContract.QuestionsTable.COLUMN_OPTION3,question.option3)
            put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR,question.answerNr)

        }
        db = writableDatabase

        db?.insert(QuizContract.QuestionsTable.TABLE_NAME,null,value)

    }

    @SuppressLint("Range")
    fun getAllQuestions():ArrayList<Question>{

        val questionList:ArrayList<Question> = ArrayList()
        db = readableDatabase
        val c:Cursor?= db?.rawQuery("SELECT * FROM ${QuizContract.QuestionsTable.TABLE_NAME}",null)
        if (c != null) {
            if (c.moveToFirst()){
                do {
                   val question = Question(
                       c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)),
                       c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1)),
                     c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2)),
                     c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3)),
                     c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR)))

                    questionList.add(question)
                }while (c.moveToNext())

            }
        }
        c?.close()

        return questionList

    }




}