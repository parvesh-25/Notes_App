package com.altaf.notesapp.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.altaf.notesapp.MainActivity
import com.altaf.notesapp.R
import com.altaf.notesapp.data.NotesViewModel
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.databinding.FragmentDetailBinding as FragmentDetailBinding1


class DetailFragment : Fragment() {


    private var _binding : FragmentDetailBinding1? = null
    // val binding get dari data yg atas
    private val binding get() = _binding as FragmentDetailBinding1

    private val navArgs by navArgs<DetailFragmentArgs>()

    private  val detailViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding1.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)

        binding.safeArgs = navArgs

        binding.toolbarDetail.apply {
            (requireActivity() as MainActivity).setSupportActionBar(this)
            setupWithNavController(navController, appBarConfig)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_edit -> {
                val action = DetailFragmentDirections.actionDetailFragmentToUpdateFragment(
                    Notes(
                        navArgs.currentItem.id,
                        navArgs.currentItem.title,
                        navArgs.currentItem.priority,
                        navArgs.currentItem.description,
                        navArgs.currentItem.date
                    )
                )
                findNavController().navigate(action)
            }
            R.id.action_delete -> confirmDeleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteNote() {
        context?.let{
            AlertDialog.Builder(it)
                .setTitle("Delete ${navArgs.currentItem.title}?")
                .setMessage("Sure want to delete'${navArgs.currentItem.title}' ?")
                .setPositiveButton("Iyeh") {_,_->
                    detailViewModel.deleteNote(navArgs.currentItem)
                    Toast.makeText(
                        requireContext(),
                        "Succesfully removed: ${navArgs.currentItem.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    // untuk menampilkan toast

                }
                .setNegativeButton("No") {_,_->}
                .setNeutralButton("Cancel"){_,_->}
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}