package com.mostafaabdel_fatah.movieapp.Activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mostafaabdel_fatah.movieapp.Adapters.MovieAdapter;
import com.mostafaabdel_fatah.movieapp.R;
import com.mostafaabdel_fatah.movieapp.helpers.DatabaseHelper;
import com.mostafaabdel_fatah.movieapp.helpers.Movie;
import com.mostafaabdel_fatah.movieapp.helpers.ConnectionStatus;
import com.mostafaabdel_fatah.movieapp.helpers.HelpServices;
import com.mostafaabdel_fatah.movieapp.helpers.WebServices;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView moviesList = null;
    ProgressDialog progressDialog;
    MovieAdapter movieAdapter = null;
    List<Movie> movies = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading. Please Wait ...");
        /******************************************************************************************/
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        /*******************************************************************************************/

        InitializeViews();
        // Check the Internet Connection
        boolean connected = ConnectionStatus.isNetworkAvailable(MainActivity.this);
        if (connected) {
            // Load Data From Server
            new JsonTask().execute();
        }else{
            showDialog("Internet Not Connected","Please Connect to Internet Network .....");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Refresh)
        {
            // Check the Internet Connection
            boolean connected = ConnectionStatus.isNetworkAvailable(MainActivity.this);
            if (connected) {
                // Load Data From Server
                new JsonTask().execute();
            }else{
                showDialog("Internet Not Connected","Please Connect to Internet Network .....");
            }
            return  true;

        }
        return super.onOptionsItemSelected(item);
    }

    public  class JsonTask extends AsyncTask<Void ,String,List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Movie> doInBackground(Void... param) {
            List<Movie> movieList = HelpServices.getMoviesList( WebServices.getMovieJson() );
            if (movieList != null || movieList.size() >= 1 ) {
                return movieList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Movie> list){
            super.onPostExecute(list);
            movies.addAll(list);
            if (movieAdapter != null)
                movieAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
            // Save Data into Database
            /*
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            boolean b = db.InsertMovies(list);
            if (b)
                Toast.makeText(getApplicationContext(),"inserted.....",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"not inserted.....",Toast.LENGTH_LONG).show();*/


        }
    }
    // show Dialog
    private AlertDialog.Builder builder;

    public void showDialog(String title , String message){
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }

        });
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Open Setting Phone Activity
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Initialize View In activity
    private void InitializeViews(){
        moviesList = (ListView) findViewById(R.id.listview);
        movieAdapter = new MovieAdapter(getApplicationContext() ,R.layout.row,movies);
        moviesList.setAdapter(movieAdapter);
        moviesList.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , MovieDetails.class);
                intent.putExtra("movie",movies.get(position).getMovie());
                intent.putExtra("year",movies.get(position).getYear()+"");
                intent.putExtra("tagline",movies.get(position).getTagline());
                intent.putExtra("duration",movies.get(position).getDuration());
                intent.putExtra("director",movies.get(position).getDirector());
                intent.putExtra("rating",movies.get(position).getRating()/2+"");
                intent.putExtra("story",movies.get(position).getStory());
                intent.putExtra("trailer",movies.get(position).getTrailer());
                StringBuffer castString = new StringBuffer();
                for (int i=0;i<movies.get(position).getCastList().size();i++ ) {
                    castString.append(movies.get(position).getCastList().get(i).getName() + "  ");
                }
                intent.putExtra("cast",castString.toString());
                startActivity(intent);

            }
        });
    }





















}

