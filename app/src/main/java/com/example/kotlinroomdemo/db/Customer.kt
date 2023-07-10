package com.example.kotlinroomdemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_data_table")
data class Customer(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    var id: Int,

    @ColumnInfo(name = "customer_name")
    var name: String,

    @ColumnInfo(name = "customer_last_name", defaultValue = "No Last Name")
    var lastname: String,

    @ColumnInfo(name = "customer_email")
    var email: String,

    //Here providing String? we tell that null values are acceptable and hence database
    //puts null to all the previous values.
    @ColumnInfo(name = "customer_course")
    var course: String?
)