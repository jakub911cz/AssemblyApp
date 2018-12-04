package com.jfa.assemblyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    Switch simpleSwitch2, simpleSwitch3;
    CheckBox checkBox;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;

    public static final String TAG = MainActivity.class.getSimpleName();
    TextView ListOfNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView ListOfNames = findViewById(R.id.ListOfNames);
        String listText = getResources().getString(R.string.missing);
        ListOfNames.setMovementMethod(new ScrollingMovementMethod());
        ListOfNames.setText(listText);


try {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String savedName  = preferences.getString("user", "");
            String savedRoom  = preferences.getString("room", "");

            EditText roomName = findViewById(R.id.room_name);
            EditText userName = findViewById(R.id.user_name);

            roomName.setText(savedRoom);
            userName.setText(savedName);
            Log.e(TAG, savedName);
            Log.e(TAG, savedRoom);

            if (savedName != ""){
            checkBox = findViewById(R.id.checkBox);
            checkBox.setChecked(true);}

           } catch (NullPointerException e ) {
    Log.e(TAG, e.toString());
 }


        // LOG IN BUTTON
        Button getIn = findViewById(R.id.getIn);
        getIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    TextView userName = findViewById(R.id.user_name);
                    String user = userName.getText().toString();

                    TextView roomName = findViewById(R.id.room_name);
                    String room = roomName.getText().toString();

                    // Create a new user
                    Map<String, Object> userX = new HashMap<>();
                    userX.put(user, user);

                    // Add a new document with a generated ID
                    db.collection("users").document(room).set(userX, SetOptions.merge());
            }
        });

        //LOG OUT BUTTON
        Button getOut = findViewById(R.id.getOut);
        getOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView userName = findViewById(R.id.user_name);
                String user = userName.getText().toString();

                TextView roomName = findViewById(R.id.room_name);
                String room = roomName.getText().toString();

                DocumentReference docRef = db.collection("users").document(room);

                docRef.update(user, FieldValue.delete());

                }
        });


        // switch for shi-sha logic
        simpleSwitch2 = findViewById(R.id.switch2);
        simpleSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (simpleSwitch2.isChecked()) {

                }
            }
        });

    checkBox = findViewById(R.id.checkBox);
    checkBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView roomName = findViewById(R.id.room_name);
            TextView userName = findViewById(R.id.user_name);
            String user = userName.getText().toString();
            String room = roomName.getText().toString();

            if (checkBox.isChecked()){
                SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", user);
                editor.putString("room", room);
                editor.apply();
            }
            else{
               preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                preferences.edit().clear().apply();
                Log.e(TAG, "User preferences deleted.");
                preferences = null;
            }

        }
    });

    }

    @Override
    protected void onResume() {
        super.onResume();

        simpleSwitch3 = findViewById(R.id.logInRoom);
        simpleSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (simpleSwitch3.isChecked()) {

                    TextView roomName = findViewById(R.id.room_name);
                    String room = roomName.getText().toString();

                    DocumentReference docRef = db.collection("users").document(room);
                    docRef.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Toast.makeText(MainActivity.this, "Error while loading.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(documentSnapshot.exists()) {
                                //       TextView roomName = findViewById(R.id.room_name);
                                //       String room = roomName.getText().toString();
                                //       String names = documentSnapshot.getString(room);
                                String presentUsers = documentSnapshot.getData().toString();
                      //          String presentUsers = documentSnapshot.get(<String>);

                                TextView ListOfNames = findViewById(R.id.ListOfNames);
                                ListOfNames.setMovementMethod(new ScrollingMovementMethod());
                                ListOfNames.setText(presentUsers);
                            }

                        }
                    });

                }

                else {
                    TextView ListOfNames = findViewById(R.id.ListOfNames);
                    ListOfNames.setMovementMethod(new ScrollingMovementMethod());
                    ListOfNames.setText("Nesleduješ hasičárnu.");
                }
            }
        });

                        if (simpleSwitch3.isChecked()) {

                        TextView roomName = findViewById(R.id.room_name);
                        String room = roomName.getText().toString();

                        DocumentReference docRef = db.collection("users").document(room);
                        docRef.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(MainActivity.this, "Error while loading.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(documentSnapshot.exists()) {
                                    //       TextView roomName = findViewById(R.id.room_name);
                                    //       String room = roomName.getText().toString();
                                    //       String names = documentSnapshot.getString(room);
                                    String presentUsers = documentSnapshot.getData().toString();
                                    //          String presentUsers = documentSnapshot.get(<String>);

                                    TextView ListOfNames = findViewById(R.id.ListOfNames);
                                    ListOfNames.setMovementMethod(new ScrollingMovementMethod());
                                    ListOfNames.setText(presentUsers);
                                }

                            }
                        });

                    }

                    else {
                        TextView ListOfNames = findViewById(R.id.ListOfNames);
                        ListOfNames.setMovementMethod(new ScrollingMovementMethod());
                        ListOfNames.setText("Nesleduješ hasičárnu.");
                    }
                }


}



