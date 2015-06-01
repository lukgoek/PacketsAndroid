package com.example.fimaz.wsexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public Button btnLogin;
    TextView lblSignIn;
    public EditText txtUsername;
    public EditText txtPassword;
    public String url ="";

    public void getSettings(){
        SharedPreferences settings =
                getSharedPreferences("Settings", Context.MODE_PRIVATE);

        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        url = settings.getString("serverIp", "192.10.10.111");
        txtUsername.setText(settings.getString("username","user@example"));
        txtPassword.setText(settings.getString("password",""));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);


        getSettings();

        btnLogin.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            ThreadWS tarea = new ThreadWS();
                                            tarea.execute(
                                                    txtUsername.getText().toString(),
                                                    txtPassword.getText().toString());


                                        }
                                    }

        );

        lblSignIn = (TextView)findViewById(R.id.lblSignIn);
        lblSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SignInActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            Toast.makeText(getApplicationContext(),"Please Wait a moment.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public class ThreadWS extends AsyncTask<String,Integer,Boolean> {

        public Boolean doInBackground(String... params) {

            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://"+url+"/packetsWS/index.php");

            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("user", params[0]);
                dato.put("password", params[1]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                HttpEntity respStr = resp.getEntity();
                String respuesta = EntityUtils.toString(respStr);


                System.out.println("respuesta: "+respuesta);
                System.out.println("*"+respuesta+"*");
                System.out.println("==");
                System.out.println("*\ntrue*");

                if(respuesta.equals("\ntrue")) {
                    resul = true;
                    System.out.println("resul " + resul);
                }
            }

            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        public void onPostExecute(Boolean result) {



            if (result == true)
            {
                Toast.makeText(MainActivity.this, "Please wait a moment.",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("username", txtUsername.getText().toString());
                startActivity(intent);


            }else{
                Toast.makeText(MainActivity.this, "Invalid username or password.",
                        Toast.LENGTH_SHORT).show();
            }

        }



    }
}
