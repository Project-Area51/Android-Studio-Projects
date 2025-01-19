package com.example.catchfallingobject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.GravityCompat; // Import this for compatibility
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;



public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);

        // Menu button click listener
        ImageView menuButton = findViewById(R.id.menuButton);
/*        menuButton.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(Gravity.END)) {
                drawerLayout.closeDrawer(Gravity.END);
            } else {
                drawerLayout.openDrawer(Gravity.END);
            }
        });
*/
        // Use GravityCompat.END for compatibility
        menuButton.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Restart button logic
        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(view -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            recreate(); // Restart activity
        });

        // Credit button logic
        Button creditButton = findViewById(R.id.creditButton);
        creditButton.setOnClickListener(view -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            Intent intent = new Intent(this, CreditActivity.class);
            startActivity(intent);
        });
    }
}
