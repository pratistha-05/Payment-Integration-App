package com.example.razorpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    private String TAG =" main";
    private Button startpayment;
    private EditText orderamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startpayment = (Button) findViewById(R.id.payment_button);
        orderamount = (EditText) findViewById(R.id.amount);

        startpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderamount.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "Amount is empty", Toast.LENGTH_LONG).show();
                }else {
                    startPayment();
                }
            }
        });
    }
    private void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "BlueApp Software");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = orderamount.getText().toString();
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "kamal.bunkar07@gmail.com");
            preFill.put("contact", "9144040888");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (JSONException e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, " payment successfull "+ s.toString());
        Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
        MediaPlayer mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.audio);//You Can Put Your File Name Instead Of abc
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        mediaplayer.start();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG,  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }

    }
}

