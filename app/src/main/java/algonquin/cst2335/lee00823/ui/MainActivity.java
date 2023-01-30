package algonquin.cst2335.lee00823.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
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

        variableBinding.myButton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myEditText.getText().toString());
        });

        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has " + s);
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
            Context context = getApplicationContext();
            CharSequence text = "The value is now: " + selected;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });

        variableBinding.checkBox.setOnCheckedChangeListener((checkBox, isChecked) ->
        {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.radioButton.setOnCheckedChangeListener((radioButton, isChecked) ->
        {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.switch1.setOnCheckedChangeListener((switch1, isChecked) ->
        {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.imageButton.setOnClickListener(click ->
        {
            int width = variableBinding.imageButton.getWidth();
            int height = variableBinding.imageButton.getHeight();
            Context context = getApplicationContext();
            CharSequence text = "The width = " + width + " and height = " + height;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });



    }
}
