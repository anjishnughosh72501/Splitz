package com.paisede.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.paisede.app.data.db.dao.ExpenseActionDao
import com.paisede.app.data.db.dao.ExpenseDao
import com.paisede.app.data.db.dao.GroupDao
import com.paisede.app.data.db.dao.MemberDao
import com.paisede.app.data.db.dao.SettlementDao
import com.paisede.app.data.db.entities.ExpenseActionEntity
import com.paisede.app.data.db.entities.ExpenseEntity
import com.paisede.app.data.db.entities.ExpenseSplitEntity
import com.paisede.app.data.db.entities.GroupEntity
import com.paisede.app.data.db.entities.MemberEntity
import com.paisede.app.data.db.entities.SettlementEntity

@Database(
    entities = [
        GroupEntity::class,
        MemberEntity::class,
        ExpenseEntity::class,
        ExpenseSplitEntity::class,
        SettlementEntity::class,
        ExpenseActionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao
    abstract fun memberDao(): MemberDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun settlementDao(): SettlementDao
    abstract fun expenseActionDao(): ExpenseActionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paisede_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
