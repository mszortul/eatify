package com.example.eatify1506;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    final ArrayList<String> ingredients = new ArrayList<String>();
    Spinner categorySpinner;
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        categorySpinner = findViewById(R.id.categorySpinner);

        final String[] categories = new String[]{
                "Aperatif",
                "Çorba",
                "Diyet",
                "Et",
                "Hamur işi",
                "İçecek",
                "Kahvaltılık",
                "Salata",
                "Sandviç",
                "Sebze",
                "Tatlı",
                "Vegan",
                "Vejetaryen",
                "Diğer"
        };

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(spinnerArrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Item selected", " " + categories[position]);
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    // Continue button
    public void continueStep2(View view) {
        String headline, entry, nop, ptime, ctime;

        EditText recipeHeadline = findViewById(R.id.recipeHeadline);
        headline = recipeHeadline.getText().toString();
        EditText recipeEntry = findViewById(R.id.recipeEntry);
        entry = recipeEntry.getText().toString();
        EditText numberOfPerson = findViewById(R.id.numberOfPersonInput);
        nop = numberOfPerson.getText().toString();
        EditText prepTime = findViewById(R.id.prepTimeInput);
        ptime = prepTime.getText().toString();
        EditText cookTime = findViewById(R.id.cookTimeInput);
        ctime = cookTime.getText().toString();

        int lengthCheck = headline.length() * entry.length() * nop.length() * ptime.length() * ctime.length() * ingredients.size();

        if (lengthCheck > 0) {
            Intent intent = new Intent(getApplicationContext(), Add2Activity.class);
            intent.putExtra("headline", headline);
            intent.putExtra("entry", entry);
            intent.putExtra("nop", nop);
            intent.putExtra("ptime", ptime);
            intent.putExtra("ctime", ctime);
            intent.putExtra("ingList", ingredients);
            intent.putExtra("category", selectedCategory);
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(AddActivity.this, "Devam etmek için tüm alanları doldurmalısınız.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    public void addIng(View view) {

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);

        final NonScrollListView ingListView = findViewById(R.id.ingListView);
        ingListView.setAdapter(arrayAdapter);

        ingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Pressed on", "pos" + Integer.toString(position));

                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("Bu malzemeyi silmek istediğinize emin misiniz?");

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                // Set up the buttons
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ingredients.remove(pos);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                return true;
            }
        });

        EditText matInput = findViewById(R.id.matInput);

        String new_item = matInput.getText().toString();

        if (new_item.trim().length() > 0) {
            ingredients.add(new_item);
            Log.i("Item added", new_item);
            Log.i("Current size", Integer.toString(ingredients.size()));
            arrayAdapter.notifyDataSetChanged();

            matInput.setText("");

        } else {
            Toast.makeText(AddActivity.this, "Lütfen malzeme bilgilerini eksiksiz giriniz.", Toast.LENGTH_SHORT).show();
        }


    }

}