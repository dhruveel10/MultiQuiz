package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.q1, listOf(
            Answer(R.string.q1a3, true),
            Answer(R.string.q1a1, false),
            Answer(R.string.q1a2, false),
            Answer(R.string.q1a4, false),
        )
        ),
        Question(R.string.q2, listOf(
            Answer(R.string.q2a1, false),
            Answer(R.string.q2a3, true),
            Answer(R.string.q2a2, false),
            Answer(R.string.q2a4, false),
        )
        ),
        Question(R.string.q3, listOf(
            Answer(R.string.q3a2, false),
            Answer(R.string.q3a3, false),
            Answer(R.string.q3a1, true),
            Answer(R.string.q3a4, false),
        )
        ),
        Question(R.string.q4, listOf(
            Answer(R.string.q4a1, false),
            Answer(R.string.q4a2, false),
            Answer(R.string.q4a3, false),
            Answer(R.string.q4a4, true),
        )
        ),
    )

    private var currentIndex = 0

    val answerList
        get() = questionBank[currentIndex].answerList

    val questionText
        get() = questionBank[currentIndex].questionResId

    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    val isLastQuestion
        get() = currentIndex == questionBank.size - 1

    val correctAnswersCount
        get() = questionBank.sumOf {q ->
            q.answerList.count{a ->
                a.isCorrect && a.isSelected
            }
        }

    val hintsUsedCount
        get() = questionBank.sumOf {q ->
            q.answerList.count{a ->
                !a.isEnabled
            }
        }

    fun resetAll(){
        questionBank.forEach { q ->
            q.answerList.forEach{a ->
                a.isEnabled = true
                a.isSelected = false
            }
        }
    }
}