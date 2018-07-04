package com.test.pokedex;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.network.ApiConstants;
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon2;
import com.test.pokedex.network.models.pokemon.Pokemon1;
import com.test.pokedex.view_model.Pokemon2ViewModel;

import java.util.List;

import javax.inject.Inject;

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

    private Pokemon1 pokemon;
    private List<Pokemon2> pokemon2List;

    String imageRoot = "https://img.pokemondb.net/artwork/";
    String imageRoot2 = "https://pokeres.bastionbot.org/images/pokemon/";

    Callback<Pokemon1> callback = new Callback<Pokemon1>() {
        @Override
        public void onResponse(Call<Pokemon1> call, Response<Pokemon1> response) {
            pokemon = response.body();
            hideAnimationView();
            setPokemonData();
        }

        @Override
        public void onFailure(Call<Pokemon1> call, Throwable t) {

        }
    };

    Callback<List<Pokemon2>> pokemon2Callback = new Callback<List<Pokemon2>>() {
        @Override
        public void onResponse(Call<List<Pokemon2>> call, Response<List<Pokemon2>> response) {
            if (response.body() != null) {
                pokemon2List = response.body();
                setPokemon2Data();
            }
            else {
                // TODO: 4/6/18 show error
            }
        }

        @Override
        public void onFailure(Call<List<Pokemon2>> call, Throwable t) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        PokeApplication.appComponent().injectPokemonDetailActivity(this);

        getBundleData();

        initViews();

//        Call<Pokemon1> pokemonByUrlCall = pokeApiService.getPokemonByURL(pokemonUrl);
//        pokemonByUrlCall.enqueue(callback);

//        Call<List<Pokemon2>> pokemonByID = pokedexApiService.getPokemonByID(pokemonId);
//        pokemonByID.enqueue(pokemon2Callback);

        Pokemon2ViewModel pokemon2ViewModel = ViewModelProviders.of(this).get(Pokemon2ViewModel.class);
        pokemon2ViewModel.getPokemon2(pokemonId).observe(this, new Observer<Pokemon2>() {
            @Override
            public void onChanged(@Nullable Pokemon2 pokemon2) {
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
        Pokemon2 pokemon2 = pokemon2List.get(0);
        showPokemon2(pokemon2);
    }

    private void showPokemon2(Pokemon2 pokemon2) {
        hideAnimationView();
        String img_url = pokemon2.getSprite();

        Timber.v("Image URL = " + img_url);

        imageLoader.loadImage(img_url, pokemonImageView, imageLoadingAnimationView);
        pokemonNameTextView.setText(pokemon2.getName());
        pokemonIdTextView.setText(String.format("#%s", pokemon2.getNumber()));
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
