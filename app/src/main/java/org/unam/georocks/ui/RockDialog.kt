package org.unam.georocks.ui

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import org.unam.georocks.R
import org.unam.georocks.application.GeoRocksDBApp
import org.unam.georocks.data.RocksRepository
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.databinding.RockDialogBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class RockDialog(
    private val newRock: Boolean = true,
    private var rock: RockEntity = RockEntity(
        name = "",
        type = "",
        origin = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {

    private var _binding: RockDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: RocksRepository

    // This is where the dialog is created and initially configured
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RockDialogBinding.inflate(requireActivity().layoutInflater)

        // Get an instance of the repository inside the dialog fragment
        repository = (requireContext().applicationContext as GeoRocksDBApp).repository

        builder = AlertDialog.Builder(requireContext())

        // Set the values of the rock object in the text input fields
        binding.apply {
            tietName.setText(rock.name)
            tietType.setText(rock.type)
            tietOrigin.setText(rock.origin)
        }

        dialog = if (newRock)
            buildDialog("Save", "Cancel", {
                // Save action

                // Get the entered texts and assign them to the rock object
                binding.apply {
                    rock.apply {
                        name = tietName.text.toString()
                        type = tietType.text.toString()
                        origin = tietOrigin.text.toString()
                    }
                }

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.insertRock(rock)
                        }

                        // Wait for this action to complete before proceeding
                        result.await()

                        // Dispatch message and updateUI actions to the main thread
                        withContext(Dispatchers.Main) {
                            message("Rock saved successfully")

                            updateUI()
                        }
                    }
                } catch (e: IOException) {
                    message("Error saving the rock")
                }

            }, {
                // Cancel action
            })
        else
            buildDialog("Update", "Delete", {
                // Update action

                // Get the entered texts and assign them to the rock object
                binding.apply {
                    rock.apply {
                        name = tietName.text.toString()
                        type = tietType.text.toString()
                        origin = tietOrigin.text.toString()
                    }
                }

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.updateRock(rock)
                        }

                        result.await()

                        withContext(Dispatchers.Main) {
                            message("Rock updated successfully")

                            updateUI()
                        }
                    }
                } catch (e: IOException) {
                    message("Error updating the rock")
                }

            }, {
                // Delete action

                // Store context in a variable before launching the new dialog
                val context = requireContext()

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete the rock ${rock.name}?")
                    // Do you really want to delete the rock %1$s?
                    .setPositiveButton("Confirm") { _, _ ->
                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = async {
                                    repository.deleteRock(rock)
                                }

                                result.await()

                                withContext(Dispatchers.Main) {
                                    message("Rock deleted successfully")

                                    updateUI()
                                }
                            }
                        } catch (e: IOException) {
                            message("Error deleting the rock")
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
            })

        return dialog
    }

    // This is called when the dialog is destroyed
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Called after the dialog is displayed on screen
    override fun onStart() {
        super.onStart()

        // Since the Dialog class doesn't allow direct access to its buttons
        val alertDialog = dialog as AlertDialog

        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

        saveButton?.isEnabled = false

        binding.apply {
            setupTextWatcher(
                tietName,
                tietType,
                tietOrigin
            )
        }
    }

    private fun validateFields(): Boolean =
        binding.tietName.text.toString().isNotEmpty() &&
                binding.tietType.text.toString().isNotEmpty() &&
                binding.tietOrigin.text.toString().isNotEmpty()

    private fun setupTextWatcher(vararg textFields: TextInputEditText) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }

        textFields.forEach { textField ->
            textField.addTextChangedListener(textWatcher)
        }
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Rock")
            .setPositiveButton(btn1Text) { _, _ ->
                // Action for the positive button
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                // Action for the negative button
                negativeButton()
            }
            .create()

}
