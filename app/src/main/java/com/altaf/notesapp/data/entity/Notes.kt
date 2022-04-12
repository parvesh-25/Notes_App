package com.altaf.notesapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

// anotasi entity untuk menandakan bahwa sebuah data class dijadikan sebuah table database
@Entity(tableName = "notes_table") // notes_table adalah nama tabel nya
@Parcelize
data class Notes(
    // untuk id didalam table supaya tidak duplikat
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    // priority didapat dari enum class Priority
    // priority akan di convert krn tipe data nya bukan String
    var priority: Priority,
    var description: String,
    var date: String,
) : Parcelable
