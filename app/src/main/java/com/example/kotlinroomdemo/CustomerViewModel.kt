package com.example.kotlinroomdemo

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinroomdemo.db.Customer
import com.example.kotlinroomdemo.db.CustomerRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputLastName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    private lateinit var customerTobeUpdatedOrDeleted: Customer
    private var isUpdateOrDelete = false

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    /**
     * we dont have to create a special function for get all customers,
     * as we can directly call repository's object
     */
    val customers = repository.customers

    fun saveOrUpdate() {

        //Adding Validations
        if (inputName.value.isNullOrBlank()) {
            statusMessage.value = Event("Enter Name, it can not be blank.")
        } else if (inputLastName.value.isNullOrBlank()) {
            statusMessage.value = Event("Enter Last Name, it can not be blank.")
        } else if (inputEmail.value.isNullOrBlank()) {
            statusMessage.value = Event("Enter Email, it can not be blank.")
        } else if (! Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Enter correct Email address.")
        } else {
            if (isUpdateOrDelete) {
                customerTobeUpdatedOrDeleted.name = inputName.value!!
                customerTobeUpdatedOrDeleted.lastname = inputLastName.value!!
                customerTobeUpdatedOrDeleted.email = inputEmail.value!!
                updateCustomer(customerTobeUpdatedOrDeleted)
            } else {
                val name = inputName.value ?: "Empty"
                val lastname = inputLastName.value ?: "Empty"
                val email = inputEmail.value ?: "Empty"
                insertCustomer(Customer(0, name, lastname, email))
                inputName.value = ""
                inputLastName.value = ""
                inputEmail.value = ""
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteCustomer(customerTobeUpdatedOrDeleted)
        } else {
            deleteAll()
        }
    }

    /**
     * Kotlin also lets us add inline substitution of the below function with its body
     * shown in updateCustomer function
     */
    private fun insertCustomer(customer: Customer) {
        viewModelScope.launch(IO) {
            val insertedRowId = repository.insertCustomer(customer)

            withContext(Main) {
                if (insertedRowId > -1) {
                    statusMessage.value = Event("$insertedRowId Customer inserted successfully")
                } else {
                    statusMessage.value = Event("Error Occured !!")
                }
            }
        }
    }

    private fun updateCustomer(customer: Customer) = viewModelScope.launch(IO) {

        val updatedRowId = repository.updateCustomer(customer)

        withContext(Main) {
            if (updatedRowId > -1) {
                inputEmail.value = ""
                inputName.value = ""
                inputLastName.value = ""

                isUpdateOrDelete = false

                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"

                statusMessage.value = Event("$updatedRowId Customer updated successfully")
            } else {
                statusMessage.value = Event("Error Occured !!")
            }
        }
    }

    private fun deleteCustomer(customer: Customer) {
        viewModelScope.launch(IO) {
            val deletedRowId = repository.deleteCustomer(customer)

            withContext(Main) {
                if (deletedRowId > -1) {
                    inputEmail.value = ""
                    inputName.value = ""
                    inputLastName.value = ""

                    isUpdateOrDelete = false

                    saveOrUpdateButtonText.value = "Save"
                    clearAllOrDeleteButtonText.value = "Clear All"

                    statusMessage.value = Event("$deletedRowId Customer deleted successfully")
                } else {
                    statusMessage.value = Event("Error Occured !!")
                }
            }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(IO) {
            val deletedAllRowId = repository.deleteAll()
            if (deletedAllRowId > -1) {
                statusMessage.value = Event("$deletedAllRowId All Customer Deleted successfully")
            } else {
                statusMessage.value = Event("Error Occured !!")
            }
        }
    }

    fun setupUpdateAndDelete(customer: Customer) {
        inputEmail.value = customer.email
        inputName.value = customer.name
        inputLastName.value = customer.lastname

        customerTobeUpdatedOrDeleted = customer
        isUpdateOrDelete = true

        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    /**
     * New function added to understand how to use flow
     */
    fun getSavedCustomers() = liveData {
        repository.customers.collect {
            emit(it)
        }
    }
}