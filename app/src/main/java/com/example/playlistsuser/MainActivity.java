package com.example.playlistsuser;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView greetingText = findViewById(R.id.password_input);

        greetingText.setText(getString(R.string.hello_message));

        String novoNome = "User";
        greetingText.setText(getString(R.string.updated_message, novoNome));
    }
}
