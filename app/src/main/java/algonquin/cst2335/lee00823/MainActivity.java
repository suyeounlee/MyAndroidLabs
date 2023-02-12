package algonquin.cst2335.lee00823;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @author Su Yeoun Lee
 * @version 1.0
 * This MainActivity class is to practice creating JavaDocs for Lab5.
 * Also, Created checkPasswordComplexity() function to check password complexity for Login.
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the center of the screen */
    private TextView tv = null;
    /** This holds the Edit text at the screen */
    private EditText et = null;
    /** This holds the button at the screen */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            checkPasswordComplexity(password);

        });
    }

    /**
     * This function is to check password complexity before login
     *
     * @param pw The String object that we are checking
     * @return true if the password is complex enough
     */
    @SuppressLint("SuspiciousIndentation")
    boolean checkPasswordComplexity(String pw) {

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if(isSpecialCharacter(c)){
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            tv.setText("You shall not pass!.");
            Toast.makeText(getApplicationContext(), "Upper case letter is missing", Toast.LENGTH_SHORT).show();// Say that they are missing an upper case letter;
            return false;
        } else if (!foundLowerCase) {
            tv.setText("You shall not pass!.");
            Toast.makeText(getApplicationContext(), "Lower case letter is missing", Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;
            return false;
        } else if (!foundNumber) {
            tv.setText("You shall not pass!.");
            Toast.makeText(getApplicationContext(), "Number is missing", Toast.LENGTH_SHORT).show(); // Say that they are missing a number;
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(getApplication(), "Special character is missing", Toast.LENGTH_SHORT).show(); // Say that they are missing a character;
            tv.setText("You shall not pass!.");
            return false;
        } else
            tv.setText("Your password meets the requirements.");
        return true; //only get here if they're all true

    }

    /**
     * This function is to check special character contains
     * @param c The char object that we are checking
     * @return true if c is one of: #$%^*!@?
     */
    boolean isSpecialCharacter(char c) {
        switch(c) {
            case '#':
            case '?':
            case '*':
            case '!':
            case '$':
            case '%':
            case '&':
            case '~':
            case '@':
            case '^':
                return true;
            default:
                return false;
        }
    }
}
