package com.example.fimaz.wsexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class SettingsActivity extends Activity {

    public Button btnSave;
    public EditText txtServerIp, txtUsername, txtPassword;

    //METODO para recoger settings
    public void getSettings(){
        SharedPreferences settings =
                getSharedPreferences("Settings", Context.MODE_PRIVATE);

        txtServerIp = (EditText)findViewById(R.id.txtServerIp);
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

                txtServerIp.setText(settings.getString("serverIp", "192.10.10.111"));
                txtUsername.setText(settings.getString("username","user@example"));
                txtPassword.setText(settings.getString("password",""));
    }
    //Metodo para guardar settings
    public void setSettings(){
        SharedPreferences settings =
                getSharedPreferences("Settings",Context.MODE_PRIVATE);

        txtServerIp = (EditText)findViewById(R.id.txtServerIp);
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

                SharedPreferences.Editor editorSettings = settings.edit();
                editorSettings.putString("serverIp", txtServerIp.getText().toString());
                editorSettings.putString("username", txtUsername.getText().toString());
                editorSettings.putString("password", txtPassword.getText().toString());
                editorSettings.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button)findViewById(R.id.btnSave);


       getSettings();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettings();
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
