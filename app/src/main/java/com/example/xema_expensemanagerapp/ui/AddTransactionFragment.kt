package com.example.xema_expensemanagerapp.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.data.Category
import com.example.xema_expensemanagerapp.data.Transactions
import com.example.xema_expensemanagerapp.data.Type
import com.example.xema_expensemanagerapp.databinding.AddTransactionFragmentBinding
import kotlinx.android.synthetic.main.add_transaction_fragment.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import java.util.*

class AddTransactionFragment: Fragment() {

    private lateinit var viewModel: AddTransactionViewModel
    private lateinit var calendar: Calendar
    private var year = 0
    private var month = 0
    private var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddTransactionFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_transaction_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = AddTransactionFragmentArgs.fromBundle(requireArguments()).id
        viewModel.setTransId(id)



        createCategoryDropDown()
        createSourceDropDown()
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        edit_date.isFocusable = false
        edit_date.setOnClickListener{
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                edit_date.setText("$mDay/${mMonth+1}/$mYear")
            }, year,month,day)
            dpd.show()
        }

        if(id == 0L){
            delete_button.visibility = View.INVISIBLE
            edit_button.visibility = View.INVISIBLE
            enableViews(true)
            income_button.setTextColor(Color.parseColor("#004D7C"))
            header.text = getString(R.string.add_transaction_header)
        }else{
            delete_button.visibility = View.VISIBLE
            edit_button.visibility = View.VISIBLE
            enableViews(false)
            income_button.setTextColor(Color.parseColor("#ffffff"))
            header.text =getString(R.string.transaction_detail_header)
        }

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { setData(it) }
        })

        income_button.setOnClickListener{
            val transType = "income"
            saveTransaction(transType)
        }



        expense_button.setOnClickListener{
            val transType = "expense"
            saveTransaction(transType)
        }


        delete_button.setOnClickListener{
            deleteTransaction()
        }
        back_button.setOnClickListener{
            requireActivity().onBackPressed()

        }
        edit_button.setOnClickListener{
            enableViews(true)
            income_button.setTextColor(Color.parseColor("#004D7C"))
        }
    }

    private fun setData(transaction : Transactions){
        //set data of transaction which is already present in the list
        edit_transaction_name.setText(transaction.nameOfTrans)
        edit_amount.setText(transaction.amount.toString())
        edit_date.setText(transaction.date)
        edit_category.setText(transaction.category)
        edit_source.setText(transaction.source)
        edit_comments.setText(transaction.comments)

    }
    private fun saveTransaction(transType : String){
        val category = edit_category.text.toString()
        val source = edit_source.text.toString()
        val comments = edit_comments.text.toString()
        edit_amount.addTextChangedListener(amountTextWatcher)
        edit_transaction_name.addTextChangedListener(transNameTextWatcher)
        edit_date.addTextChangedListener(dateTextWatcher)

        if(edit_amount.text.toString().isEmpty() ||edit_transaction_name.text.toString().isEmpty()||edit_date.text.toString().isEmpty())
        {
            if(edit_amount.text.toString().isEmpty()){
                amount.error = "Please enter amount"
            }
            if(edit_transaction_name.text.toString().isEmpty()) {
                transaction_name.error = "Please enter transaction name"
            }
            if (edit_date.text.toString().isEmpty()){
                val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_calendar_today_24, null)
                select_date.errorIconDrawable = icon
                select_date.error = "Please select date"
            }
        }else{


            val transName = edit_transaction_name.text.toString()
            val amount = edit_amount.text.toString().toDouble()
            val date = edit_date.text.toString()
            val dateString = date.split("/")
            val mDay = dateString[0].toInt()
            val mMonth = dateString[1].toInt()
            val mYear = dateString[2].toInt()
            Log.d("AddTransaction" , "$mDay $mMonth $mYear")
            val calender = Calendar.getInstance()
            val currentYear = calender.get(Calendar.YEAR)
            val currentMonth = calender.get(Calendar.MONTH) + 1
            val currentDay = calender.get(Calendar.DAY_OF_MONTH)
            Log.d("AddTransaction" , "$currentDay $currentMonth $currentYear")
            val type : String  = if(mYear > currentYear){
                Type.UpcomingTransaction.name
            }else if(mYear>=currentYear && mMonth > currentMonth){
                Type.UpcomingTransaction.name
            }else if(mYear>=currentYear && mMonth >= currentMonth && mDay > currentDay){
                Type.UpcomingTransaction.name
            }else{
                Type.Transaction.name
            }
            val transaction = Transactions(viewModel.transId.value!!,
                type = type,
                nameOfTrans = transName,
                amount =  amount,
                date = date ,
                comments = comments,
                source = source,
                transKind = transType,
                category = category,
                day = mDay,
                month = mMonth,
                year = mYear
            )
            viewModel.saveTransaction(transaction)

            requireActivity().onBackPressed()
        }
    }
    private fun deleteTransaction(){
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_dialog, null)
        val builder = AlertDialog.Builder(context).setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.yes_button.setOnClickListener {
            alertDialog.dismiss()
            viewModel.deleteTransaction()
            requireActivity().onBackPressed()
        }
        dialogView.no_button.setOnClickListener{
            alertDialog.dismiss()
        }
    }
    private fun createCategoryDropDown(){
        val categories = mutableListOf<String>()
        Category.values().forEach { categories.add(it.name )}
        val adapter= ArrayAdapter(requireActivity(), R.layout.category_drop_down,R.id.category_item, categories)
        edit_category.setAdapter(adapter)
        edit_category.setText(categories[8])
    }
    private fun createSourceDropDown(){
        val sources = listOf<String>("Cash","Debit Card", "Credit Card")
        val adapter= ArrayAdapter(requireActivity(), R.layout.category_drop_down,R.id.category_item, sources)
        edit_source.setAdapter(adapter)
    }
    private fun enableViews(value : Boolean){
        edit_date.isEnabled = value
        edit_category.isEnabled = value
        category.isEnabled = value
        source.isEnabled = value
        edit_source.isEnabled = value
        edit_comments.isEnabled = value
        edit_amount.isEnabled = value
        income_button.isEnabled = value
        expense_button.isEnabled = value
        edit_transaction_name.isEnabled = value
    }
    private val amountTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            amount.error = null
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }
    private val transNameTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            transaction_name.error = null
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }
    private val dateTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            select_date.error = null
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

}