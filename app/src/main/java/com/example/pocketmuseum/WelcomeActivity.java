package com.example.pocketmuseum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    Button seeArtButton = findViewById(R.id.seeArtButton);

    // Set an OnClickListener for the "See the Art of the Day" button
    seeArtButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Create an Intent to transition to the main screen
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });
  }
}
