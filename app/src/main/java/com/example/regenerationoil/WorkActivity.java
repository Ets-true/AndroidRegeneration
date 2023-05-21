package com.example.regenerationoil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class WorkActivity extends AppCompatActivity {

    private AppDatabase database;
    private int targetFilReq;
    private double targetDryReq;
    private double targetDgReq;
    private double targetAcidityReq;
    private double targetDielectricLossReq;
    private double targetBreakdownVoltageReq;
    private int targetAddReq;

    private Handler handler;
    private Runnable apiRunnable;
    private final int delayMillis = 500;
    final AtomicBoolean isProcessRunning = new AtomicBoolean(false);


    private void startApiRequest() {
        handler = new Handler();
        apiRunnable = new Runnable() {
            @Override
            public void run() {
                if (isProcessRunning.get()) {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://regenerationoil--kirillsmirnov21.repl.co/getCurrent")
                            .get()
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            // Обработка ошибки при выполнении запроса
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String jsonResponse = response.body().string();

                                // Обработка ответа сервера
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonResponse);

                                    int currentStateReq = jsonObject.optInt("currentStateReq", 0);
                                    int currentFilReq = jsonObject.optInt("currentFilReq", 0);
                                    double currentDryReq = jsonObject.optDouble("currentDryReq", 0.0);
                                    double currentDgReq = jsonObject.optDouble("currentDgReq", 0.0);
                                    double currentAcidityReq = jsonObject.optDouble("currentAcidityReq", 0.0);
                                    double currentDielectricLossReq = jsonObject.optDouble("currentDielectricLossReq", 0.0);
                                    double currentBreakdownVoltageReq = jsonObject.optDouble("currentBreakdownVoltageReq", 0.0);
                                    int currentAddReq = jsonObject.optInt("currentAddReq", 0);
                                    boolean workFinished = jsonObject.optBoolean("workFinished", false);
                                    boolean isRunning = jsonObject.optBoolean("isRunning", false);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView currentFil = findViewById(R.id.currentFil);
                                            TextView currentDry = findViewById(R.id.currentDry);
                                            TextView currentDg = findViewById(R.id.currentDg);
                                            TextView currentAcidity = findViewById(R.id.currentAcidity);
                                            TextView currentDielectricLoss = findViewById(R.id.currentDielectricLoss);
                                            TextView currentBreakdownVoltage = findViewById(R.id.currentBreakdownVoltage);
                                            TextView currentAdd = findViewById(R.id.currentAdd);

                                            currentFil.setText(String.valueOf(currentFilReq));
                                            currentDry.setText(String.valueOf(currentDryReq));
                                            currentDg.setText(String.valueOf(currentDgReq));
                                            currentAcidity.setText(String.valueOf(currentAcidityReq));
                                            currentDielectricLoss.setText(String.valueOf(currentDielectricLossReq));
                                            currentBreakdownVoltage.setText(String.valueOf(currentBreakdownVoltageReq));
                                            currentAdd.setText(String.valueOf(currentAddReq));


                                            LinearLayout[] statusBlock = new LinearLayout[5];
                                            statusBlock[0] = findViewById(R.id.status1Block);
                                            statusBlock[1] = findViewById(R.id.status2Block);
                                            statusBlock[2] = findViewById(R.id.status3Block);
                                            statusBlock[3] = findViewById(R.id.status4Block);
                                            statusBlock[4] = findViewById(R.id.status5Block);

                                            TextView[] statusText = new TextView[5];
                                            statusText[0] = findViewById(R.id.status1Text);
                                            statusText[1] = findViewById(R.id.status2Text);
                                            statusText[2] = findViewById(R.id.status3Text);
                                            statusText[3] = findViewById(R.id.status4Text);
                                            statusText[4] = findViewById(R.id.status5Text);

                                            if(currentStateReq != 0) {
                                                int index = currentStateReq - 1;
                                                statusBlock[index].setBackgroundColor(Color.parseColor("#79C982"));
                                                statusText[index].setText("В процессе");

                                                for (int i = 0; i < statusText.length; i++) {
                                                    if (i != index) {
                                                        statusBlock[i].setBackgroundColor(Color.parseColor("#DCDCDC"));
                                                        statusText[i].setText("Ожидание");
                                                    }

                                                }
                                            }

//                                            if(!isRunning) {
//                                                for (int i = 0; i < statusText.length; i++) {
//                                                    statusBlock[i].setBackgroundColor(Color.parseColor("#DCDCDC"));
//                                                    statusText[i].setText("Ожидание");
//                                                }
//                                            }

                                            if(workFinished) {
                                                Button buttonStart = findViewById(R.id.buttonStart);
                                                Button buttonRestart = findViewById(R.id.buttonRestart);
                                                Button buttonNext = findViewById(R.id.buttonNext);
                                                isProcessRunning.set(false);
                                                buttonRestart.setEnabled(false);
                                                buttonNext.setEnabled(false);
                                                buttonStart.setText("Запустить");
                                                buttonStart.setBackgroundColor(Color.parseColor("#79C982"));
                                                Toast.makeText(getApplicationContext(), "Техпроцесс завершен", Toast.LENGTH_SHORT).show();
                                            }
                                            handler.postDelayed(apiRunnable, delayMillis);

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };

        handler.post(apiRunnable);
    }

    private void stopApiRequest() {
        if (handler != null && apiRunnable != null) {
            handler.removeCallbacks(apiRunnable);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        database = Room.databaseBuilder(context, AppDatabase.class, "regeneration_oil")
                .fallbackToDestructiveMigration()
                .build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);




        String jobIdString = getIntent().getStringExtra("jobId");
        int jobId = Integer.parseInt(jobIdString);


        // Выполнение операций с базой данных в фоновом потоке
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                JobEntity savedJob = database.jobDao().getJobById(jobId);

                Gson gson = new Gson();
                String json = gson.toJson(savedJob);
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody requestBody = RequestBody.create(mediaType, json);

                Request request = new Request.Builder()
                        .url("https://regenerationoil--kirillsmirnov21.repl.co/process")
                        .post(requestBody)
                        .build();


                try {
                    Response response = client.newCall(request).execute();

                    // Обработка ответа сервера
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();

                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

                        targetFilReq = jsonObject.get("targetFil").getAsInt();
                        targetDryReq = jsonObject.get("targetDry").getAsDouble();
                        targetDgReq = jsonObject.get("targetDg").getAsDouble();
                        targetAcidityReq = jsonObject.get("targetAcidity").getAsDouble();
                        targetDielectricLossReq = jsonObject.get("targetDielectricLoss").getAsDouble();
                        targetBreakdownVoltageReq = jsonObject.get("targetBreakdownVoltage").getAsDouble();
                        targetAddReq = jsonObject.get("targetAdd").getAsInt();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView baseFil = findViewById(R.id.baseFil);
                        TextView baseDry = findViewById(R.id.baseDry);
                        TextView baseDg = findViewById(R.id.baseDg);
                        TextView baseAcidity = findViewById(R.id.baseAcidity);
                        TextView baseDielectricLoss = findViewById(R.id.baseDielectricLoss);
                        TextView baseBreakdownVoltage = findViewById(R.id.baseBreakdownVoltage);

                        baseFil.setText(String.valueOf(savedJob.getCleanlinessClass()));
                        baseDry.setText(String.valueOf(savedJob.getWaterContent()));
                        baseDg.setText(String.valueOf(savedJob.getGasContent()));
                        baseAcidity.setText(String.valueOf(savedJob.getAcidity()));
                        baseDielectricLoss.setText(String.valueOf(savedJob.getDielectricLoss()));
                        baseBreakdownVoltage.setText(String.valueOf(savedJob.getBreakdownVoltage()));


                        TextView targetFil = findViewById(R.id.targetFil);
                        TextView targetDry = findViewById(R.id.targetDry);
                        TextView targetDg = findViewById(R.id.targetDg);
                        TextView targetAcidity = findViewById(R.id.targetAcidity);
                        TextView targetDielectricLoss = findViewById(R.id.targetDielectricLoss);
                        TextView targetBreakdownVoltage = findViewById(R.id.targetBreakdownVoltage);
                        TextView targetAdd = findViewById(R.id.targetAdd);

                        targetFil.setText(String.valueOf(targetFilReq));
                        targetDry.setText(String.valueOf(targetDryReq));
                        targetDg.setText(String.valueOf(targetDgReq));
                        targetAcidity.setText(String.valueOf(targetAcidityReq));
                        targetDielectricLoss.setText(String.valueOf(targetDielectricLossReq));
                        targetBreakdownVoltage.setText(String.valueOf(targetBreakdownVoltageReq));
                        targetAdd.setText(String.valueOf(targetAddReq));
                    }
                });
            }
        });

        thread.start();

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonRestart = findViewById(R.id.buttonRestart);
        Button buttonNext = findViewById(R.id.buttonNext);



        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProcessRunning.get()) {
                    stopApiRequest();
                    isProcessRunning.set(false);


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://regenerationoil--kirillsmirnov21.repl.co/stop")
                            .post(RequestBody.create(MediaType.parse("text/plain"), ""))
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });

                    Toast.makeText(getApplicationContext(), "Техпроцесс остановлен", Toast.LENGTH_SHORT).show();

                    buttonRestart.setEnabled(false);
                    buttonNext.setEnabled(false);
                    buttonStart.setText("Запустить");
                    buttonStart.setBackgroundColor(Color.parseColor("#79C982"));
                } else {
                    startApiRequest();
                    isProcessRunning.set(true);
                    Toast.makeText(getApplicationContext(), "Техпроцесс запущен", Toast.LENGTH_SHORT).show();

                    buttonRestart.setEnabled(true);
                    buttonNext.setEnabled(true);

                    buttonStart.setText("Остановить");
                    buttonStart.setBackgroundColor(Color.RED);

                    // Отправить POST-запрос на запуск процесса
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://regenerationoil--kirillsmirnov21.repl.co/start")
                            .post(RequestBody.create(MediaType.parse("text/plain"), ""))
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            // Обработка ошибки при выполнении запроса
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // Обработка успешного ответа сервера
                        }
                    });
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://regenerationoil--kirillsmirnov21.repl.co/next")
                            .post(RequestBody.create(MediaType.parse("text/plain"), ""))
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
                    Toast.makeText(getApplicationContext(), "Следующий этап", Toast.LENGTH_SHORT).show();
            }
        });





    }

}
