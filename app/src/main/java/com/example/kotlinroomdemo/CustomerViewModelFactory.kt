package com.example.kotlinroomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinroomdemo.db.CustomerRepository

/**
 * We have to make a viewModel Factory because our viewModel has a constructor parameter
 */
class CustomerViewModelFactory(private val repository: CustomerRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}