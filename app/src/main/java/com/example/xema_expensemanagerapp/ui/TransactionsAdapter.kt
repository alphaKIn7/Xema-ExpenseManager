package com.example.xema_expensemanagerapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.data.Transactions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*


class TransactionsAdapter(private val listener: (Long) -> Unit) :
    ListAdapter<Transactions, TransactionsAdapter.ViewHolder>(TransactionsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }
    fun getTransactionAt(position: Int) : Transactions{
        return getItem(position)
    }
    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            itemView.setOnClickListener{
                listener.invoke(getItem(adapterPosition).transId)
            }
        }
        fun bind(item : Transactions){
            when(item.category){
                "Bill" -> leading_icon.setImageResource(R.drawable.bill)
                "EMI" -> leading_icon.setImageResource(R.drawable.emi)
                "Entertainment" -> leading_icon.setImageResource(R.drawable.entertainment)
                "Food" ->leading_icon.setImageResource(R.drawable.food)
                "Fuel"-> leading_icon.setImageResource(R.drawable.fuel)
                "Groceries" ->leading_icon.setImageResource(R.drawable.groceries)
                "Health" -> leading_icon.setImageResource(R.drawable.health)
                "Investment" -> leading_icon.setImageResource(R.drawable.investment)
                "Other" -> leading_icon.setImageResource(R.drawable.other)
                "Shopping" ->leading_icon.setImageResource(R.drawable.shopping)
                "Transfer" -> leading_icon.setImageResource(R.drawable.transfer)
                "Travel" -> leading_icon.setImageResource(R.drawable.travel)

            }
            item_name.text = item.nameOfTrans
            if(item.transKind == "income"){
                item_price.setTextColor(Color.parseColor("#158D29"))
                item_price.text = "+₹${item.amount.toString()}"
            }else{
                item_price.setTextColor(Color.parseColor("#C24B12"))
                item_price.text = "-₹${item.amount.toString()}"
            }
            item_date.text = item.date
        }
    }

}

class TransactionsDiffCallback : DiffUtil.ItemCallback<Transactions>(){
    override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions): Boolean {
        return oldItem.transId == newItem.transId
    }
    override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions): Boolean {
        return oldItem == newItem
    }
}