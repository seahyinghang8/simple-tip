package edu.stanford.seahyinghang8.simpletip

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

enum class ContainerSelected {
    BASE, TIP, NONE
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup container listeners
        baseContainer.setOnClickListener {
            etBase.requestFocus()
            showKeyboard(etBase)
        }

        etBase.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) selectContainer(ContainerSelected.BASE)
        }

        // Set focus to the tip amount
        tipContainer.setOnClickListener {
            selectContainer(ContainerSelected.TIP)
        }

        // Clear focus from every other element
        mainContainer.setOnClickListener {
            clearAllSelected()
        }

        // Instant recalculation when typed
        etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val match = Regex("[\\.]\\d{3,}$").find(s)
                    if (match != null) {
                        val startIdx = match.range.first + 2
                        val endIdx = match.range.last
                        s.delete(startIdx, endIdx)
                    }
                }
                calculateAmounts()
            }
        })

        // Instant recalculation when tip bar is adjusted
        sbTipPercentage.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                calculateAmounts()
                tvTipPercentage.text = "($progress%)"
                if (fromUser) {
                    val color = ArgbEvaluator().evaluate(
                        progress.toFloat() / sbTipPercentage.max,
                        ContextCompat.getColor(applicationContext, R.color.worstTip),
                        ContextCompat.getColor(applicationContext, R.color.bestTip)
                    ) as Int
                    tvTipPercentage.setTextColor(color)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Round total button is clicked
        btnRound.setOnClickListener {
            roundTotalAndTip()
            clearAllSelected()
            btnRound.visibility = View.GONE
        }

        // Set the initial values
        tvTipPercentage.text = "($INITIAL_TIP_PERCENT%)"
        tvTipPercentage.setTextColor(ContextCompat.getColor(applicationContext, R.color.hint))
        etBase.requestFocus()
        sbTipPercentage.progress = INITIAL_TIP_PERCENT
    }

    private fun roundTotalAndTip() {
        val totalAmountValue = tvTotalAmount.text.toString().toDouble()
        val tipAmountValue = tvTipAmount.text.toString().toDouble()

        var roundedTotal = totalAmountValue.roundToInt().toDouble()
        var roundedTip = 0.0
        val roundDiff = roundedTotal - totalAmountValue

        when {
            roundDiff < 0 && abs(roundDiff) > tipAmountValue -> {
                // supposedly round down, but not enough in tips
                // thus, we will round up instead
                val roundUpVal = 1.0 + roundDiff
                roundedTotal = totalAmountValue + roundUpVal
                roundedTip = tipAmountValue + roundUpVal
            }
            else -> {
                // round up / round down
                roundedTip = tipAmountValue + roundDiff
            }
        }

        tvTipAmount.text = "%.2f".format(roundedTip)
        tvTotalAmount.text = "%.2f".format(roundedTotal)
    }

    private fun calculateAmounts() {
        val inputBaseValue =
            if (etBase.text.isNotEmpty()) etBase.text.toString().toDouble() else 0.0
        val inputTipPercent = sbTipPercentage.progress.toDouble() / 100.0
        val outputTipAmount = inputBaseValue * inputTipPercent
        val outputTotalAmount = outputTipAmount + inputBaseValue

        tvTipAmount.text = "%.2f".format(outputTipAmount)
        tvTotalAmount.text = "%.2f".format(outputTotalAmount)

        // check if total amount is rounded
        val match = Regex("[\\.][0]{2}").find(tvTotalAmount.text)
        btnRound.visibility = if (match != null) View.GONE else View.VISIBLE
    }

    private fun clearAllSelected() {
        selectContainer(ContainerSelected.NONE)
        etBase.clearFocus()
    }

    private fun hideKeyboard(view: View) {
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showKeyboard(view: View) {
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    private fun selectContainer(selected: ContainerSelected) {
        var baseVisibility = View.GONE
        var tipVisibility = View.GONE

        when (selected) {
            ContainerSelected.BASE -> {
                baseVisibility = View.VISIBLE
                tvTipPercentage.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.hint
                    )
                )
            }
            ContainerSelected.TIP -> {
                tipVisibility = View.VISIBLE
                etBase.clearFocus()
                hideKeyboard(etBase)
            }
            ContainerSelected.NONE -> {
                etBase.clearFocus()
                hideKeyboard(etBase)
                tvTipPercentage.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.hint
                    )
                )
            }
        }

        baseHighlight.visibility = baseVisibility
        tipHighlight.visibility = tipVisibility
        sbTipPercentage.visibility = tipVisibility
    }
}