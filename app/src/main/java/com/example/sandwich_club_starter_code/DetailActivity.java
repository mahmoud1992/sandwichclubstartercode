package com.example.sandwich_club_starter_code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sandwich_club_starter_code.model.Sandwich;
import com.example.sandwich_club_starter_code.utils.JsonUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    public static final String TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

//        TextView nameView = findViewById(R.id.name_tv);
        TextView originTv = findViewById(R.id.origin_tv);
        TextView aka = findViewById(R.id.also_known_tv);
        TextView descView = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);

        String aliases = TextUtils.join(", ", sandwich.getAlsoKnownAs());
        // if the sandwich doesn't have any "also known as" names, don't display that section
        if (aliases.isEmpty()) {
            aka.setVisibility(View.GONE);
            aka.setVisibility(View.GONE);
        } else {
            aka.setText(aliases);
        }

        String origin = sandwich.getPlaceOfOrigin();
        if (origin.isEmpty()) {
            origin = getString(R.string.detail_place_of_origin_unknown);
        }
        originTv.setText(origin);

        descView.setText(sandwich.getDescription());

        ingredients.setText(TextUtils.join(", ", sandwich.getIngredients()));
    }
}
