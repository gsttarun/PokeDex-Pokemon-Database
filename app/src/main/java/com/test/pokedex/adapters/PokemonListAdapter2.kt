package com.test.pokedex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.pokedex.ImageLoader
import com.test.pokedex.PokeApplication
import com.test.pokedex.R
import com.test.pokedex.RecyclerViewPagingAdapter
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import javax.inject.Inject

class PokemonListAdapter2(val pokemonClickListener: PokemonClickListener) : RecyclerViewPagingAdapter<PokemonItem>() {
    init {
        setLoadingLayout(R.layout.list_item_loading)
        PokeApplication.appComponent()!!.injectPokemonListAdapter(this)
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_pokemon, parent, false)
        return PokemonListItemViewHolder2(view)
    }

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PokemonListItemViewHolder2) {
            val pokemonItem = getItem(position)
            with(holder) {
                //            bindData(pokemonItem, position)
                rootView.setOnClickListener {
                    pokemonClickListener.onPokemonClick(holder.adapterPosition,getItem(holder.adapterPosition))
                }
                val name = pokemonItem.name
                val upperString = name.substring(0, 1).toUpperCase() + name.substring(1)
                pokemonNameTextView.text = upperString
                val imagePath = "https://img.pokemondb.net/sprites/x-y/normal/$name.png"
                imageLoader.loadImage(imagePath, pokemonImageView, null)
                holder.pokemonIdTextView.text = String.format("%d", pokemonItem.id)
            }
        }
    }

    override fun getViewType(position: Int): Int {
        return VIEW_TYPE_DEAFULT
    }

    class PokemonListItemViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var rootView: View = itemView.findViewById(R.id.list_item_root)
        var pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        var pokemonIdTextView: TextView = itemView.findViewById(R.id.pokemon_id)
        var pokemonImageView: ImageView = itemView.findViewById(R.id.pokemon_img)

        fun bindData(pokemonItem: PokemonItem, position: Int) {
            val name = pokemonItem.name
            val upperString = name.substring(0, 1).toUpperCase() + name.substring(1)
            pokemonNameTextView.text = upperString
            val imagePath = "https://img.pokemondb.net/sprites/x-y/normal/$name.png"
//            imageLoader.loadImage(imagePath, pokemonImageView, null)
            pokemonIdTextView.text = String.format("%d", position + 1)
        }
    }

    interface PokemonClickListener {
        fun onPokemonClick(position: Int,pokemonItem: PokemonItem)
    }
}

