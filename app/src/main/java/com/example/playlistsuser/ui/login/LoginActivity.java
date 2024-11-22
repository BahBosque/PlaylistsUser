package com.example.playlistsuser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.playlistsuser.R;
import com.example.playlistsuser.data.repository.SessionManager;
import com.example.playlistsuser.data.repository.auth.AuthRepository;
import com.example.playlistsuser.data.repository.auth.AuthRepositoryImpl;
import com.example.playlistsuser.service.AuthService;
import com.example.playlistsuser.ui.home.HomeActivity;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        AuthRepository authRepository = new AuthRepositoryImpl();
        AuthService authService = new AuthService(authRepository);
        loginViewModel = new LoginViewModel(authService);

        loginButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmailFilled = !emailInput.getText().toString().trim().isEmpty();
                boolean isPasswordFilled = !passwordInput.getText().toString().trim().isEmpty();

                if (isEmailFilled && isPasswordFilled) {
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1.0f);
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        emailInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                loginViewModel.login(email, password, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess(String token) throws JSONException {
                        sessionManager.saveAuthToken(token);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }
}
