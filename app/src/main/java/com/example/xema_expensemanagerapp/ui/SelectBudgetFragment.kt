package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.databinding.SelectBudgetFragmentBinding

//Onboarding screen of app
class SelectBudgetFragment : Fragment() {

    companion object {

        const val SAVED_NAME = "SAVED NAME"
        const val SAVED_INCOME = "SAVED INCOME"
        const val SAVED_BUDGET = "SAVED BUDGET"
        const val FIRST_TIME = "FIRST TIME"
    }

    private lateinit var viewModel: SelectBudgetViewModel
    private lateinit var binding: SelectBudgetFragmentBinding
    private lateinit var  sharedPref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val firstTime = sharedPref.getBoolean(FIRST_TIME,true)
        Log.d("SelectFragment", "firstTime is $firstTime")
        if(!firstTime){
            findNavController().navigate(SelectBudgetFragmentDirections.actionSelectBudgetFragmentToHomeScreenFragment())
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.select_budget_fragment,container,false)
        viewModel = ViewModelProvider(this).get(SelectBudgetViewModel::class.java)
        binding.selectBudgetViewModel = viewModel
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        binding.continueButton.isEnabled = false

        binding.editUserName.addTextChangedListener(userNameTextWatcher)
        viewModel.userName.observe(viewLifecycleOwner, Observer {
            with(sharedPref.edit()) {
                putString(SAVED_NAME, viewModel.userName.value)
                apply()
            }

        })

        binding.editIncome.addTextChangedListener(incomeTextWatcher)
        viewModel.income.observe(viewLifecycleOwner, Observer {
            with(sharedPref.edit()) {
                putString(SAVED_INCOME, viewModel.income.value)
                apply()
            }
        })
        binding.editMonthlyBudget.addTextChangedListener(budgetTextWatcher)
        viewModel.budget.observe(viewLifecycleOwner, Observer {
            with(sharedPref.edit()) {
                putString(SAVED_BUDGET, viewModel.budget.value)
                apply()
            }
        })

        binding.continueButton.setOnClickListener{
            findNavController().navigate(SelectBudgetFragmentDirections.actionSelectBudgetFragmentToHomeScreenFragment())
        }

        with(sharedPref.edit()){
            putBoolean(FIRST_TIME,false)
            apply()
        }

    }


    private val userNameTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.setUserName(s.toString())
            if(binding.editUserName.text.toString().isNotEmpty() ){
                binding.continueButton.isEnabled = true
            }else{
                binding.editUserNameField.error = "Please enter name"
                binding.continueButton.isEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    private val incomeTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.setIncome(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
    private val budgetTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.setBudget(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

}