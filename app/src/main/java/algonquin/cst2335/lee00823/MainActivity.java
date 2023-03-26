package algonquin.cst2335.lee00823;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.lee00823.databinding.ActivityMainBinding;


/**
 * @author Su Yeoun Lee
 * @version 1.0
 * This MainActivity class is to practice creating JavaDocs for Lab10.
 * Also, Created
 */
public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;
    protected ActivityMainBinding binding;
    Bitmap image;
    String minTemp;
    String maxTemp;
    Runnable Runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = "";

            try {

              stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                      + URLEncoder.encode(cityName, "UTF-8")
                      + "&appid=a840d787cf24f31f5762db75e73ebe77&units=metric";

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                        try {
                            JSONObject coord = response.getJSONObject("coord");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");


                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            String imgURL = "https://openweathermap.org/img/w/" + iconName + ".png";

                            String pathname = getFilesDir()+"/" + iconName +".png";

                            File file = new File( pathname );

                            if(file.exists()){
                                image = BitmapFactory.decodeFile(pathname);
                            } else {
                                // let's download that URL and store it as a bitmap. Volley also has an ImageRequest object, like the JSONObjectRequest.
                                ImageRequest imgReq = new ImageRequest(imgURL, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        image = bitmap;
//second HTTP request to the server and the BitmapFactory.decodeStream() is reading the data from the server.
// Now you should save the icon to the device so that next time you don't have to download if you've already downloaded it:
                                        FileOutputStream fOut = null;
                                        try {
                                            fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);

                                            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                            fOut.flush();
                                            fOut.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();

                                        }
                                    } //onResponse
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                });
                                queue.add(imgReq);
                            } //file exists else


                            runOnUiThread( ( ) -> { //To make sure that you are calling these functions on the main GUI thread
                               //set the text after you've read the strings from the JSON response
                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.humidity.setText("The humidity is " + humidity);
                                binding.humidity.setVisibility(View.VISIBLE);

                                binding.description.setText( description);
                                binding.description.setVisibility(View.VISIBLE);

                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, //JsonObjectRequest (response)

                    ( error ) -> {

                    }

            ); //JsonObjectRequest
            queue.add( request );
        }); //setOnClickListener

    } //onCreate
}