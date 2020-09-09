package com.example.xema_expensemanagerapp.ui

import android.app.Application
import androidx.lifecycle.*

import com.example.xema_expensemanagerapp.data.TransactionListRepository
import com.example.xema_expensemanagerapp.data.Transactions
import com.example.xema_expensemanagerapp.data.Type

class TransactionsViewAllViewModel(application: Application) : AndroidViewModel(application) {
    private val repo : TransactionListRepository = TransactionListRepository(application)
    private val _month = MutableLiveData<Int>()
    val month : LiveData<Int>
        get() = _month

    private val _year = MutableLiveData<Int>()
    val year : LiveData<Int>
        get() = _year

//Custom Livedata
    private val combinedValues = MediatorLiveData<Pair<Int?, Int?>>().apply {
        addSource(month) {
            value = Pair(it, year.value)
        }
        addSource(year) {
            value = Pair(month.value, it)
        }
    }

    fun setMonth(month:Int){
        _month.value =  month
    }
    fun setYear(year : Int){
        _year.value = year
    }
    val expensesByMonth = repo.getAmountByMonthAndYear("expense", Type.Transaction.name)

    val transactionsListByMonthAndYear = Transformations.switchMap(combinedValues) {pair->
        repo.getListByMonthAndYear(pair.first ?: 0, pair.second?:0, Type.Transaction.name)
    }

    val fullUpcomingList : LiveData<List<Transactions>> = repo.getUpcomingList(Type.UpcomingTransaction.name)
}