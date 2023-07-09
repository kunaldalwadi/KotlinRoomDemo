package com.example.kotlinroomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomerDAO {

    @Insert
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer): Int

    @Delete
    suspend fun deleteCustomer(customer: Customer): Int

    @Query("DELETE FROM CUSTOMER_DATA_TABLE")
    suspend fun deleteAllRows(): Int


    /**
     * if you choose the return type to be of LiveData, you do not need to use suspend keyword,
     * because it is of type LiveData, Room db itself runs that operation on a background thread.
     * hence we do not have to use coroutine or asynctask or executor, it is managed by Room itself.
     */
    @Query("SELECT * FROM CUSTOMER_DATA_TABLE")
    fun getAllCustomers():LiveData<List<Customer>>

}