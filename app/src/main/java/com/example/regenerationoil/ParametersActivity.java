package com.example.regenerationoil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class ParametersActivity extends AppCompatActivity {
    private RadioButton transformerOilRadioButton, turbineOilRadioButton, hydraulicOilRadioButton;
    private RadioButton cleanOnlyRadioButton, cleanAndRegenerateRadioButton;
    private EditText volumeEditText, cleanlinessClassEditText, waterContentEditText, gasContentEditText;
    private EditText acidityEditText, dielectricLossEditText, breakdownVoltageEditText;
    private Button launchButton;

    private AppDatabase database; // Добавлено поле для базы данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametrs);

        // Инициализация компонентов
        transformerOilRadioButton = findViewById(R.id.transformerRadioButton);
        turbineOilRadioButton = findViewById(R.id.turbineRadioButton);
        hydraulicOilRadioButton = findViewById(R.id.hydraulicRadioButton);
        cleanOnlyRadioButton = findViewById(R.id.cleaningRadioButton);
        cleanAndRegenerateRadioButton = findViewById(R.id.cleaningAndRegenerationRadioButton);
        volumeEditText = findViewById(R.id.oilVolumeEditText);
        cleanlinessClassEditText = findViewById(R.id.initialCleanlinessEditText);
        waterContentEditText = findViewById(R.id.waterContentEditText);
        gasContentEditText = findViewById(R.id.gasContentEditText);
        acidityEditText = findViewById(R.id.acidityEditText);
        dielectricLossEditText = findViewById(R.id.dielectricLossEditText);
        breakdownVoltageEditText = findViewById(R.id.breakdownVoltageEditText);
        launchButton = findViewById(R.id.startButton);

        Context context = getApplicationContext();
        // Инициализация базы данных
        database = Room.databaseBuilder(context, AppDatabase.class, "regeneration_oil")
                .fallbackToDestructiveMigration()
                .build();;

        // Обработчик нажатия на кнопку "Запустить"
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение выбранных и введенных параметров
                String oilType = getSelectedOilType();
                String operationCycle = getSelectedOperationCycle();
                double volume = Double.parseDouble(volumeEditText.getText().toString());
                int cleanlinessClass = Integer.parseInt(cleanlinessClassEditText.getText().toString());
                double waterContent = Double.parseDouble(waterContentEditText.getText().toString());
                double gasContent = Double.parseDouble(gasContentEditText.getText().toString());
                double acidity = Double.parseDouble(acidityEditText.getText().toString());
                double dielectricLoss = Double.parseDouble(dielectricLossEditText.getText().toString());
                double breakdownVoltage = Double.parseDouble(breakdownVoltageEditText.getText().toString());

                // Сохранение параметров в базе данных
                saveParametersToDatabase(oilType, operationCycle, volume, cleanlinessClass, waterContent, gasContent, acidity, dielectricLoss, breakdownVoltage);
            }
        });
    }

    // Метод для получения выбранного типа масла
    private String getSelectedOilType() {
        if (transformerOilRadioButton.isChecked()) {
            return "Трансформаторное";
        } else if (turbineOilRadioButton.isChecked()) {
            return "Турбинное";
        } else if (hydraulicOilRadioButton.isChecked()) {
            return "Гидравлическое";
        } else {
            return "";
        }
    }

    // Метод для получения выбранного цикла работы
    private String getSelectedOperationCycle() {
        if (cleanOnlyRadioButton.isChecked()) {
            return "Только очистка";
        } else if (cleanAndRegenerateRadioButton.isChecked()) {
            return "Очистка и регенерация";
        } else {
            return "";
        }
    }

    // Метод для сохранения параметров в базе данных
    private void saveParametersToDatabase(String oilType, String operationCycle, double volume, int cleanlinessClass, double waterContent, double gasContent, double acidity, double dielectricLoss, double breakdownVoltage) {
        JobEntity job = new JobEntity(
                oilType,
                operationCycle,
                volume,
                cleanlinessClass,
                waterContent,
                gasContent,
                acidity,
                dielectricLoss,
                breakdownVoltage
        );


        // Выполнение операций с базой данных в фоновом потоке
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Сохранение job в базе данных Room
                long id = database.jobDao().insertJob(job);

                Intent intent = new Intent(ParametersActivity.this, NextActivity.class);
                intent.putExtra("jobId", String.valueOf(id));
                startActivity(intent);
            }
        });
        thread.start();

    }
}

