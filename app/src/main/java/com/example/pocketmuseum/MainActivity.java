package com.example.pocketmuseum;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;
import android.os.AsyncTask;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

  private ImageView artworkImage;
  private TextView artworkNameTextView;
  private TextView artistNameTextView;
  private TextView artworkDescriptionTextView;
  private TextView yearCreatedTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Initialize UI elements
    artworkImage = findViewById(R.id.artworkImage);
    artworkNameTextView = findViewById(R.id.artworkName);
    artistNameTextView = findViewById(R.id.artistName);
    artworkDescriptionTextView = findViewById(R.id.artworkDescription);
    yearCreatedTextView = findViewById(R.id.yearCreated);

    // Load and display the artwork of the day
    new LoadArtworkTask().execute();
  }

  private class LoadArtworkTask extends AsyncTask<Void, Void, JSONObject> {
    private Exception error; // Store the exception, if any

    @Override
    protected JSONObject doInBackground(Void... params) {
      try {
        Log.d("Load Artwork Task", "Starting network request");
        OkHttpClient client = new OkHttpClient();
        String apiKey = "7a2b9683-6f25-4e72-b05a-1a127e7582e4";
        String apiUrl = "https://api.harvardartmuseums.org/object";

        // Add parameters to the API URL (e.g., specify the artwork of the day)
        String fullUrl = apiUrl + "?apikey=" + apiKey + "&sort=random";


        Request request = new Request.Builder()
          .url(fullUrl)
          .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
          String responseBody = response.body().string();

          return new JSONObject(responseBody);
        }
      } catch (Exception e) {
        error = e; // Store the exception for later reporting
      }
      return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
      if (error != null) {
        // Handle the error. You can display an error message to the user.
        // For example, you can show a Toast with the error message.
        Toast.makeText(MainActivity.this, "An error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
      } else if (json != null) {
        try {
          // Extract the first artwork from the JSON response
          JSONArray records = json.getJSONArray("records");
          if (records.length() > 0) {
            JSONObject artwork = records.getJSONObject(0);


            // Extract and display artist name from Json response
            String artistName = null;
            if (artwork.has("people")) {
              JSONArray peopleArray = artwork.getJSONArray("people");
              if (peopleArray.length() > 0) {
                JSONObject artist = peopleArray.getJSONObject(0);
                artistName = artist.optString("name");
              }
            }

            // Extract and display the artwork data from the JSON response
            String artworkName = artwork.getString("title");
            String description = artwork.getString("description");
            String yearCreated = artwork.getString("dated");

            // Update UI elements with the retrieved data
            artworkNameTextView.setText(artworkName);
            artistNameTextView.setText(artistName);
            artworkDescriptionTextView.setText(description);
            yearCreatedTextView.setText(yearCreated);

            String imageUrl = artwork.getString("primaryimageurl");
            Picasso.get().load(imageUrl).into(artworkImage);
          }
        } catch (Exception e) {
          // Handle JSON parsing error
          Toast.makeText(MainActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
          e.printStackTrace();

        }
      }
    }
  }
}
