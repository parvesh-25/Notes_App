package com.altaf.notesapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.data.room.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    // view model
    private val notesDao = NotesDatabase.getDatabase(application).notesDao()
    // variabel ini untuk berkomunikasi antar repository dgn view model,
    private val repository: NotesRepository = NotesRepository(notesDao)

    // ngambil semua data
    fun getAllData() : LiveData<List<Notes>> = repository.getAllData()
    // untuk ngambil data dari notesRepository

    // fungsi ini yang akan digunakan oleh view ketika input data dan dikirimkan ke dalam repository
    // masukkan data ke database
    fun insertData(notes: Notes){
        // viewModel scope adalah sebuah coroutines, IO supaya bekerja dibelakang layar, sehingga hp kita tidak ngefreeze saat penginputan layar
        viewModelScope.launch(Dispatchers.IO){
            repository.insetNotes(notes)
        }
    }

    fun sortByHighPriority(): LiveData<List<Notes>> = repository.sortByHighPriority()
    fun sortByLowPriority(): LiveData<List<Notes>> = repository.sortByLowPriority()

    fun deleteAllData () {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllData()
        }
    }

    fun searchByQuery(query : String) : LiveData<List<Notes>> {
        return repository.searchByQuery(query)
    }

    fun deleteNote(notes: Notes){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNote(notes)
        }
    }

    fun updateNote(notes: Notes){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateNote(notes)
        }
    }
}