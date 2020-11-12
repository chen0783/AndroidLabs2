package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherForecast extends AppCompatActivity {

    TextView currentText;
    TextView minText;
    TextView maxText;
    TextView uvText;
    ImageView weatherImage;
    ProgressBar weatherProgressBar;
    String uvRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        currentText = (TextView)findViewById(R.id.currentTemp);
        minText = (TextView)findViewById(R.id.minTemp);
        maxText = (TextView)findViewById(R.id.maxTemp);
        uvText = (TextView)findViewById(R.id.UV);
        weatherImage = (ImageView)findViewById(R.id.weatherImage);
        weatherProgressBar.setVisibility(View.VISIBLE);
        ForecastQuery query = new ForecastQuery();
        query.execute();
    }
    //add code to check if your cloudy, sunny, raining images are already present in the local storage directory
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }

    //create inner class ForecastQuery (step5)
    private  class ForecastQuery extends AsyncTask<String, Integer, String>{
        String currentTemp;
        String min;
        String max;
        String uv;
        Bitmap image;
        String iconName;

//        @Override
//        protected void onPreExcute(){
//            currentText = (TextView)findViewById(R.id.currentTemp);
//            minText = (TextView)findViewById(R.id.minTemp);
//            maxText = (TextView)findViewById(R.id.maxTemp);
//            uvText = (TextView)findViewById(R.id.UV);
//        }

        @Override
        protected String doInBackground(String... args){

            String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            try {

                //create a URL object of what server to contact
                URL url = new URL(weatherUrl);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at BEGIN_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemp = xpp.getAttributeValue(null,    "value");
                            publishProgress(25);

                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);

                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);

                        }else if (xpp.getName().equals("weather")){
                            iconName = xpp.getAttributeValue(null, "icon");
                            String iconFileName = iconName + ".png";

                            // If the Image file exists, then you don’t need to re-download it, just read it from your disk
                            if (fileExistance(iconFileName)){
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconFileName);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);
                                Log.i("WeatherForecast", "Image already exists");

                            }else{
                                //copy from instruction, step9: download image
                                //Bitmap image = null;
                                URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");

                                HttpURLConnection connection = (HttpURLConnection) iconUrl.openConnection();
                                connection.connect();
                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    image = BitmapFactory.decodeStream(connection.getInputStream());
                                }
                                //Save the Bitmap object to the local application storage
                                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                Log.i("WeatherForecast","Add new image");

                            }
                            Log.i("WeatherForecast", "file name="+iconFileName);
                            publishProgress(100);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            }
            catch (Exception e)
            {
            }

            //get UV information with JSON method.
            String uvUrl ="http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
            try {

                //create a URL object of what server to contact
                URL url = new URL(uvUrl);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data
                InputStream response = urlConnection.getInputStream();

                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                uv = uvReport.getString("value");
                //      publishProgress(100);
                Log.i("MainActivity", "The uv is now: " + uvRating) ;

            }
            catch (Exception e)
            {
            }

            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer ...values){
            super.onProgressUpdate(values);
            weatherProgressBar.setProgress(values[0]);
            weatherProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String results ){
            super.onPostExecute(results);
            String degree = Character.toString((char) 0x00B0);
            currentText.setText(currentText.getText()+" : "+currentTemp+degree+"C");
            minText.setText(minText.getText()+" : "+min+degree+"C");
            maxText.setText(maxText.getText()+" : "+max+degree+"C");
            uvText.setText(uvText.getText()+" : "+uv);
            weatherImage.setImageBitmap(image);

            //weatherProgressBar.setVisibility(View.INVISIBLE);
        }

    }
}