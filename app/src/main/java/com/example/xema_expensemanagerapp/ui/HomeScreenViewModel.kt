package com.example.xema_expensemanagerapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.xema_expensemanagerapp.data.TransactionDetailRepository
import com.example.xema_expensemanagerapp.data.TransactionListRepository
import com.example.xema_expensemanagerapp.data.Transactions
import com.example.xema_expensemanagerapp.data.Type
import kotlinx.coroutines.launch
import java.util.*

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val repo : TransactionListRepository = TransactionListRepository(application)

    private val repo2 : TransactionDetailRepository = TransactionDetailRepository(application)

    fun delete(transaction : Transactions){
        viewModelScope.launch {
            repo2.deleteTransaction(transaction)
        }
    }

    fun insert(transaction: Transactions){
        viewModelScope.launch {
            repo2.insertTransaction(transaction)
        }
    }

    fun update(transaction: Transactions){
        viewModelScope.launch {
            repo2.updateTransaction(transaction)
        }
    }
    private val calendar: Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val upTransactions: LiveData<List<Transactions>> = repo.getTransactionList(Type.UpcomingTransaction.name)
    val transactions : LiveData<List<Transactions>> = repo.getTransactionList(Type.Transaction.name)
    val latestMonthTransactions : LiveData<Double> = repo.getLatestMonthAmount(month,year,Type.Transaction.name, "expense")


    val allTransactions = repo.getAllTransactions()
}