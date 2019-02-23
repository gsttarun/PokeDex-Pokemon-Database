package com.test.pokedex.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.ImageLoader;
import com.test.pokedex.MyDiffUtilCallBack;
import com.test.pokedex.PokeApplication;
import com.test.pokedex.R;
import com.test.pokedex.network.models.pokemon_list.PokemonItem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    private Queue<List<PokemonItem>> pendingUpdates =
            new ArrayDeque<>();

    @Inject
    ImageLoader imageLoader;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<PokemonItem> pokemonItems;

    @Inject
    public PokemonListAdapter(Context context) {
        pokemonItems = new ArrayList<>();
        PokeApplication.Companion.appComponent().injectPokemonListAdapter(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_pokemon, parent, false);
            return new PokemonListItemViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PokemonListItemViewHolder) {
            PokemonItem pokemonItem = pokemonItems.get(position);
            PokemonListItemViewHolder pokemonListItemViewHolder = (PokemonListItemViewHolder) holder;
            pokemonListItemViewHolder.bindData(pokemonItem, position);
            pokemonListItemViewHolder.rootView.setOnClickListener(this);
            pokemonListItemViewHolder.rootView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return pokemonItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return pokemonItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
                    pokemonItems.add(null);
                    loadingItemInsertPosition = pokemonItems.size() - 1;
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

    public void setPokemonItems(List<PokemonItem> pokemonItems) {
        this.pokemonItems = pokemonItems;
        notifyDataSetChanged();
    }

    public void addResults(List<PokemonItem> resultsToAdd) {
        if (pokemonItems.size() > 0) {
            int removePosition = pokemonItems.size() - 1;
            pokemonItems.remove(removePosition);
            notifyItemRemoved(removePosition);
            pokemonItems.addAll(resultsToAdd);
            notifyItemRangeInserted(removePosition, resultsToAdd.size());
        }
        else {
            pokemonItems.addAll(resultsToAdd);
            notifyDataSetChanged();
        }
    }

    public void updateList(List<PokemonItem> newList) {
        if (pokemonItems.size() > 0) {
            pendingUpdates.add(newList);
            if (pendingUpdates.size() > 1) {
                return;
            }
            updateItemsInternal(newList);
        }
        else {
            pokemonItems.addAll(newList);
            notifyDataSetChanged();
        }

    }

    private void updateItemsInternal(final List<PokemonItem> newList) {
        final int removePosition = pokemonItems.size() - 1;
        pokemonItems.remove(removePosition);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallBack(pokemonItems, newList));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRemoved(removePosition);
                        applyDiffResult(newList, diffResult);
                    }
                });
            }
        }).start();


    }

    // This method is called when the background work is done
    protected void applyDiffResult(List<PokemonItem> newList,
                                   DiffUtil.DiffResult diffResult) {
        pendingUpdates.remove();
        dispatchUpdates(newList, diffResult);
        if (pendingUpdates.size() > 0) {
            updateItemsInternal(pendingUpdates.peek());
        }
    }

    // This method does the work of actually updating
    // the backing data and notifying the adapter
    private void dispatchUpdates(final List<PokemonItem> newList,
                                 final DiffUtil.DiffResult diffResult) {
        final PokemonListAdapter pokemonListAdapter = this;
        recyclerView.post(new Runnable() {
            @Override
            public void run() {

                diffResult.dispatchUpdatesTo(pokemonListAdapter);
                pokemonItems.clear();
                pokemonItems.addAll(newList);
            }
        });
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

        void bindData(PokemonItem pokemonItem, int position) {

            String name = pokemonItem.getName();
            String upperString = name.substring(0, 1).toUpperCase() + name.substring(1);
            pokemonNameTextView.setText(upperString);
            String imagePath = "https://img.pokemondb.net/sprites/x-y/normal/" + name + ".png";
            imageLoader.loadImage(imagePath, pokemonImageView, null);
            pokemonIdTextView.setText(String.format("%d", position + 1));
        }
    }


    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView animationView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            animationView = itemView.findViewById(R.id.animation_view);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
