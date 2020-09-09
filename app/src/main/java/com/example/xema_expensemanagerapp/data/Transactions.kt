package com.example.xema_expensemanagerapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//save budget and name of user using shared preference
//enum class KindOfTransaction{
//    Expense, Income
//}
//enum class Source{
//    Cash, DebitCard, CreditCard
//}
enum class Category{
    Bill, EMI, Entertainment, Food, Fuel, Groceries, Health,
    Investment, Other, Shopping, Transfer, Travel
}
enum class Type {
    Transaction,
    UpcomingTransaction
}

data class TransactionsByMonth (
    @ColumnInfo(name = "month")
    var month : Int,

    @ColumnInfo(name = "year")
    var year: Int,

    @ColumnInfo(name = "SUM(amount)")
    var amount: Double

)

@Entity(tableName = "transactions_table")
data class Transactions(@PrimaryKey(autoGenerate = true) val transId : Long,
                        var type : String,
                        val nameOfTrans : String,
                        @ColumnInfo(name = "amount")
                        val amount : Double,
                        val date : String,
                        val transKind : String,
                        val comments : String,
                        val category : String,
                        val source: String,
                        val day : Int,
                        @ColumnInfo(name = "month")
                        val month : Int,
                        @ColumnInfo(name = "year")
                        val year : Int)
//                        val isRecurring: IsRecurring,
//                        val fromDate : String,
//                        val toDate : String)

