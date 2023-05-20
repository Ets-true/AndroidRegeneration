package com.example.regenerationoil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        // Инициализация базы данных Room
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my-database").build();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                } else {
                    // Проверка пользователя в БД Room в фоновом потоке
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user = database.userDao().getUser(username, password);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (user != null) {
                                        Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        // Добавление пользователя в БД (выполнять только один раз)
        addUserToDatabase();
    }

    private void addUserToDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User("Kirill", "1111");
                database.userDao().insert(user);
            }
        }).start();
    }
}
