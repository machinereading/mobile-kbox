package kr.ac.kaist.qamel.qamel_demo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        // --

        setTitle("모바일 K-Box 데모");

        // --

        GlobalVariable.kbox = new Kbox(this);

        // --

        Button b = (Button) findViewById(R.id.button);

        String label = "K-Box 검색\n-SPARQL endpoint-";
        Spannable spannable = new SpannableString(label);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 9, label.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        b.setText(spannable);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QueryInterface.class);

                startActivity(intent);
            }
        });

        // --

        b = (Button) findViewById(R.id.button2);

        label = "K-Box 동기화\n-외부 KB 로부터 지식 동기화-";
        spannable = new SpannableString(label);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 9,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 0, 9,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 10, label.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        b.setText(spannable);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SyncInterface.class);

                startActivity(intent);
            }
        });

        // --

        b = (Button) findViewById(R.id.button3);

        label = "K-Box 증강\n-외부 텍스트에서 지식추출 및 증강-";
        spannable = new SpannableString(label);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 9, label.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        b.setText(spannable);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReInterface.class);

                startActivity(intent);
            }
        });

        // --

        b = (Button) findViewById(R.id.button4);

        label = "K-Box 저장\n-내부저장소에 저장/다음 실행에 불러오기-";
        spannable = new SpannableString(label);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 0, 8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1f), 9, label.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        b.setText(spannable);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (GlobalVariable.kbox.saveDb()) {
                    Toast.makeText(getApplicationContext(), Integer.toString(GlobalVariable.kbox.size()) + "개 트리플을 저장 완료", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
