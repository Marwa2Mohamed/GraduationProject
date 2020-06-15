package com.example.android.paia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class page3 extends AppCompatActivity {
    private Button next;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);
        next=(Button)findViewById(R.id.next);
        back=(Button)findViewById(R.id.back);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page3 = new Intent(page3.this, PermissionActivity.class);
                startActivity(page3);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page3intent = new Intent(page3.this,page2.class);
                startActivity(page3intent);
            }
        });
    }
}
