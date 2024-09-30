package org.unam.georocks.ui

import androidx.recyclerview.widget.RecyclerView
import org.unam.georocks.R
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

            // Assign correct image based on rock origin
            when (rock.origin) {
                "Volcanic" -> ivRockImage.setImageResource(R.drawable.volcanic)
                "Plutonic" -> ivRockImage.setImageResource(R.drawable.plutonic)
                "Metamorphic" -> ivRockImage.setImageResource(R.drawable.metamorphic)
                else -> ivRockImage.setImageResource(R.drawable.gamepad) // Default image
            }
        }
    }
}
