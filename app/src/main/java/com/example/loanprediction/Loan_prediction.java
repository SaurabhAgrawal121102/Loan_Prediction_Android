package com.example.loanprediction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loan_prediction extends AppCompatActivity {
    Spinner[] spinners = new Spinner[7];
    String[][] spinnersdata = {
            { "Select Gender", "Male", "Female" },
            { "Select Married", "Yes", "No" },
            { "Select Education", "Graduate", "Not Graduate" },
            { "Select Employed", "Yes", "No" },
            { "Select Credit history", "Good", "Bad" },
            { "How many dependents?", "0", "1", "2", "3+" },
            { "Select Property Area", "Rural", "Semiurban", "Urban" }
    };
    int[] SpinnersId = {
            R.id.gender_spinner,
            R.id.married_spinner,
            R.id.education_spinner,
            R.id.employed_spinner,
            R.id.credit_history_spinner,
            R.id.dependents_spinner,
            R.id.property_area_spinner
    };

    int[] values = new int[8];
    Button cancel_button, pred_button;
    TextInputEditText[] editTexts = new TextInputEditText[4];
    int[] editTextIds = {
            R.id.applicantIncome,
            R.id.coapplicantIncome,
            R.id.loanAmount,
            R.id.loanTerm
    };

    TextView result_text;
    CardView result_card;
    ProgressBar loading_progress;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan_prediction);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        // Initialize UI elements
        cancel_button = findViewById(R.id.cancel_button);
        pred_button = findViewById(R.id.pred_button);
        result_text = findViewById(R.id.result_text);
        result_card = findViewById(R.id.result_card);
        loading_progress = findViewById(R.id.loading_progress);

        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i] = findViewById(editTextIds[i]);
        }

        setupSpinners();

        cancel_button.setOnClickListener(v -> {
            for (TextInputEditText et : editTexts) {
                et.setText("");
            }
            for (Spinner s : spinners) {
                s.setSelection(0);
            }
            result_card.setVisibility(View.GONE);
            Toast.makeText(Loan_prediction.this, "Cleared", Toast.LENGTH_SHORT).show();
        });

        pred_button.setOnClickListener(v -> performPrediction());
    }

    private void setupSpinners() {
        for (int i = 0; i < spinners.length; i++) {
            spinners[i] = findViewById(SpinnersId[i]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                    spinnersdata[i]);
            spinners[i].setAdapter(adapter);
            int index = i;
            spinners[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        String selected = spinnersdata[index][position];
                        mapSpinnerValue(index, selected);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void mapSpinnerValue(int index, String selected) {
        if (selected.equalsIgnoreCase("Male"))
            values[0] = 1;
        else if (selected.equalsIgnoreCase("Female"))
            values[0] = 0;
        else if (selected.equalsIgnoreCase("Yes"))
            values[index] = 1;
        else if (selected.equalsIgnoreCase("No"))
            values[index] = 0;
        else if (selected.equalsIgnoreCase("Graduate"))
            values[2] = 0;
        else if (selected.equalsIgnoreCase("Not Graduate"))
            values[2] = 1;
        else if (selected.equalsIgnoreCase("Good"))
            values[4] = 1;
        else if (selected.equalsIgnoreCase("Bad"))
            values[4] = 0;
        else if (selected.equalsIgnoreCase("0"))
            values[5] = 0;
        else if (selected.equalsIgnoreCase("1"))
            values[5] = 1;
        else if (selected.equalsIgnoreCase("2"))
            values[5] = 2;
        else if (selected.equalsIgnoreCase("3+"))
            values[5] = 3;
        else if (selected.equalsIgnoreCase("Rural")) {
            values[6] = 0; // Semiurban
            values[7] = 0; // Urban
        } else if (selected.equalsIgnoreCase("Semiurban")) {
            values[6] = 1;
            values[7] = 0;
        } else if (selected.equalsIgnoreCase("Urban")) {
            values[6] = 0;
            values[7] = 1;
        }
    }

    private void performPrediction() {
        // Validation
        for (int i = 0; i < spinners.length; i++) {
            if (spinners[i].getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select " + spinnersdata[i][0].replace("Select ", ""), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        double[] inputs = new double[4];
        for (int i = 0; i < editTexts.length; i++) {
            String val = editTexts[i].getText().toString().trim();
            if (val.isEmpty()) {
                editTexts[i].setError("Required");
                return;
            }
            inputs[i] = Double.parseDouble(val);
        }

        // Show loading
        loading_progress.setVisibility(View.VISIBLE);
        result_card.setVisibility(View.GONE);
        pred_button.setEnabled(false);

        executorService.execute(() -> {
            try {
                Python py = Python.getInstance();
                PyObject pyObject = py.getModule("predict");

                PyObject result = pyObject.callAttr("predict_loan",
                        values[0], // gender
                        values[1], // married
                        values[2], // education
                        values[3], // employed
                        values[4], // credit
                        values[5], // dependents
                        values[6], // semiurban
                        values[7], // urban
                        inputs[0], // applicantIncome
                        inputs[1], // coapplicantIncome
                        inputs[2], // loanAmount
                        inputs[3] // loanTerm
                );

                String predictionResult = result.toString();

                mainHandler.post(() -> {
                    loading_progress.setVisibility(View.GONE);
                    pred_button.setEnabled(true);
                    result_card.setVisibility(View.VISIBLE);
                    result_text.setText(predictionResult);

                    if (predictionResult.contains("Approved")) {
                        result_text.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else {
                        result_text.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                });

            } catch (Exception e) {
                Log.e("PredictionError", "Error during prediction", e);
                mainHandler.post(() -> {
                    loading_progress.setVisibility(View.GONE);
                    pred_button.setEnabled(true);
                    Toast.makeText(Loan_prediction.this, "Prediction failed: " + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}