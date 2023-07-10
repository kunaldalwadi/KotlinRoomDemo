package com.example.kotlinroomdemo.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(entities = [Customer::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = CustomerDatabase.Migration3To4::class)
    ]
)
abstract class CustomerDatabase : RoomDatabase() {

    abstract val customerDAO: CustomerDAO
    @RenameColumn(tableName = "customer_data_table", fromColumnName = "customer_name", toColumnName = "customer_first_name")
    class Migration3To4 : AutoMigrationSpec

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