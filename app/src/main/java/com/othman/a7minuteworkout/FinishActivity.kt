package com.othman.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.othman.a7minuteworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding: ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbFinish)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbFinish?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener{
            finish()
        }
        val historyDao = (application as WorkoutApp).db.historyDao()
        addDateToDatabase(historyDao)


    }

    private fun addDateToDatabase(historyDao: HistoryDao) {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss aaa", Locale.getDefault())
        val dateTime = c.time
        val date = sdf.format(dateTime)
        lifecycleScope.launch{
            historyDao.insert(HistoryEntity(date=date))
        }
    }

}