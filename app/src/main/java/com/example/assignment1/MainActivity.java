package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUnit;
    EditText etRebate;
    Button btnConvert;
    Button btnClear;
    TextView tvCalcDet;
    TextView tvFcharge;
    TextView tvFcharge2;
    TextView tvRebate;
    TextView tvTcharge;
    TextView tvBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUnit = (EditText) findViewById(R.id.etUnit);
        etRebate = (EditText) findViewById(R.id.etRebate);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnClear = (Button) findViewById(R.id.btnClear);
        tvCalcDet = (TextView) findViewById(R.id.tvCalcDet);
        tvTcharge = (TextView) findViewById(R.id.tvTcharge);
        tvFcharge = (TextView) findViewById(R.id.tvFcharge);
        tvFcharge2 = (TextView) findViewById(R.id.tvFcharge2);
        tvRebate = (TextView) findViewById(R.id.tvRebate);
        tvBlock = (TextView) findViewById(R.id.tvBlock);
        btnConvert.setOnClickListener(this);
        btnClear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        switch(v.getId()){
            case R.id.btnConvert:
                try {
                    double meter = Double.parseDouble(etUnit.getText().toString());
                    double rebate = 0.0; // Default value of 0
                    String rebateText = etRebate.getText().toString();
                    if (!rebateText.isEmpty()) {
                        rebate = Double.parseDouble(rebateText) / 100;
                    }
                    int B1 = 200; //block size
                    int B2 = 100;
                    int B3 = 300;
                    double B1_rate = 0.218; // RM/kWh for first 200 kWh (1 - 200 kWh) per month
                    double B2_rate = 0.334; // RM/kWh for next 100 kWh (201 - 300 kWh) per month
                    double B3_rate = 0.516; // RM/kWh for next 300 kWh (301 - 600 kWh) per month
                    double B4_rate = 0.546; // RM/kWh for next 300 kWh onwards (601-900++ kWh) per month
                    double B1_cost = B1*B1_rate; // cost for full block
                    double B2_cost = B2*B2_rate;
                    double B3_cost = B3*B3_rate;
                    double TotalCharge = 0.0;
                    double Fcharge = 0.0;
                    String calculation= "";
                    String output = "";


                    if (meter > 600) {
                        TotalCharge = B1_cost + B2_cost + B3_cost + meter%600*B4_rate;
                        output = String.format("Total Charge: RM%.2f", TotalCharge);
                        calculation = String.format(
                                "Unit Used: %d kWh\n" +
                                "Blocks (kWh):\n\n" +
                                "First 200: 200*"+ B1_rate +" = RM" +B1_cost + "\n" +
                                "Next 100: 100*"+ B2_rate +" = RM" +B2_cost+ "\n" +
                                "Next 300: 300*"+ B3_rate +" = RM" +B3_cost+ "\n" +
                                "Next 300: " + "%.2f*"+ B4_rate+ " = RM"+ meter%600*B4_rate +"\n"
                                ,(int) meter, meter%600);
                        Fcharge = TotalCharge - (TotalCharge * rebate);
                    } else if (meter > 300) {
                        TotalCharge =  B1_cost + B2_cost + (meter % 300) * B3_rate;
                        calculation = String.format(
                                "Unit Used: %d kWh\n" +
                                        "Blocks (kWh):\n\n" +
                                        "First 200: 200*"+B1_rate+" = RM" +B1_cost + "\n" +
                                        "Next 100: 100*"+B2_rate+" = RM" +B2_cost+ "\n" +
                                        "Next 300: " + "%.2f*"+B3_rate+" = RM" + (meter % 300) * B3_rate +"\n"
                                ,(int) meter, meter%300);
                        output = String.format("Total Charge: RM%.2f", TotalCharge);
                        Fcharge = TotalCharge - (TotalCharge * rebate);
                    } else if (meter > 200) {
                        TotalCharge =  B1_cost + (meter % 200) * B2_rate;
                        calculation = String.format(
                                "Unit Used: %d kWh\n" +
                                "Blocks (kWh):\n\n" +
                                "First 200: 200*"+B1_rate+" = RM" +B1_cost + "\n" +
                                "Next 100: " + "%.2f*"+B2_rate+" = RM" +(meter % 200) * B2_rate+"\n"
                                ,(int) meter, meter%200);
                        output = String.format("Total Charge: RM%.2f", TotalCharge);
                        Fcharge = TotalCharge - (TotalCharge * rebate);
                    } else if (meter > 0) {
                        TotalCharge = meter * B1_rate;
                        calculation = String.format(
                        "Unit Used: %d kWh\n" +
                                "Blocks (kWh):\n" +
                                "First 200: " + "%.2f*"+B1_rate+" = RM" + meter * B1_rate +"\n"
                                ,(int) meter, meter);
                        output = String.format("Total Charge: RM%.2f", TotalCharge);
                        Fcharge = TotalCharge - (TotalCharge * rebate);
                    }
                    tvCalcDet.setText("\nCalculation Details");
                    tvBlock.setText(calculation);
                    tvRebate.setText(String.format("Rebate: %.1f%%", rebate*100));
                    tvFcharge.setText(String.format("Final Charge: RM%.2f", Fcharge));
                    tvFcharge2.setText(String.format("Final Charge: RM%.2f", Fcharge));
                    tvTcharge.setText(output);
                    Toast.makeText(this, "Calculation is completed!", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                    // Handle the exception when the input is not a valid number
                    Toast.makeText(this, "Please enter electricity unit used!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClear:
                tvCalcDet.setText("");
                tvTcharge.setText("");
                tvFcharge.setText("");
                tvFcharge2.setText("");
                tvBlock.setText("");
                tvRebate.setText("");
                etUnit.setText("");
                etRebate.setText("");
                Toast toast = Toast.makeText(this, "Cleared!", Toast.LENGTH_SHORT);
                toast.setDuration(250);
                toast.show();
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.about :
            //Toast.makeText(this,"This is about",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AboutActivity.class) ;
            startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}