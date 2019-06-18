package com.example.eatify1506;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Add2Activity extends AppCompatActivity {

    boolean commandFromChild = false;
    final ArrayList<String> steps = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);

        final EditText addStepInput = findViewById(R.id.addStepInput);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(Add2Activity.this, android.R.layout.simple_list_item_1, steps);

        final NonScrollListView stepListView = findViewById(R.id.stepListView);

        stepListView.setAdapter(arrayAdapter);

        stepListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Pressed on", "pos" + Integer.toString(position));

                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(Add2Activity.this);
                builder.setTitle("Adımı kaldırmak istediğinize emin misiniz?");

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                // Set up the buttons
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        steps.remove(pos);
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

        addStepInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (addStepInput.getRight() - addStepInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        String newStep = addStepInput.getText().toString();
                        steps.add(newStep);
                        addStepInput.setText("");

                        arrayAdapter.notifyDataSetChanged();

                        return true;
                    }
                }

                return false;
            }
        });
    }


    public void continueStep3(View view) {
        if (steps.size() > 0) {
            EditText suggestionInput = findViewById(R.id.suggestionInput);

            Intent in = getIntent(); // came from step 1, forwarding extras to step 3

            Intent intent = new Intent(getApplicationContext(), Add3Activity.class);
            intent.putExtra("stepList", steps);
            intent.putExtra("suggestion", suggestionInput.getText().toString());
            intent.putExtra("headline", in.getStringExtra("headline"));
            intent.putExtra("entry", in.getStringExtra("entry"));
            intent.putExtra("nop", in.getStringExtra("nop"));
            intent.putExtra("ptime", in.getStringExtra("ptime"));
            intent.putExtra("ctime", in.getStringExtra("ctime"));
            intent.putExtra("ingList", in.getStringArrayListExtra("ingList"));
            intent.putExtra("category", in.getStringExtra("category"));

            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(Add2Activity.this, "Devam etmek için yapılış adımlarını yazın.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

}
