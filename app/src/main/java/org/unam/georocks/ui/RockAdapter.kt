package org.unam.georocks.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.databinding.RockElementBinding

class RockAdapter(
    private val onRockClicked: (RockEntity) -> Unit
) : RecyclerView.Adapter<RockViewHolder>() {

    private var rocks: MutableList<RockEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RockViewHolder {
        val binding = RockElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RockViewHolder(binding)
    }

    override fun getItemCount(): Int = rocks.size

    override fun onBindViewHolder(holder: RockViewHolder, position: Int) {
        val rock = rocks[position]
        holder.bind(rock)
        holder.itemView.setOnClickListener {
            onRockClicked(rock)
        }
    }

    fun updateList(list: MutableList<RockEntity>) {
        rocks = list
        notifyDataSetChanged()
    }
}
