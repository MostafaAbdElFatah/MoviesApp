package com.mostafaabdel_fatah.movieapp.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mostafaabdel_fatah.movieapp.R;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movie = (TextView) findViewById(R.id.movie);
        TextView tagline = (TextView) findViewById(R.id.tagline);
        TextView year = (TextView) findViewById(R.id.year);
        TextView duration = (TextView) findViewById(R.id.duration);
        TextView director = (TextView) findViewById(R.id.director);
        TextView cast = (TextView) findViewById(R.id.cast);
        TextView story = (TextView) findViewById(R.id.story);
        RatingBar rating=(RatingBar) findViewById(R.id.rating);
        WebView trailer = (WebView) findViewById(R.id.trailer);
        trailer.getSettings().setJavaScriptEnabled(true);
        trailer.setWebChromeClient( new WebChromeClient(){

        });
        try {
            Intent intent = getIntent();
            movie.setText(intent.getStringExtra("movie").toString());
            tagline.setText("Tagline:"+intent.getStringExtra("tagline").toString());
            year.setText("Year:"+intent.getStringExtra("year").toString());
            duration.setText("Duration:"+intent.getStringExtra("duration").toString());
            director.setText("Director:"+intent.getStringExtra("director").toString());
            cast.setText("Cast:"+intent.getStringExtra("cast").toString());
            story.setText(intent.getStringExtra("story").toString());
            trailer.loadData(intent.getStringExtra("trailer").toString(),"text/html","utf-8" );
            double value =2.5;
            value = Double.parseDouble(intent.getStringExtra("rating").toString());
            rating.setRating((float) value );
        } catch (Exception e) {
            Toast.makeText(this, "Message from Detalis: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
