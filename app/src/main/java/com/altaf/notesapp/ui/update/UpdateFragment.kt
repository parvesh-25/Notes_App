package com.altaf.notesapp.ui.update

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.altaf.notesapp.R
import com.altaf.notesapp.data.NotesViewModel
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.databinding.FragmentUpdateBinding
import com.altaf.notesapp.utils.ExtensionsFunctions.setActionBar
import com.altaf.notesapp.utils.HelperFunctions.parsetoPriority
import com.altaf.notesapp.utils.HelperFunctions.spinnerListener
import java.util.*

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding as FragmentUpdateBinding

    private val saveArgs: UpdateFragmentArgs by navArgs()

    private val updateViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.safeArgs = saveArgs

        setHasOptionsMenu(true)

        binding.toolbarUpdate.setActionBar(requireActivity())

        binding.apply {
            toolbarUpdate.setActionBar(requireActivity())
            spinnerPrioritiesUpdate.onItemSelectedListener =
                spinnerListener(context, binding.priorityIndicator)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener {
            Toast.makeText(context, "Note has been update", Toast.LENGTH_SHORT).show()
            updateNote()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateNote() {
        val title = binding.edtTitleUpdate.text.toString()
        val desc = binding.edtDescriptionUpdate.text.toString()
        val priority = binding.spinnerPrioritiesUpdate.selectedItem.toString()
        val date = Calendar.getInstance().time
        val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

        val notes = Notes(
            saveArgs.currentItem.id,
            title,
            parsetoPriority(priority, context),
            desc,
            formatDate
        )




        if (title.isEmpty()) {
            binding.edtTitleUpdate.error = "Title is required"
            return
        } else if (desc.isEmpty()) {
            Toast.makeText(context, "Description is required", Toast.LENGTH_SHORT).show()
        } else {
            updateViewModel.updateNote(
                Notes(
                    saveArgs.currentItem.id,
                    title,
                    parsetoPriority(priority, context),
                    desc,
                    formatDate
                )
            )
            val action = UpdateFragmentDirections.actionUpdateFragmentToDetailFragment(notes)
            findNavController().navigate(action)
            Toast.makeText(context, "Note has been update", Toast.LENGTH_SHORT).show()
            Log.i("UpdateFragment", "Note has been update")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}