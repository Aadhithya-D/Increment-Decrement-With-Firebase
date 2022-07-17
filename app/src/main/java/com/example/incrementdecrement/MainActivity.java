package com.example.incrementdecrement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button increment;
    Button decrement;
    TextView number;
    int n = 0;
    static final String TAG = "Read Data Activity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        number = findViewById(R.id.textView);

        Map<String, Object> data = new HashMap<>();

        db.collection("Numbers")
                .document("Number")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                n = Integer.valueOf(document.get("Number").toString());
                                number.setText(String.valueOf(n));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(MainActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
                            n = 0;
                        }
                    }
                });
        number.setText(String.valueOf(n));
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n += 1;
                number.setText(String.valueOf(n));
                data.put("Number", n);
                db.collection("Numbers")
                        .document("Number")
                        .set(data);
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n -= 1;
                number.setText(String.valueOf(n));
                data.put("Number", n);
                db.collection("Numbers")
                        .document("Number")
                        .set(data);
            }
        });
    }
}