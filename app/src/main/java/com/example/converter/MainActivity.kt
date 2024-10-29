package com.example.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etQuantity: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar

    private val currencies = arrayOf("USD", "EUR", "GBP", "VND", "JPY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etQuantity = findViewById(R.id.etQuantity)
        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        tvResult = findViewById(R.id.tvResult)
        progressBar = findViewById(R.id.progressbar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                convertCurrency() // Gọi hàm chuyển đổi
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        etQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertCurrency() // Gọi hàm chuyển đổi
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun convertCurrency() {
        val quantity = etQuantity.text.toString().toDoubleOrNull()
        if (quantity == null) {
            tvResult.text = "Output value"
            return
        }

        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()

        progressBar.visibility = View.VISIBLE

        val convertedValue = performConversion(quantity, fromCurrency, toCurrency)
        tvResult.text = String.format("%.2f %s", convertedValue, toCurrency)

        progressBar.visibility = View.GONE
    }

    private fun performConversion(value: Double, fromCurrency: String, toCurrency: String): Double {
        val exchangeRates = mapOf(
            "USD-EUR" to 0.94,
            "USD-GBP" to 0.78,
            "USD-VND" to 24100.0,
            "USD-JPY" to 150.0,
            "EUR-USD" to 1.06,
            "EUR-GBP" to 0.83,
            "EUR-VND" to 25600.0,
            "EUR-JPY" to 160.0,
            "GBP-USD" to 1.28,
            "GBP-EUR" to 1.20,
            "GBP-VND" to 31000.0,
            "GBP-JPY" to 200.0,
            "VND-USD" to 0.000042,
            "VND-EUR" to 0.000039,
            "VND-GBP" to 0.000032,
            "VND-JPY" to 0.0071,
            "JPY-USD" to 0.0067,
            "JPY-EUR" to 0.0062,
            "JPY-GBP" to 0.0050,
            "JPY-VND" to 140.0
        )
        val key = "$fromCurrency-$toCurrency"
        val rate = exchangeRates[key] ?: 1.0

        return value * rate
    }
}