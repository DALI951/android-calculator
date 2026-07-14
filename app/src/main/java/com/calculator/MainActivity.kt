package com.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btnDot
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onNumberClick(it as Button) }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperationClick("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { onOperationClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperationClick("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperationClick("÷") }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { onBackspaceClick() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercentClick() }
    }

    private fun onNumberClick(button: Button) {
        if (isNewOperation) {
            currentNumber = ""
            isNewOperation = false
        }
        val number = button.text.toString()
        if (number == "." && currentNumber.contains(".")) return
        currentNumber += number
        tvResult.text = currentNumber
    }

    private fun onOperationClick(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty() && !isNewOperation) {
                calculate()
            }
            previousNumber = currentNumber
            operation = op
            isNewOperation = true
            tvResult.text = "$previousNumber $operation"
        }
    }

    private fun onEqualsClick() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty()) {
            calculate()
            operation = ""
            isNewOperation = true
        }
    }

    private fun calculate() {
        val num1 = previousNumber.toDoubleOrNull() ?: return
        val num2 = currentNumber.toDoubleOrNull() ?: return
        val result = when (operation) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else {
                tvResult.text = "Error"
                previousNumber = ""
                currentNumber = ""
                return
            }
            else -> num2
        }
        val display = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        tvResult.text = display
        previousNumber = display
        currentNumber = ""
    }

    private fun onClearClick() {
        currentNumber = ""
        previousNumber = ""
        operation = ""
        isNewOperation = true
        tvResult.text = "0"
    }

    private fun onBackspaceClick() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
            tvResult.text = if (currentNumber.isEmpty()) "0" else currentNumber
        }
    }

    private fun onPercentClick() {
        if (currentNumber.isNotEmpty()) {
            val value = currentNumber.toDouble() / 100
            currentNumber = value.toString()
            tvResult.text = currentNumber
        }
    }
}