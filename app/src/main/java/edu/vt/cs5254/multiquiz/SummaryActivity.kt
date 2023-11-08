package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivitySummaryBinding

private const val EXTRA_CORRECT_ANSWERS = "edu.vt.cs5254.multiquiz.correct_answers"
private const val EXTRA_HINTS_USED = "edu.vt.cs5254.multiquiz.hints_used"

const val EXTRA_RESET_ALL = "edu.vt.cs5254.multiquiz.reset_all"

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private  val summaryVM: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(
            Activity.RESULT_OK,
            Intent().apply { putExtra(EXTRA_RESET_ALL, summaryVM.isResetAll) }
        )
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.resetAllButton.setOnClickListener {
            summaryVM.isResetAll = true
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_RESET_ALL, summaryVM.isResetAll)
                }
            )
            updateView()
        }


        Log.w("--Summary Activity--", "GOT CORRECT ANSWERS ${intent.getIntExtra(
            EXTRA_CORRECT_ANSWERS, -1)}")
        Log.w("--Summary Activity--", "GOT CORRECT HINTS USED ${intent.getIntExtra(
            EXTRA_HINTS_USED, -1)}")

        updateView()
    }

    private fun updateView(){
        if(summaryVM.isResetAll){
            binding.correctAnswers.text = "0"
            binding.hintsUsedCount.text = "0"
            binding.resetAllButton.isEnabled = false
        }
        else {
            binding.correctAnswers.text = intent.getIntExtra(EXTRA_CORRECT_ANSWERS, -1).toString()
            binding.hintsUsedCount.text = intent.getIntExtra(EXTRA_HINTS_USED, -1).toString()
        }
    }

    companion object{
        fun newIntent(
            packageContext: Context,
            correctAnswers: Int,
            hintsUsed: Int
        ):Intent{
             return Intent(packageContext, SummaryActivity::class.java).apply {
                 putExtra(EXTRA_CORRECT_ANSWERS, correctAnswers)
                 putExtra(EXTRA_HINTS_USED, hintsUsed)
             }
        }
    }

}