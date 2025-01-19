package com.example.catchfallingobject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class CreditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        TextView creditText = findViewById(R.id.creditText);
        creditText.setText("Directed by:\n\n- Saral Maxwell.M \n\n\n Written by:\\n\\n- Avani\\n- Jeevitha \"\n\n\nCreated by:\n\n- Rahul Prasad.R\n- Eswaran S ");
    }
}
