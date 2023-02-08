package algonquin.cst2335.lee00823;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate() is the first function that gets created when an application is launched
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w( TAG, "In onCreate() - Loading Widgets" );

        Button loginButton = (Button) findViewById(R.id.loginButton);
        EditText emailEditText = (EditText) findViewById(R.id.emailText);

        //create a SharedPreferences object
        //MyData" is the name of the file that will be opened for saving,
        // and the Context.MODE_PRIVATE means that only the application that created the file can open it.
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //save the email address that is typed in to the EditText
        SharedPreferences.Editor editor = prefs.edit();

        //to see if anything is saved using a variable name
        String emailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(emailAddress);


        loginButton.setOnClickListener( clk-> {
            //save the string to your MyData file that you have opened using the command
            editor.putString("LoginName", emailEditText.getText().toString());
            //apply() function writes the data in a background thread so that the GUI doesn't slow down
            editor.apply();

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress", emailEditText.getText().toString() );
            startActivity(nextPage);
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart() - Application now visible on screen" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume() - Responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause() - no longer responds to user input" );

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop() - Application no longer visible" );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "In onDestroy() -  Any memory used by the application is freed." );

    }

}