package org.unam.georocks.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.unam.georocks.R
import org.unam.georocks.application.GeoRocksDBApp
import org.unam.georocks.data.RocksRepository
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.databinding.RockDialogBinding
import java.io.IOException
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

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
    private var rockImageView: ImageView? = null

    private lateinit var repository: RocksRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RockDialogBinding.inflate(requireActivity().layoutInflater)
        repository = (requireContext().applicationContext as GeoRocksDBApp).repository

        builder = AlertDialog.Builder(requireContext())

        val rockTypes = arrayOf("Igneous", "Sedimentary", "Metamorphic")
        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rockTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRockType.adapter = typeAdapter

        val rockOrigins = arrayOf("Volcanic", "Plutonic", "Metamorphic")
        val originAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rockOrigins)
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRockOrigin.adapter = originAdapter

        binding.spinnerRockType.setSelection(rockTypes.indexOf(rock.type).takeIf { it >= 0 } ?: 0)
        binding.spinnerRockOrigin.setSelection(rockOrigins.indexOf(rock.origin).takeIf { it >= 0 } ?: 0)

        rockImageView = binding.ivHeader
        updateRockIcon(binding.spinnerRockOrigin.selectedItem.toString())

        binding.apply {
            tietName.setText(rock.name)
        }

        dialog = if (newRock)
            buildDialog("Save", "Cancel", {
                binding.apply {
                    rock.apply {
                        name = tietName.text.toString()
                        type = binding.spinnerRockType.selectedItem?.toString() ?: "Unknown"
                        origin = binding.spinnerRockOrigin.selectedItem?.toString() ?: "Unknown"
                    }
                }

                updateRockIcon(rock.origin)

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async { repository.insertRock(rock) }
                        result.await()
                        withContext(Dispatchers.Main) {
                            message("Rock saved successfully")
                            updateUI()
                        }
                    }
                } catch (e: IOException) {
                    message("Error saving the rock")
                }
            }, {
            })
        else
            buildDialog("Update", "Delete", {
                binding.apply {
                    rock.apply {
                        name = tietName.text.toString()
                        type = binding.spinnerRockType.selectedItem?.toString() ?: "Unknown"
                        origin = binding.spinnerRockOrigin.selectedItem?.toString() ?: "Unknown"
                    }
                }

                updateRockIcon(rock.origin)

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async { repository.updateRock(rock) }
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
                val context = requireContext()
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete the rock ${rock.name}?")
                    .setPositiveButton("Confirm") { _, _ ->
                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = async { repository.deleteRock(rock) }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false
        binding.apply {
            setupTextWatcher(tietName)
        }
    }

    private fun updateRockIcon(rockType: String) {
        when (rockType) {
            "Volcanic" -> rockImageView?.setImageResource(R.drawable.volcanic)
            "Plutonic" -> rockImageView?.setImageResource(R.drawable.plutonic)
            "Metamorphic" -> rockImageView?.setImageResource(R.drawable.metamorphic)
            else -> rockImageView?.setImageResource(R.drawable.gamepad)
        }
    }

    private fun validateFields(): Boolean =
        binding.tietName.text.toString().isNotEmpty()

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
            .setPositiveButton(btn1Text) { _, _ -> positiveButton() }
            .setNegativeButton(btn2Text) { _, _ -> negativeButton() }
            .create()
}
