package com.example.kotlinroomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinroomdemo.databinding.ListItemBinding
import com.example.kotlinroomdemo.db.Customer

class CustomerRecyclerViewAdapter(private val clickEvent: (Customer) -> Unit): RecyclerView.Adapter<CustomerViewHolder>() {

    private lateinit var binding: ListItemBinding

    private val customersList = ArrayList<Customer>()

    /**
     * As the name suggests what should happen when the below ViewHolder is created,
     * for reference, in an activity we start by modifying the onCreate method first,
     * we provide the setContentView and also the binding variable.
     * similarly we want to provide the binding variable for the custom view of the list;
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customersList.size
    }

    /**
     * This function is used to display data on the list item.
     */
    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customersList[position], clickEvent)
    }

    fun setList(customers: List<Customer>) {
        customersList.clear()
        customersList.addAll(customers)
    }

}

class CustomerViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(customer: Customer, clickEvent: (Customer) -> Unit) {
        binding.tvIdNumber.text = customer.id.toString()
        binding.tvName.text = customer.name
        binding.tvLastName.text = customer.lastname
        binding.tvEmail.text = customer.email

        binding.cvCard.setOnClickListener {
            clickEvent(customer)
        }
    }

}