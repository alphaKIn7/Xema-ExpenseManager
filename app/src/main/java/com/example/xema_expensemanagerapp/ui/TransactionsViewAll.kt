package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.databinding.TransactionsViewAllFragmentBinding
import kotlinx.android.synthetic.main.transactions_view_all_fragment.*
import kotlin.math.exp

class TransactionsViewAll : Fragment() {

    companion object {
        fun newInstance() = TransactionsViewAll()
    }

    private lateinit var viewModel: TransactionsViewAllViewModel
    private lateinit var binding : TransactionsViewAllFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransactionsViewAllViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.transactions_view_all_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val budget= sharedPref.getString(SelectBudgetFragment.SAVED_BUDGET,"0")
        var mainBudget : Int = 0
        mainBudget = if(budget == null || budget==""){
            0
        }else{
            budget.toInt()
        }

        with(binding.transactionsViewAllClub){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionsViewAllAdapter(mainBudget){
                findNavController().navigate(TransactionsViewAllDirections.actionTransactionsViewAllToTransactionsViewAllExpanded(it[0],it[1]))
            }
        }

        viewModel.expensesByMonth.observe(viewLifecycleOwner, Observer {expense->
            (binding.transactionsViewAllClub.adapter as TransactionsViewAllAdapter).submitList(expense)

            if(expense.isEmpty()){
                binding.viewAllImage.visibility = View.VISIBLE
                binding.addMessageLabel.visibility = View.VISIBLE
            }else{
                binding.viewAllImage.visibility = View.INVISIBLE
                binding.addMessageLabel.visibility = View.INVISIBLE
            }
        })


        binding.backButton4.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }
}