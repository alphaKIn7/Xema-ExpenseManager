package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.databinding.FragmentPieChartBinding
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_BUDGET
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_INCOME
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_pie_chart.*

class PieChartFragment : Fragment() {
    private lateinit var binding : FragmentPieChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pie_chart, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.backButton7.setOnClickListener{
            requireActivity().onBackPressed()
        }
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val budget= sharedPref.getString(SAVED_BUDGET,"0")
        val income = sharedPref.getString(SAVED_INCOME,"0")
//        Log.d("HomeScreen", "budget is $budget")
        var mainIncome : Float
        mainIncome = if(income == null|| income==""){
            0f
        }else{
            income.toFloat()
        }
        var mainBudget : Float = 0f
        mainBudget = if(budget == null || budget==""){
            0f
        }else{
            //Log.d("HomeScreen", "went in else")
            budget.toFloat()
        }
        Log.d("PieChartFragment", "mainBudget is $mainBudget and mainIncome is $mainIncome")

        var budgetPercentage:Float = ((mainBudget*100)/mainIncome).toFloat()
        Log.d("PieChartFragment", "budget is $budgetPercentage")
        var savingsPercentage:Float = 100f - budgetPercentage

        val savings = mainIncome - mainBudget
        binding.pieChart.setUsePercentValues(true)
        val entries : List<PieEntry> = listOf(PieEntry(budgetPercentage, "Monthly Budget = ₹$mainBudget"),
        PieEntry(savingsPercentage,"Monthly Savings = ₹$savings"))

        val pieDataSet = PieDataSet(entries,"")
        pieDataSet.valueTextSize = 15f
        val description : Description = Description()
        description.text = "Monthly savings pie chart"
        binding.pieChart.description = description
        val pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        pieDataSet?.setColors(*ColorTemplate.JOYFUL_COLORS)
        binding.pieChart.animateXY(800,800)
    }

}