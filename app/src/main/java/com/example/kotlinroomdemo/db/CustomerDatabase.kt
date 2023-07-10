package com.example.kotlinroomdemo.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class CustomerDatabase : RoomDatabase() {

    abstract val customerDAO: CustomerDAO

    companion object {

        @Volatile
        private var DBINSTANCE: CustomerDatabase? = null

        fun getInstance(context: Context): CustomerDatabase {
            synchronized(this) {
                var instance = DBINSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CustomerDatabase::class.java,
                        "customer_data_database"
                    ).build()
                    DBINSTANCE = instance
                }
                return instance
            }
        }

    }

}