package com.altaf.notesapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.altaf.notesapp.data.entity.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // suspend berguna agar
    suspend fun insertNotes(notes: Notes)

    // NGAMBIL DATA dari yang terupdate
    @Query("SELECT * FROM notes_table")
    fun getAllData() : LiveData<List<Notes>>

    // ngambil seluruh data tapi diurutkan dari yg high KE yg medium, ke yg low , H% arrtinya ngambil data yg dpn nya H, M% = medium
    // High ururtan 1, Medium urutan 2, Low urutan 3, end
    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    // fungsi ini akan mengquery ke database
    fun sortByHighPriority(): LiveData<List<Notes>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<Notes>>

    // fungsi ini untuk menghapus semua yg ada di notes table
    @Query("DELETE FROM notes_table")
    suspend fun deleteAllData()

    @Query("SELECT * FROM notes_table WHERE title LIKE :query")
    fun searchByQuery(query: String) : LiveData<List<Notes>>

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Update
    suspend fun updateNote(notes: Notes)
}