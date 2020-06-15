package com.example.android.paia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class page2 extends AppCompatActivity {
    private Button next;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);
        next=(Button)findViewById(R.id.next);
        back=(Button)findViewById(R.id.back);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page2Intent = new Intent(page2.this,page3.class);
                startActivity(page2Intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(page2.this,MainActivity.class);
                startActivity(pageIntent);
            }
        });
    }
}
