package com.example.kotlinroomdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinroomdemo.databinding.MainActivityBinding
import com.example.kotlinroomdemo.db.Customer
import com.example.kotlinroomdemo.db.CustomerDatabase
import com.example.kotlinroomdemo.db.CustomerRepository


private val TAG = MainActivity::class.java.simpleName


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up binding for activity
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Now we want viewModel instance here which is CustomerViewModel for us.
         * CustomerViewModel can only be created by CustomerViewModelFactory,
         * CustomerViewModelFactory needs CustomerRepository object in constructor,
         * CustomerRepository needs CustomerDAO object in constructor,
         *
         * Hence we need to create customerDAO object first and trickle it up to ViewModel.
         */

        val dao = CustomerDatabase.getInstance(application).customerDAO
        val repository = CustomerRepository(dao)
        val factory = CustomerViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        binding.viewModelxml = viewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        viewModel.toastMessage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * this function initializes the layoutmanager for the recyclerview and displays the data
     */
    private fun initRecyclerView() {
        binding.rvCustomerListview.layoutManager = LinearLayoutManager(this)
        displayAllCustomers()
    }

    fun displayAllCustomers() {
        viewModel.customers.observe(this, Observer {
            Log.i(TAG, "displayAllCustomers: " + it.size)
            Log.i(TAG, "displayAllCustomers: " + it.toString())

            //Using Kotlin Higher Order Function to pass a function as an argument.
            binding.rvCustomerListview.adapter = CustomerRecyclerViewAdapter(it) { selectedCustomer: Customer ->
                clickEventOnRecyclerView(selectedCustomer)
            }
        })
    }

    fun clickEventOnRecyclerView(customer: Customer) {
        Toast.makeText(this, "Clicked name is ${customer.name}", Toast.LENGTH_SHORT).show()

        viewModel.setupUpdateAndDelete(customer)
    }
}