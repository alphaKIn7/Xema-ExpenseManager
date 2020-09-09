package com.example.xema_expensemanagerapp.ui

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.databinding.FragmentTransactionsViewAllExpandedBinding
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class TransactionsViewAllExpanded : Fragment() {
    private lateinit var binding : FragmentTransactionsViewAllExpandedBinding
    private lateinit var viewModel : TransactionsViewAllViewModel
    private lateinit var homeScreenViewModel : HomeScreenViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_view_all_expanded,container,false)
        viewModel = ViewModelProvider(this).get(TransactionsViewAllViewModel::class.java)
        homeScreenViewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val month = TransactionsViewAllExpandedArgs.fromBundle(requireArguments()).month
        val year = TransactionsViewAllExpandedArgs.fromBundle(requireArguments()).year
        viewModel.setMonth(month)
        viewModel.setYear(year)
        when(month){
            1-> binding.dateHeader.text ="January, ${year}"
            2-> binding.dateHeader.text ="February, ${year}"
            3-> binding.dateHeader.text ="March, ${year}"
            4-> binding.dateHeader.text ="April, ${year}"
            5-> binding.dateHeader.text ="May, ${year}"
            6-> binding.dateHeader.text ="June, ${year}"
            7-> binding.dateHeader.text ="July, ${year}"
            8-> binding.dateHeader.text ="August, ${year}"
            9-> binding.dateHeader.text ="September, ${year}"
            10-> binding.dateHeader.text ="October, ${year}"
            11-> binding.dateHeader.text ="November, ${year}"
            12-> binding.dateHeader.text="December, ${year}"
        }

        with(binding.monthWiseList){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionsAdapter{
                findNavController().navigate(
                   TransactionsViewAllExpandedDirections.actionTransactionsViewAllExpandedToAddTransactionFragment(it)
                )
            }
        }
        viewModel.transactionsListByMonthAndYear.observe(viewLifecycleOwner, Observer {
            (binding.monthWiseList.adapter as TransactionsAdapter).submitList(it)
        })

        binding.backButton5.setOnClickListener{
            requireActivity().onBackPressed()
        }

        val itemTouchHelper2 = ItemTouchHelper(itemTouchHelperCallBack2)
        itemTouchHelper2.attachToRecyclerView(binding.monthWiseList)
    }
    private val itemTouchHelperCallBack2 = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            val deleteTransaction =  (binding.monthWiseList.adapter as TransactionsAdapter).getTransactionAt(position)
            homeScreenViewModel.delete(deleteTransaction)
            Snackbar.make(viewHolder.itemView, "Transaction deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    homeScreenViewModel.insert(deleteTransaction)
                }.show()
        }
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ){
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(Color.parseColor("#B52121"))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }


}