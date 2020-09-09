package com.example.xema_expensemanagerapp.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TransactionDetailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction : Transactions) : Long

    @Update
    suspend fun updateTransaction(transaction: Transactions)

    @Delete
    suspend fun deleteTransaction(transaction: Transactions)

    @Query("SELECT * FROM transactions_table WHERE `transId` = :id ")
    fun getTransaction(id : Long) : LiveData<Transactions>

}
