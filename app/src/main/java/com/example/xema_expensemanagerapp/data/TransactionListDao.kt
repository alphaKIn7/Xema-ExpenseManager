package com.example.xema_expensemanagerapp.data

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Query


@Dao
interface TransactionListDao {



    @Query("Select * from transactions_table where type = :type order by year desc,month desc, day desc  Limit 5")
    fun getTransactionList(type: String):LiveData<List<Transactions>>

    @Query("Select * from transactions_table")
    fun getAllTransactions():LiveData<List<Transactions>>

    @Query("Select * from transactions_table where type = :type order by year desc,month desc, day desc")
    fun getUpcomingList(type: String)  : LiveData<List<Transactions>>

    
    @Query("Select month, year, SUM(amount)from transactions_table where transKind = :transKind and type = :type group by year,month order by year desc,month desc, day desc")
    fun getAmountByMonthAndYear(transKind: String,type: String) : LiveData<List<TransactionsByMonth>>

    @Query("Select * from transactions_table where month = :month and year = :year and type =:type order by year desc,month desc, day desc")
    fun getListByMonthAndYear(month : Int , year: Int, type: String) : LiveData<List<Transactions>>

    @Query("Select SUM(amount) from transactions_table where month = :month and year = :year and type = :type and transKind = :transKind")
    fun getLatestMonthAmount(month : Int, year: Int, type: String, transKind: String) : LiveData<Double>

    @Query("Delete from transactions_table")
    fun clear()

}
//order by year desc,month desc, day desc