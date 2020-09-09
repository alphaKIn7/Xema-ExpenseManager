package com.example.xema_expensemanagerapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.xema_expensemanagerapp.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Year

class AddTransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TransactionDetailRepository =
        TransactionDetailRepository(application)

    private val _type = MutableLiveData<String>()
    val type : LiveData<String>
        get() = _type

    private val _transId = MutableLiveData<Long>(0)
    val transId : LiveData<Long>
        get() = _transId


    val transaction : LiveData<Transactions> = Transformations.
            switchMap(_transId) {id->
                repo.getTransaction(id)
            }

    fun setTransId (id : Long){
        if(_transId.value != id){
            _transId.value = id
        }
    }
    fun saveTransaction(transaction: Transactions){
        viewModelScope.launch {
            if(_transId.value == 0L){
                repo.insertTransaction(transaction)
            }else{
                repo.updateTransaction(transaction)
            }
        }
    }
    fun deleteTransaction(){
        viewModelScope.launch {
            transaction.value?.let{repo.deleteTransaction(it)}
        }
    }

}