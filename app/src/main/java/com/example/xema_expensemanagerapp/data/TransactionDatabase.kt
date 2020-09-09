package com.example.xema_expensemanagerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transactions::class/*, UpcomingTransactions::class*/], version = 8, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase(){
    abstract fun transactionDetailDao() : TransactionDetailDao
    abstract fun transactionListDao() : TransactionListDao
    companion object {
        @Volatile
        private var instance: TransactionDatabase? = null
        fun getDatabase(context: Context) = instance
            ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}