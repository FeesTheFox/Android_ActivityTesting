package com.example.activitytesting;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.activitytesting.databinding.ActivityMainBinding;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SharedPreferences sPref; //saves user data
    final String keyName = "Fees"; //key


    private void saveText() {
        sPref = getPreferences(MODE_PRIVATE); //MODE_PRIVATE - privacy settings, after using it, data will be available only for this app
        SharedPreferences.Editor editor = sPref.edit();
        //adding data that can be saved
        editor.putString(keyName, binding.textView.getText().toString());
        editor.commit(); //saves data
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    private void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        //extracting saved data
        Log.d("Something", "loadText:Detect");
        String savedText = sPref.getString(keyName, "");
        binding.textView.setText(savedText); //output in the text field
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<Intent> startSecondActivityForResult = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        //handling result, that returned from SecondActivity
                        public void onActivityResult(ActivityResult result) { //ActivityResult result -
                            // object that gives info about executing the operation
                            if (result.getResultCode() == Activity.RESULT_OK) { //if the activity executed perfectly,
                                // then (RESULT_OK) -
                                // const, that checks if the Activity is executed
                                Intent intent = result.getData(); //result contains that are returned by Intent,
                                // this line extracts Intent
                                if (intent != null) { // if object intent is not null
                                    String name = intent.getStringExtra("Name"); //then extracts info
                                    // from getStringExtra method,
                                    // "Name" - key for extracting the String
                                    binding.textView.setText(name); // name is saved in text field by method setText()
                                }
                            } else { //if result of activity is not fine, then
                                String textError = "Error!"; //initializes String "Error"
                                binding.textView.setText(textError); // it installs into the textView
                            }
                        }
                    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //work with binding
        View view = binding.getRoot();
        setContentView(view);

        //creating an intent
        Intent intent = new Intent(this, SecondActivity.class);
        binding.GoToBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSecondActivityForResult.launch(intent); //transferring the intent to be executed
            }
        });

        loadText();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }
}









