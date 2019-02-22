package com.test.pokedex;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.network.ApiConstants;
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon;
import com.test.pokedex.view_model.Pokemon2ViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class PokemonDetailActivity extends AppCompatActivity implements ApiConstants {

    @Inject
    ImageLoader imageLoader;

    String pokemonUrl;
    String pokemonName;
    int pokemonId;
    ImageView pokemonImageView;
    TextView pokemonNameTextView, pokemonIdTextView;
    LottieAnimationView loadingAnimationView, imageLoadingAnimationView;

    private Pokemon pokemon;
    private List<Pokemon> pokemon2List;

    String imageRoot = "https://img.pokemondb.net/artwork/";
    String imageRoot2 = "https://pokeres.bastionbot.org/images/pokemon/";

    Callback<Pokemon> callback = new Callback<Pokemon>() {
        @Override
        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
            pokemon = response.body();
            hideAnimationView();
            setPokemonData();
        }

        @Override
        public void onFailure(Call<Pokemon> call, Throwable t) {

        }
    };

    Callback<List<Pokemon>> pokemon2Callback = new Callback<List<Pokemon>>() {
        @Override
        public void onResponse(Call<List<Pokemon>> call, Response<List<Pokemon>> response) {
            if (response.body() != null) {
                pokemon2List = response.body();
                setPokemon2Data();
            }
            else {
                // TODO: 4/6/18 show error
            }
        }

        @Override
        public void onFailure(Call<List<Pokemon>> call, Throwable t) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        PokeApplication.Companion.appComponent().injectPokemonDetailActivity(this);

        getBundleData();

        initViews();

//        Call<Pokemon> pokemonByUrlCall = pokeApiService.getPokemonByURL(pokemonUrl);
//        pokemonByUrlCall.enqueue(callback);

//        Call<List<Pokemon>> pokemonByID = pokedexApiService.getPokemonByID(pokemonId);
//        pokemonByID.enqueue(pokemon2Callback);

        Pokemon2ViewModel pokemon2ViewModel = ViewModelProviders.of(this).get(Pokemon2ViewModel.class);
        pokemon2ViewModel.getPokemon2(pokemonId).observe(this, new Observer<Pokemon>() {
            @Override
            public void onChanged(@Nullable Pokemon pokemon2) {
                if (pokemon2 != null) {
                    showPokemon2(pokemon2);
                }
            }
        });

    }

    private void getBundleData() {
        pokemonUrl = getIntent().getStringExtra(URL);
        pokemonName = getIntent().getStringExtra(POKEMON_NAME);
        pokemonId = getIntent().getIntExtra(POKEMON_ID, 0);
    }

    private void initViews() {
        pokemonImageView = findViewById(R.id.pokemon_img);
        pokemonNameTextView = findViewById(R.id.pokemon_name);
        pokemonIdTextView = findViewById(R.id.pokemon_id);
        loadingAnimationView = findViewById(R.id.loading_view);
        imageLoadingAnimationView = findViewById(R.id.img_loading_view);
    }


    private void setPokemon2Data() {
        Pokemon pokemon2 = pokemon2List.get(0);
        showPokemon2(pokemon2);
    }

    private void showPokemon2(Pokemon pokemon2) {
        hideAnimationView();
//        String img_url = pokemon2.getSprites().getFrontDefault();
        String img_url = "https://img.pokemondb.net/artwork/" + pokemon2.getName() + ".jpg";

        Timber.v("Image URL = " + img_url);

        imageLoader.loadImage(img_url, pokemonImageView, imageLoadingAnimationView);
        pokemonNameTextView.setText(pokemon2.getName());
        pokemonIdTextView.setText(String.format("#%s", pokemon2.getId()));
    }


    private void hideAnimationView() {
        loadingAnimationView.cancelAnimation();
        loadingAnimationView.setVisibility(View.GONE);
    }

    private void setPokemonData() {
        String img_url;
//        img_url = imageRoot + pokemon.getName() + ".jpg";
        img_url = imageRoot2 + pokemon.getId() + ".png";

        Timber.v("Image URL = " + img_url);

        imageLoader.loadImage(img_url, pokemonImageView, imageLoadingAnimationView);

        pokemonNameTextView.setText(pokemon.getName());
        pokemonIdTextView.setText(String.format("# %d", pokemon.getId()));

    }

}
