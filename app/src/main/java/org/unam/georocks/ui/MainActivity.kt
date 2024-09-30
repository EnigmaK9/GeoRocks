package org.unam.georocks.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.unam.georocks.R
import org.unam.georocks.data.RocksRepository
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.databinding.ActivityMainBinding
import org.unam.georocks.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.unam.georocks.application.GeoRocksDBApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var rocks: MutableList<RockEntity> = mutableListOf()
    private lateinit var repository: RocksRepository

    private lateinit var rockAdapter: RockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as GeoRocksDBApp).repository

        rockAdapter = RockAdapter { selectedRock ->

            // Click on each rock record

            val dialog = RockDialog(newRock = false, rock = selectedRock, updateUI = {
                updateUI()
            }, message = { text ->
                // Function for messages
                message(text)
            })

            dialog.show(supportFragmentManager, "dialog2")
        }

        // Set the RecyclerView
        binding.rvRocks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rockAdapter
        }

        updateUI()
    }

    fun click(view: View) {
        // Handle the click of the floating action button

        val dialog = RockDialog(updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })

        dialog.show(supportFragmentManager, "dialog1")
    }

    private fun message(text: String) {
        Snackbar.make(
            binding.cl,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.snackbar))
            .show()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            rocks = repository.getAllRocks()

            binding.tvNoRecords.visibility =
                if (rocks.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            rockAdapter.updateList(rocks)
        }
    }
}
