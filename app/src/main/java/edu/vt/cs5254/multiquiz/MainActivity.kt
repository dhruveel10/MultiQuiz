package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Name: Dhruveel Chouhan
    // PID: dhruveel10

    private lateinit var binding: ActivityMainBinding
    private lateinit var answerButtons: List<Button>

    private val quizVM: QuizViewModel by viewModels()

    private val summaryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val isReset = it.data?.getBooleanExtra(EXTRA_RESET_ALL, false) ?: false
            if (isReset){
                quizVM.resetAll()
            }
        }
        quizVM.nextQuestion()
        updateQuestion()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerButtons = listOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        )

        binding.hintButton.setOnClickListener {
            quizVM.answerList.filter {
                !it.isCorrect && it.isEnabled
            }.random().let {
                it.isSelected = false
                it.isEnabled = false
            }
            updateView()
        }

        binding.submitButton.setOnClickListener {
            if(quizVM.isLastQuestion){
                val intent = SummaryActivity.newIntent(
                    this,
                    quizVM.correctAnswersCount,
                    quizVM.hintsUsedCount
                )
                summaryLauncher.launch(intent)
            }else{
                quizVM.nextQuestion()
                updateQuestion()
            }
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        binding.questionText.setText(quizVM.questionText)

        quizVM.answerList.zip(answerButtons).forEach { (answer, button) ->
            button.setText(answer.textResId)
        }

        quizVM.answerList.zip(answerButtons).forEach { (answer, button) ->
            button.setOnClickListener {
                quizVM.answerList.filter {
                    it != answer
                }.forEach {
                    it.isSelected = false
                }
                answer.isSelected = !answer.isSelected
                updateView()
            }
        }
        updateView()
    }

    private fun updateView() {
        var isSubmitButtonDisabled = false
        quizVM.answerList.zip(answerButtons).forEach { (answer, button) ->
            button.isEnabled = answer.isEnabled
            button.isSelected = answer.isSelected
            isSubmitButtonDisabled = isSubmitButtonDisabled || answer.isSelected
            button.updateColor()
        }

        binding.hintButton.isEnabled = usingAny()
        binding.submitButton.isEnabled = isSubmitButtonDisabled
    }

    private fun usingAny(): Boolean {
        return quizVM.answerList
            .filter { !it.isCorrect }
            .any { it.isEnabled }
    }
}
