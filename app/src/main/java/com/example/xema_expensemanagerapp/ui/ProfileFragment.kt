package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.databinding.ProfileFragmentBinding
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_BUDGET
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_INCOME
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_NAME


class ProfileFragment : Fragment() {


    private lateinit var viewModel: SelectBudgetViewModel
    private lateinit var binding : ProfileFragmentBinding
    private lateinit var name : String
    private lateinit var budget: String
    private lateinit var income: String
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectBudgetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.profile_fragment,container,false)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        name = sharedPref.getString(SAVED_NAME,"").toString()
        income = sharedPref.getString(SAVED_INCOME,"").toString()
        budget = sharedPref.getString(SAVED_BUDGET,"").toString()
        binding.profileName.setText(name)
        binding.profileBudget.setText(budget)
        binding.profileIncome.setText(income)
        binding.saveButton.visibility = View.INVISIBLE
        enableViews(false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.backButton2.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.editButton2.setOnClickListener {
            binding.saveButton.visibility = View.VISIBLE
            enableViews(true)
        }
        binding.profileName.addTextChangedListener(userNameTextWatcher)
        binding.profileIncome.addTextChangedListener(incomeTextWatcher)
        binding.profileBudget.addTextChangedListener(budgetTextWatcher)

        binding.saveButton.setOnClickListener {
            binding.profileName.addTextChangedListener(userNameTextWatcher)
            viewModel.userName.observe(viewLifecycleOwner, Observer {
                with(sharedPref.edit()) {
                    putString(SAVED_NAME, viewModel.userName.value)
                    apply()
                }
            })


            viewModel.income.observe(viewLifecycleOwner, Observer {
                with(sharedPref.edit()) {
                    putString(SAVED_INCOME, viewModel.income.value)
                    apply()
                }
            })

            viewModel.budget.observe(viewLifecycleOwner, Observer {
                with(sharedPref.edit()) {
                    putString(SAVED_BUDGET, viewModel.budget.value)
                    apply()
                }
            })
            binding.saveButton.visibility = View.INVISIBLE
            enableViews(false)
        }

    }

    private val userNameTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.setUserName(s.toString())
            if (binding.profileName.text.toString().isEmpty()) {
                binding.profileNameField.error = "Please enter name"
                binding.saveButton.isEnabled = false
            }else{
                binding.profileNameField.error = null
                binding.saveButton.isEnabled = true
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
    private fun enableViews(value : Boolean){
        binding.profileName.isEnabled = value
        binding.profileBudget.isEnabled = value
        binding.profileIncome.isEnabled = value
    }
}