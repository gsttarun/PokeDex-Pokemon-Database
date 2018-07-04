package com.test.pokedex.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.pokedex.ImageLoader;
import com.test.pokedex.PokeApplication;
import com.test.pokedex.R;
import com.test.pokedex.network.models.pokemon_list.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class PokemonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading = false;
    private RecyclerView recyclerView;
    private int visibleThreshold = 5;
    private int loadingItemInsertPosition = 0;

    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    @Inject
    ImageLoader imageLoader;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<Result> results;

    @Inject
    public PokemonListAdapter(Context context) {
        results = new ArrayList<>();
        PokeApplication.appComponent().injectPokemonListAdapter(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_pokemon, parent, false);
            return new PokemonListItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PokemonListItemViewHolder) {
            Result result = results.get(position);
            PokemonListItemViewHolder pokemonListItemViewHolder = (PokemonListItemViewHolder) holder;
            pokemonListItemViewHolder.bindData(result, position);
            pokemonListItemViewHolder.rootView.setOnClickListener(this);
            pokemonListItemViewHolder.rootView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        return results.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    results.add(null);
                    loadingItemInsertPosition = results.size() - 1;
                    notifyItemInserted(loadingItemInsertPosition);
                    setLoading();
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                        Timber.v("Load More");
                    }
                }
            }
        });
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public void addResults(List<Result> resultsToAdd) {
        if (results.size() > 0) {
            int removePosition = results.size() - 1;
            results.remove(removePosition);
            notifyItemRemoved(removePosition );
            results.addAll(resultsToAdd);
            notifyItemRangeInserted(removePosition , resultsToAdd.size());
        } else {
            results.addAll(resultsToAdd);
            notifyDataSetChanged();
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.onItemClick((Integer) view.getTag());
    }


    class PokemonListItemViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView pokemonNameTextView, pokemonIdTextView;
        ImageView pokemonImageView;

        public PokemonListItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.list_item_root);
            pokemonNameTextView = itemView.findViewById(R.id.pokemon_name);
            pokemonImageView = itemView.findViewById(R.id.pokemon_img);
            pokemonIdTextView = itemView.findViewById(R.id.pokemon_id);
        }

        void bindData(Result result, int position) {

            String name = result.getName();
            String upperString = name.substring(0, 1).toUpperCase() + name.substring(1);
            pokemonNameTextView.setText(upperString);
            String imagePath = "https://img.pokemondb.net/sprites/x-y/normal/" + name + ".png";
            imageLoader.loadImage(imagePath, pokemonImageView, null);
            pokemonIdTextView.setText(String.format("%d", position + 1));
        }
    }


    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
