package com.example.xema_expensemanagerapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Transaction

class TransactionDetailRepository(context : Application) {
    private val transactionDetailDao : TransactionDetailDao = TransactionDatabase.getDatabase(context).transactionDetailDao()

    fun getTransaction(id:Long) : LiveData<Transactions> {
        return transactionDetailDao.getTransaction(id)
    }

    suspend fun insertTransaction(transaction : Transactions) : Long {
        return  transactionDetailDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction : Transactions){
        transactionDetailDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction : Transactions) {
        transactionDetailDao.deleteTransaction(transaction)
    }
}

