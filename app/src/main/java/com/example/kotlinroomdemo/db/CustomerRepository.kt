package com.example.kotlinroomdemo.db

/**
 * Repository is the mediator between ViewModel and any kind of data.
 *
 * MVVM pattern involves adding repository as an abstraction layer.
 *
 * Here we are adding dao as the constructor parameter because we are
 * going to call into all dao methods/functions from here, this is just a mediator.
 */
class CustomerRepository(private val dao: CustomerDAO) {

    val customers = dao.getAllCustomers()

    suspend fun insertCustomer(customer: Customer): Long {
        return dao.insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer): Int {
        return dao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer): Int {
        return dao.deleteCustomer(customer)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAllRows()
    }

}