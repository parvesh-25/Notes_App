package com.altaf.notesapp.ui.add

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.altaf.notesapp.MainActivity
import com.altaf.notesapp.R
import com.altaf.notesapp.data.NotesViewModel
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.data.entity.Priority
import com.altaf.notesapp.databinding.FragmentAddBinding
import com.altaf.notesapp.ui.ViewModelFactory
import com.altaf.notesapp.utils.ExtensionsFunctions.setActionBar
import com.altaf.notesapp.utils.HelperFunctions
import com.altaf.notesapp.utils.HelperFunctions.parsetoPriority
import java.text.SimpleDateFormat
import java.util.*


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding

    // supaya manggil database dari NotesViewModel
    private val addViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)



        binding.toolbarAdd.setActionBar(requireActivity())
        // change custome back arrow

        // memanggil spinner yg dimana akan berubah ketika dipilih
        // fungsi dari HelperFunctions akan menjadi value di spinnerPriorities
        // dia manggil fungsi spinnerListener dari Helper.Functions dan mengisi param nya dengan context dan cardview dgn id priorityIndicator di fragment_add
        binding.spinnerPriorities.onItemSelectedListener = HelperFunctions.spinnerListener(context, binding.priorityIndicator)
        // ketika spinnerPriorities diklik maka akan menjalankan fungsi dari HelperFunctions
        // context untuk berhubugan lgsg dengan view

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        // objek AppBarConfiguration untuk mengelola perilaku tombol Navigation di sudut kiri atas bagian tampilan aplikasi
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
        // memanggil item menu_save
        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener{
            insertNotes()
        }
    }

    private fun insertNotes() {
        binding.apply {
            val title = edtTitle.text.toString()
            val priority = spinnerPriorities.selectedItem.toString()
            val desc = edtDescription.text.toString()
            // mendapatkan waktu dan tanggal notes dibuat
            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar)



            val note = Notes(
                0,
                title,
                parsetoPriority(priority,context),
                desc,
                date
            )
            if(edtTitle.text.isEmpty() || edtDescription.text.isEmpty()){
                edtTitle.error = "Please fill the fields"
                edtDescription.error = "Please fill the fields"

            }else{
                addViewModel.insertData(note)
                Toast.makeText(context, "succesful add note", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
            }
        }
    }

    // fungsi ini utk ngubah data priority di atas yg menggunakan tipe data String menjadi tipe data Priority (enum class)


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}