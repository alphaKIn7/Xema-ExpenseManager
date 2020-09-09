package com.example.xema_expensemanagerapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xema_expensemanagerapp.R
import com.example.xema_expensemanagerapp.data.Type
import com.example.xema_expensemanagerapp.databinding.HomeScreenFragmentBinding
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_BUDGET
import com.example.xema_expensemanagerapp.ui.SelectBudgetFragment.Companion.SAVED_NAME
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.home_screen_fragment.*
import java.util.*


class HomeScreenFragment : Fragment() {

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var binding : HomeScreenFragmentBinding
    private lateinit var addTransactionViewModel: AddTransactionViewModel
    private var sum = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)
        addTransactionViewModel = ViewModelProvider(this).get((AddTransactionViewModel::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_screen_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(binding.transList){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionsAdapter{
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAddTransactionFragment(it)
                )
            }
            setHasFixedSize(true)
        }
        with(binding.uptransList){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionsAdapter{
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAddTransactionFragment(it)
                )
            }
            setHasFixedSize(true)
        }

        binding.addTransactionButton.setOnClickListener{
            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToAddTransactionFragment(0))
        }
        binding.profileButton.setOnClickListener{
            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToProfileFragment())
        }
        binding.viewAll1.setOnClickListener{
            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToTransactionsViewAll())
        }
        binding.viewAll2.setOnClickListener{
            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToUpcomingTransactionViewAll())
        }
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            (binding.transList.adapter as TransactionsAdapter).submitList(it)
            if(it.isEmpty()){
                placeholder_icon.visibility = View.VISIBLE
                add_your_transaction_label.visibility = View.VISIBLE
            }else{
                placeholder_icon.visibility = View.INVISIBLE
                add_your_transaction_label.visibility = View.INVISIBLE

            }
        })
        viewModel.upTransactions.observe(viewLifecycleOwner, Observer {
            (binding.uptransList.adapter as TransactionsAdapter).submitList(it)
            if(it.isEmpty()){
                up_placeholder_icon.visibility = View.VISIBLE
                add_up_trans_label.visibility = View.VISIBLE
            }else{
                up_placeholder_icon.visibility = View.INVISIBLE
                add_up_trans_label.visibility = View.INVISIBLE

            }
        })

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val userName = sharedPref.getString(SAVED_NAME,"")
        binding.userName.text = "Hi, $userName"


        val calendar = Calendar.getInstance()
        var monthText : String = ""
        when(calendar.get(Calendar.MONTH)+1){
            1-> monthText ="January"
            2-> monthText="February"
            3-> monthText ="March"
            4-> monthText ="April"
            5-> monthText ="May"
            6-> monthText ="June"
            7-> monthText ="July"
            8-> monthText ="August"
            9-> monthText ="September"
            10-> monthText ="October"
            11-> monthText ="November"
            12->  monthText="December"
        }


        val budget= sharedPref.getString(SAVED_BUDGET,"0")
        var mainBudget : Int = 0
        mainBudget = if(budget == null || budget==""){
            0
        }else{
            budget.toInt()
        }
        viewModel.latestMonthTransactions.observe(viewLifecycleOwner, Observer {
            if(it == null){
                binding.spend.text = "0"
                binding.monthText.text = "in $monthText"

            }else{
                binding.spend.text = it.toString()
                binding.monthText.text = "in $monthText"
            }
            if(mainBudget == 0){
                Log.d("HomeScreen", "went in if")
                binding.progressBar.progress = mainBudget
                binding.budgetView.text = "Set Budget"
            }else {
                sum = if(it == null ) {
                    0.0
                }else{
                    it!!
                }
                val progress = ((sum /mainBudget) *100).toInt()
                binding.progressBar.progress = progress
                binding.budgetView.text = "${progress}%"
            }
        })

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.uptransList)

        val itemTouchHelper2 = ItemTouchHelper(itemTouchHelperCallBack2)
        itemTouchHelper2.attachToRecyclerView(binding.transList)

        binding.pieIcon.setOnClickListener{
            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToPieChartFragment())
        }
    }

    //For swipe delete and add to transaction
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
            val deleteTransaction =  (binding.uptransList.adapter as TransactionsAdapter).getTransactionAt(position)
            when(direction){
                ItemTouchHelper.LEFT->{viewModel.delete(deleteTransaction)
                    Snackbar.make(viewHolder.itemView, "Transaction deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.insert(deleteTransaction)
                        }.show()
                }

                ItemTouchHelper.RIGHT-> {
                    deleteTransaction.type = Type.Transaction.name
                    viewModel.update(deleteTransaction)
                    Snackbar.make(viewHolder.itemView, "Added to Transaction", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            deleteTransaction.type = Type.UpcomingTransaction.name
                            viewModel.update(deleteTransaction)
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

    //for swipe delete
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
            val deleteTransaction =  (binding.transList.adapter as TransactionsAdapter).getTransactionAt(position)
                viewModel.delete(deleteTransaction)
                    Snackbar.make(viewHolder.itemView, "Transaction deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.insert(deleteTransaction)
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