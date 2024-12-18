package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var isRunning = false
    private var hours: Int = 0
    private var minutes: Int = 0
    private var seconds: Int = 0
    private var countDownTimer: CountDownTimer? = null
    private var totalMillis: Long = 0
    private val lapsList = ArrayList<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listview.adapter = arrayAdapter

        binding.lap.setOnClickListener {
            addLap()
        }

        binding.clock.setOnClickListener {
            showTimePickerDialog()
        }

        binding.run.setOnClickListener {
            if (isRunning) {
                stopChronometer()
            } else {
                startChronometer()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addLap() {
        val lapTime = binding.chronometr.text.toString()
        lapsList.add(lapTime)
        arrayAdapter.notifyDataSetChanged()
        resetChronometer()
    }

    private fun showTimePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        val numberPickerHours = dialog.findViewById<NumberPicker>(R.id.numberPicker_hours)
        val numberPickerMinutes = dialog.findViewById<NumberPicker>(R.id.numberPicker_minutes)
        val numberPickerSeconds = dialog.findViewById<NumberPicker>(R.id.numberPicker_seconds)

        numberPickerHours.minValue = 0
        numberPickerHours.maxValue = 23

        numberPickerMinutes.minValue = 0
        numberPickerMinutes.maxValue = 59

        numberPickerSeconds.minValue = 0
        numberPickerSeconds.maxValue = 59

        dialog.findViewById<Button>(R.id.set_time).setOnClickListener {
            hours = numberPickerHours.value
            minutes = numberPickerMinutes.value
            seconds = numberPickerSeconds.value
            binding.clocktime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun startChronometer() {
        totalMillis = ((hours * 3600 + minutes * 60 + seconds) * 1000).toLong()
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000) / 3600
                val minutes = ((millisUntilFinished / 1000) % 3600) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.chronometr.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                binding.chronometr.text = "00:00:00"
                isRunning = false
                binding.run.text = "Run"
            }
        }.start()
        isRunning = true
        binding.run.text = "Stop"
    }

    private fun stopChronometer() {
        countDownTimer?.cancel()
        isRunning = false
        binding.run.text = "Run"
    }

    private fun resetChronometer() {
        stopChronometer()
        binding.chronometr.text = "00:00:00"
        hours = 0
        minutes = 0
        seconds = 0
        binding.clocktime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Save the state of lapsList when the activity is recreated
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("lapsList", lapsList)
    }

    // Restore the state of lapsList when the activity is recreated
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredLaps = savedInstanceState.getStringArrayList("lapsList")
        restoredLaps?.let {
            lapsList.clear()
            lapsList.addAll(it)
            arrayAdapter.notifyDataSetChanged()
        }
    }
}
