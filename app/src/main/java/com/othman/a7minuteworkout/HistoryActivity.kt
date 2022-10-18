package com.othman.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.othman.a7minuteworkout.databinding.ActivityHistoryBinding import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbHistoryActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title ="HISTORY"
        }
        binding?.tbHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        val historyDao = (application as WorkoutApp).db.historyDao()
        lifecycleScope.launch{
            historyDao.fetchAllDates().collect{
               val historyList = ArrayList(it)
               setupListOfDataIntoRecyclerView(historyList)
            }
        }

    }

    private fun setupListOfDataIntoRecyclerView(historyList: ArrayList<HistoryEntity>) {
        if (historyList.isNotEmpty()) {
            val historyAdapter = HistoryAdapter(historyList)
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
            binding?.rvHistory?.adapter = historyAdapter
            binding?.rvHistory?.visibility = View.VISIBLE
            binding?.tvHistory?.visibility = View.VISIBLE
            binding?.tvNoDataAvailable?.visibility = View.INVISIBLE
        }else{
            binding?.rvHistory?.visibility = View.INVISIBLE
            binding?.tvNoDataAvailable?.visibility = View.VISIBLE
            binding?.tvHistory?.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}