package com.example.fimaz.wsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class MainMenuActivity extends Activity {

    public TextView txtWelcome, lblName, lblLastName, lblPhoneNumber, lblAddress, lblPostalCode, lblCity, lblState, lblCountry, lblDegree, lblEmail, lblUsername, lblCompanyName, lblRFC, lblPosition;
    public Button btnEditProfile, btnFindPacket;
    public String url, username;

    public void setData(String dataCustomer[]){
        lblName = (TextView)findViewById(R.id.lblName);
        lblLastName = (TextView)findViewById(R.id.lblLastName);
        lblPhoneNumber = (TextView)findViewById(R.id.lblPhoneNumber);
        lblAddress = (TextView)findViewById(R.id.lblAddress);

        lblPostalCode = (TextView)findViewById(R.id.lblPostalCode);
        lblCity = (TextView)findViewById(R.id.lblCity);
        lblState = (TextView)findViewById(R.id.lblState);
        lblCountry = (TextView)findViewById(R.id.lblCountry);
        lblDegree = (TextView)findViewById(R.id.lblDegree);
        lblEmail = (TextView)findViewById(R.id.lblEmail);
        lblUsername = (TextView)findViewById(R.id.lblUsername);

        lblCompanyName = (TextView)findViewById(R.id.lblCompanyName);
        lblRFC = (TextView)findViewById(R.id.lblRFC);
        lblPosition = (TextView)findViewById(R.id.lblPosition);



        lblName.setText(dataCustomer[0]);
        lblLastName.setText(dataCustomer[1]);
        lblPhoneNumber.setText(dataCustomer[2]);
        lblAddress.setText(dataCustomer[3]);

        lblPostalCode.setText(dataCustomer[4]);
        lblCity.setText(dataCustomer[5]);
        lblState.setText(dataCustomer[6]);
        lblCountry.setText(dataCustomer[7]);
        lblDegree.setText(dataCustomer[8]);
        lblEmail.setText(dataCustomer[9]);
        lblUsername.setText(dataCustomer[10]);

        lblCompanyName.setText(dataCustomer[11]);
        lblRFC.setText(dataCustomer[12]);
        lblPosition.setText(dataCustomer[13]);




    }

    public void executeQuery(){

        ThreadWS tarea = new ThreadWS();
        tarea.execute(

                getIntent().getStringExtra("username")
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        url = getIntent().getStringExtra("url");
        username = getIntent().getStringExtra("username");

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome " + username);

        executeQuery();

        //Accion para activity de edit profile
        btnEditProfile = (Button)findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, EditProfileActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        //Accion para activity de find packet
        btnFindPacket = (Button)findViewById(R.id.btnFindPacket);
        btnFindPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, FindPacketActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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

    public class ThreadWS extends AsyncTask<String, Integer, Boolean> {
        String newDataCustomer[] = new String[15];
        public Boolean doInBackground(String... params) {


            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://"+url+"/packetsWS/queryDataCustomer.php");

            post.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("user", params[0]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                HttpEntity respStr = resp.getEntity();
                String respuesta = EntityUtils.toString(respStr);

                System.out.println(respuesta);
                //guardamos las
                String dataCustomer[] = respuesta.split(",");

                for (int i = 0; i < dataCustomer.length; i++) {

                    if (dataCustomer[i].equals("NULL") || dataCustomer[i].equals("null")) {
                        dataCustomer[i] = " ";
                    } else {

                            newDataCustomer[i] = dataCustomer[i].replaceAll("[^A-Za-z0-9_@. ]", "");

                    }
                    System.out.println(newDataCustomer[i]);
                }





                //lblName.setText(newDataCustomer[0]);

                if (respuesta != "") {
                    resul = true;
                    //System.out.println("resul " + resul);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        public void onPostExecute(Boolean result) {


            if (result == true) {
                setData(newDataCustomer);

            }


        }


    }
}
