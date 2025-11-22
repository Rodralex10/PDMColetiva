package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseFirestore db;
    private MessageAdapter adapter;
    private final List<Message> messageList = new ArrayList<>();
    private FirebaseAuth auth;
    private EditText editTextMessage;
    private RecyclerView recyclerView;
    private TextView textUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            goToLogin();
            return;
        }
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        Button buttonSend = findViewById(R.id.buttonSend);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        textUser = findViewById(R.id.textUser);

        adapter = new MessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        subscribeToMessages();

        buttonSend.setOnClickListener(view -> sendMessage());
        buttonLogout.setOnClickListener(v -> logout());

        textUser.setText(getAccountName());
    }

    private void subscribeToMessages() {
        db.collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed", e);
                            Toast.makeText(MainActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (snapshots == null) {
                            return;
                        }
                        messageList.clear();
                        messageList.addAll(snapshots.toObjects(Message.class));
                        adapter.setMessages(messageList);
                        if (!messageList.isEmpty()) {
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    private void sendMessage() {
        String text = editTextMessage.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String sender = getAccountName();

        Message msg = new Message(text, sender, null); // ServerTimestamp will populate timestamp

        db.collection("messages")
                .add(msg)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    editTextMessage.setText("");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error sending message", e);
                    Toast.makeText(MainActivity.this, "Send failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void logout() {
        auth.signOut();
        goToLogin();
    }

    private String getAccountName() {
        if (auth == null || auth.getCurrentUser() == null) return "Unknown";
        String display = auth.getCurrentUser().getDisplayName();
        if (!TextUtils.isEmpty(display)) return display;
        String email = auth.getCurrentUser().getEmail();
        if (!TextUtils.isEmpty(email)) {
            int at = email.indexOf("@");
            return at > 0 ? email.substring(0, at) : email;
        }
        return auth.getCurrentUser().getUid();
    }
}
