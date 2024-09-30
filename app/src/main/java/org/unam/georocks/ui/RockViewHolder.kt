package org.unam.georocks.ui

import androidx.recyclerview.widget.RecyclerView
import org.unam.georocks.data.db.model.RockEntity
import org.unam.georocks.databinding.RockElementBinding

class RockViewHolder(
    private val binding: RockElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(rock: RockEntity) {
        binding.apply {
            tvName.text = rock.name
            tvType.text = rock.type
            tvOrigin.text = rock.origin
        }
    }
}
