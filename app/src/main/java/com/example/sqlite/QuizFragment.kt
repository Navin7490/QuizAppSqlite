package com.example.sqlite

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.sqlite.databinding.FragmentQuizBinding
import com.example.sqlite.model.SQLiteHelper
import com.example.sqlite.utils.Question
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QuizFragment() : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentQuizBinding
    lateinit var sqLiteHelper: SQLiteHelper
    lateinit var textColorDefaultRb: ColorStateList
    lateinit var questionList: ArrayList<Question>

    private var questionCounter: Int = 0
    private var questionCountTotal: Int = 0
    private var currentQuestion: Question? = null

    private var score: Int = 0
    private var answered: Boolean = true
    private val COUNTDOWN_IN_MILLIS:Long=30000
    lateinit var textColorDefaultCd:ColorStateList
    private var  countDownTimer: CountDownTimer? = null
    var timeLeftInMillis:Long =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        sqLiteHelper = SQLiteHelper(requireContext())
        sqLiteHelper.fillQuestionsTable()


        questionList = sqLiteHelper.getAllQuestions()

        Log.e("questionListTag", "$questionList")

        textColorDefaultRb = binding.radioOption1.textColors
        textColorDefaultCd = binding.tvCountDown.textColors
        questionCountTotal = questionList.size
        questionList.shuffle()

        showNextQuestion()

        binding.btnConfirmNext.setOnClickListener {
            if (!answered) {
                if (binding.radioOption1.isChecked || binding.radioOption2.isChecked || binding.radioOption3.isChecked) {
                    checkAnswer()
                } else {
                    Toast.makeText(requireContext(), "please select an answer", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                showNextQuestion()
            }
        }
        return binding.root
    }

    private fun checkAnswer() {
        answered = true

        val rbSelected: RadioButton = binding.radioGroup.findViewById(binding.radioGroup.checkedRadioButtonId)

        val answerNr = binding.radioGroup.indexOfChild(rbSelected)+1

        if (answerNr == currentQuestion?.answerNr) {
            score++
            binding.tvScore.text = "score:$score"

        }
        countDownTimer?.cancel()

        showSolution()
    }

    private fun showSolution() {
        binding.radioOption1.setTextColor(Color.RED)
        binding.radioOption2.setTextColor(Color.RED)
        binding.radioOption3.setTextColor(Color.RED)
        when (currentQuestion?.answerNr) {

            1 -> {
                binding.radioOption1.setTextColor(Color.GREEN)
                binding.tvQuestion.text = "Answer A is correct"


            }
            2 -> {
                binding.radioOption2.setTextColor(Color.GREEN)
                binding.tvQuestion.text = "Answer B is correct"


            }
            3 -> {
                binding.radioOption3.setTextColor(Color.GREEN)
                binding.tvQuestion.text = "Answer C is correct"


            }


        }

        if (questionCounter < questionCountTotal) {
            binding.btnConfirmNext.text = "Next"
        } else {
            binding.btnConfirmNext.text = "Finish"
        }

    }

    private fun showNextQuestion() {
        binding.radioOption1.setTextColor(textColorDefaultRb)
        binding.radioOption2.setTextColor(textColorDefaultRb)
        binding.radioOption3.setTextColor(textColorDefaultRb)
        binding.radioGroup.clearCheck()

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList[questionCounter]
            binding.tvQuestion.text = currentQuestion?.question
            binding.radioOption1.text = currentQuestion?.option1
            binding.radioOption2.text = currentQuestion?.option2
            binding.radioOption3.text = currentQuestion?.option3

            questionCounter++

            binding.tvQuestionCount.text = "Question:$questionCounter /$questionCountTotal"
            answered = false
            binding.btnConfirmNext.text = "confirm"
            timeLeftInMillis = COUNTDOWN_IN_MILLIS
            startCountDown()

        } else {
            finishQuiz()
        }

    }

    private fun startCountDown() {
        countDownTimer =  object : CountDownTimer(timeLeftInMillis,1000) {
            override fun onTick(millisInFuture: Long) {
                timeLeftInMillis = millisInFuture
                updateCountDownText()
            }

            override fun onFinish() {

                timeLeftInMillis =0
                updateCountDownText()
                checkAnswer()
            }

        }.start()
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds)
        binding.tvCountDown.text = timeFormatted

        if (timeLeftInMillis< 10000){
            binding.tvCountDown.setTextColor(Color.RED)
        } else {
            binding.tvCountDown.setTextColor(textColorDefaultCd)
        }
    }

    private fun finishQuiz() {
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (countDownTimer != null){
            countDownTimer?.cancel()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}