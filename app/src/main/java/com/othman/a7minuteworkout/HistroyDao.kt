package com.othman.a7minuteworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
     @Insert
     suspend fun insert(historyEntity: HistoryEntity)

     @Update
     suspend fun update(historyEntity: HistoryEntity)

     @Delete
     suspend fun delete(historyEntity: HistoryEntity)

     @Query("Select * from `history-table`")
     fun fetchAllDates(): Flow<List<HistoryEntity>>


}