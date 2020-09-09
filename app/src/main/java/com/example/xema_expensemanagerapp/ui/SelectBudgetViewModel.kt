package com.example.xema_expensemanagerapp.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectBudgetViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _userName = MutableLiveData<String?>()
    val userName : LiveData<String?>
        get() = _userName

    fun setUserName(userName : String){
        _userName.value = userName
    }

    private val _income = MutableLiveData<String?>()
    val income : LiveData<String?>
        get() = _income

    fun setIncome(income : String){
        _income.value =  income
    }

    private val _budget = MutableLiveData<String?>()
    val budget : LiveData<String?>
        get() = _budget

    fun setBudget(budget : String){
        _budget.value = budget
    }


}