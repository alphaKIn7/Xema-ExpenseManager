package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.data.Transactions
import com.example.xema_expensemanagerapp.data.TransactionsByMonth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.add_transaction_fragment.*
import kotlinx.android.synthetic.main.card_list_item.*
import kotlinx.android.synthetic.main.list_item.*

class TransactionsViewAllAdapter (val budget: Int, private val listener: (List<Int>) -> Unit):
    ListAdapter<TransactionsByMonth, TransactionsViewAllAdapter.ViewHolder>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.card_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer{
        init {
            itemView.setOnClickListener{
                listener.invoke(listOf(getItem(adapterPosition).month, getItem(adapterPosition).year))
            }
        }

        fun bind(item : TransactionsByMonth){
            when(item.month){
                1-> month_name.text ="January, ${item.year}"
                2-> month_name.text ="February, ${item.year}"
                3-> month_name.text ="March, ${item.year}"
                4-> month_name.text ="April, ${item.year}"
                5-> month_name.text ="May, ${item.year}"
                6-> month_name.text ="June, ${item.year}"
                7-> month_name.text ="July, ${item.year}"
                8-> month_name.text ="August, ${item.year}"
                9-> month_name.text ="September, ${item.year}"
                10-> month_name.text ="October, ${item.year}"
                11-> month_name.text ="November, ${item.year}"
                12->  month_name.text="December, ${item.year}"
            }
            spends_label.text = "${item.amount}"
            if(item.amount > budget){
                info_budget.setTextColor(Color.parseColor("#C24B12"))
                info_budget.text = "Budget Exceeded"
                indicator.setImageResource(R.drawable.card_stroke_red)
            }else{
                info_budget.setTextColor(Color.parseColor("#4EC54F"))
                info_budget.text = "In the Limit"
                indicator.setImageResource(R.drawable.card_stroke_green)
            }

        }

    }
}

class DiffCallBack : DiffUtil.ItemCallback<TransactionsByMonth>(){
    override fun areItemsTheSame(oldItem: TransactionsByMonth, newItem: TransactionsByMonth): Boolean {
        return oldItem.year == newItem.year
    }

    override fun areContentsTheSame(oldItem: TransactionsByMonth, newItem: TransactionsByMonth): Boolean {
        return oldItem == newItem
    }
}

