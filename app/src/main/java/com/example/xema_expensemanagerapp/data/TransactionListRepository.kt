package com.example.xema_expensemanagerapp.data

import android.app.Application
import androidx.lifecycle.LiveData

class TransactionListRepository (context: Application) {
    private val transactionListDao: TransactionListDao = TransactionDatabase.getDatabase(context).transactionListDao()

    fun getTransactionList(type:String):LiveData<List<Transactions>>{
        return transactionListDao.getTransactionList(type)
    }
    fun getAllTransactions():LiveData<List<Transactions>>{
        return transactionListDao.getAllTransactions()
    }


    fun getAmountByMonthAndYear(transKind: String, type: String) : LiveData<List<TransactionsByMonth>>{
        return transactionListDao.getAmountByMonthAndYear(transKind,type)
    }

    fun getListByMonthAndYear(month : Int, year : Int, type: String) : LiveData<List<Transactions>>{
        return transactionListDao.getListByMonthAndYear(month,year,type)
    }

    fun getLatestMonthAmount(month: Int,year: Int, type:String,transKind: String) : LiveData<Double>{
        return transactionListDao.getLatestMonthAmount(month,year,type,transKind)
    }

    fun getUpcomingList(type: String) : LiveData<List<Transactions>>{
        return transactionListDao.getUpcomingList(type)
    }

}