package algonquin.cst2335.lee00823.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import algonquin.cst2335.lee00823.R;
import algonquin.cst2335.lee00823.data.MainViewModel;
import algonquin.cst2335.lee00823.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater()); //produceslayouts

        setContentView(variableBinding.getRoot());

       // variableBinding.myEditText.setText(model.editString);
        variableBinding.myButton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myEditText.getText().toString());
           // model.editString = variableBinding.myEditText.getText().toString();
            variableBinding.myEditText.setText("Your edit text has: " + model.editString);
            model.editString.observe(this, s -> {
            variableBinding.myEditText.setText("Your edit text has " + s);

                Button myButton  = findViewById(R.id.myButton);
               int width = myButton.getWidth();
               int height = myButton.getHeight();
                String toast_message = "The width = " + width + " and height = " + height;
                Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
            });
        });

        //variableBinding.mySwitch.setChecked(true);

        //setOnCheckedChangeListener((btn, isChecked)-> {});
        model.isSelected.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
        });



//        Button btn  = findViewById(R.id.myButton);
//         String editString = myedit.getText().toString();
//          mytext.setText( "Your edit text has: " + editString);

        //myButton.setOnClickListener(    (View v) -> {  mytext.setText("Your edit text has: " + editString);  }   );
        // if(mybutton != null) mybutton.setOnClickListener( )
//        variableBinding.myButton.setOnClickListener(v -> {
//            model.editString = "You clicked the button";
//            variableBinding.myButton.setText(model.editString);
//        });


    }


    /*
    class MyListner implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
    */

}