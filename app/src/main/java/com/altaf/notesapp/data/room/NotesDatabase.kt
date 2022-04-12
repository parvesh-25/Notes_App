package com.altaf.notesapp.data.room

import android.content.Context
import androidx.room.*
import com.altaf.notesapp.data.entity.Notes


@Database(entities = [Notes::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object{
        // supaya instance database tidak dibuat berulang kali saat aplikasi di running
        @Volatile
        private var instance: NotesDatabase? = null

        @JvmStatic
        // menggunakan context karena kita berhubungan dengan view
        fun getDatabase(context: Context):NotesDatabase{
            // jika instance = null maka instance akan di synchronized dnegan database builder sehingga tidak null
            if(instance == null){
                synchronized(NotesDatabase::class.java){
                    instance = Room.databaseBuilder(
                        context,
                        NotesDatabase::class.java,
                        "notes.db"
                    // notes.db adalah nama argumen
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance as NotesDatabase
        }
    }
}