package com.stratone.bmotion.activities;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;
/*import butterknife.BindView;
import butterknife.ButterKnife;*/

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stratone.bmotion.R;

public class OrderActivity extends AppCompatActivity {
  /*  @BindView(R.id.barcode)
    ImageView barcode;

    @BindView(R.id.imgBtnBack)
    ImageButton back;

    @BindView(R.id.purchased)
    TextView purchased;*/

    ImageView barcode;
    ImageButton back;
    TextView purchased;

    private String orderNo;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;
    private int purchasedBBM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        /*ButterKnife.bind(this);*/

        barcode = findViewById(R.id.barcode);
        back = findViewById(R.id.imgBtnBack);
        purchased = findViewById(R.id.purchased);

        GetPutExtra();
        GenerateQRCode();

        purchased.setText(purchasedBBM + " ltr");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }

    private void GetPutExtra() {
        if (getIntent().getExtras() != null) {
            orderNo = getIntent().getStringExtra("orderNo");
            purchasedBBM = getIntent().getIntExtra("liter",0);
        }
    }

    private void GenerateQRCode()
    {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                orderNo, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            bitmap = qrgEncoder.getBitmap();
            barcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Back()
    {
        Intent i = new Intent(OrderActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Back();
    }
}
