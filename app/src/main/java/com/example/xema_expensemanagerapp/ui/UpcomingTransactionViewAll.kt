package com.example.xema_expensemanagerapp.ui

import android.graphics.Canvas
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.data.Type
import com.example.xema_expensemanagerapp.databinding.UpcomingTransactionViewAllFragmentBinding
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


//To view all the upcoming transactions
class UpcomingTransactionViewAll : Fragment() {

    private lateinit var viewModel: TransactionsViewAllViewModel
    private lateinit var binding  : UpcomingTransactionViewAllFragmentBinding
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.upcoming_transaction_view_all_fragment, container,
            false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransactionsViewAllViewModel::class.java)
        homeScreenViewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)

        with(binding.fullUpTransList){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionsAdapter{
                findNavController().navigate(UpcomingTransactionViewAllDirections.actionUpcomingTransactionViewAllToAddTransactionFragment(it))
            }
        }

        viewModel.fullUpcomingList.observe(viewLifecycleOwner, Observer {
            (binding.fullUpTransList.adapter as TransactionsAdapter).submitList(it)
            if(it.isEmpty()){
                binding.upViewAllImage.visibility =View.VISIBLE
                binding.addUpMessageLabel.visibility = View.VISIBLE
            }else{
                binding.upViewAllImage.visibility =View.INVISIBLE
                binding.addUpMessageLabel.visibility = View.INVISIBLE
            }
            //Log.d("UpcomingTransaction", "list is $it")
        })

        binding.backButton6.setOnClickListener{
            requireActivity().onBackPressed()
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.fullUpTransList)

    }
    private val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ){

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            val deleteTransaction =  (binding.fullUpTransList.adapter as TransactionsAdapter).getTransactionAt(position)
            when(direction){
                ItemTouchHelper.LEFT->{homeScreenViewModel.delete(deleteTransaction)
                    Snackbar.make(viewHolder.itemView, "Transaction deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            homeScreenViewModel.insert(deleteTransaction)
                        }.show()
                }

                ItemTouchHelper.RIGHT-> {
                    deleteTransaction.type = Type.Transaction.name
                    homeScreenViewModel.update(deleteTransaction)
                    Snackbar.make(viewHolder.itemView, "Added to Transaction", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            deleteTransaction.type = Type.UpcomingTransaction.name
                            homeScreenViewModel.update(deleteTransaction)
                        }.show()
                }
            }
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
                .addSwipeRightBackgroundColor(Color.parseColor("#18835C"))
                .addSwipeRightLabel("Add to Transactions")
                .setSwipeRightLabelColor(Color.parseColor("#ffffff"))
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}