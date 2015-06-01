package com.example.fimaz.wsexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class EditProfileActivity extends Activity {
    public String url;
    public EditText txtName, txtLastName, txtPhoneNumber, txtAddress, txtPostalCode, txtCity, txtState, txtCountry, txtEmail, txtCompanyName, txtRFC, txtPosition;
    public Button button;


    public void showConfirm(){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure.")
                .setMessage("Do you want apply update?")
                //botonPositivo
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executeUpdate();
                    }
                })
                //BotonNegativo
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(EditProfileActivity.this, MainMenuActivity.class);
                        intent.putExtra("url", getIntent().getStringExtra("url"));
                        intent.putExtra("username", getIntent().getStringExtra("username"));
                        startActivity(intent);
                    }
                }).show();

    }

    public void setData(String dataCustomer[]) {
        txtName = (EditText) findViewById(R.id.txtName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);

        txtPostalCode = (EditText) findViewById(R.id.txtPostalCode);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtState = (EditText) findViewById(R.id.txtState);
        txtCountry = (EditText) findViewById(R.id.txtCountry);
        //txtDegree = (EditText)findViewById(R.id.txtDegree);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        //txtUsername = (EditText)findViewById(R.id.txtUsername);

        txtCompanyName = (EditText) findViewById(R.id.txtCompanyName);
        txtRFC = (EditText) findViewById(R.id.txtRFC);
        txtPosition = (EditText) findViewById(R.id.txtPosition);


        txtName.setText(dataCustomer[0]);
        txtLastName.setText(dataCustomer[1]);
        txtPhoneNumber.setText(dataCustomer[2]);
        txtAddress.setText(dataCustomer[3]);

        txtPostalCode.setText(dataCustomer[4]);
        txtCity.setText(dataCustomer[5]);
        txtState.setText(dataCustomer[6]);
        txtCountry.setText(dataCustomer[7]);
        //txtDegree.setText(dataCustomer[8]);
        txtEmail.setText(dataCustomer[9]);
        //txtUsername.setText(dataCustomer[10]);

        txtCompanyName.setText(dataCustomer[11]);
        txtRFC.setText(dataCustomer[12]);
        txtPosition.setText(dataCustomer[13]);


    }

    // METODO PARA ejecutar el Thread QUERY
    public void executeQuery() {

        ThreadWSProfile tarea = new ThreadWSProfile();
        tarea.execute(
                getIntent().getStringExtra("username")
        );
    }

    // METODO PARA ejecutar el Thread UPDATE
    public void executeUpdate() {


        ThreadWSUpdateProfile tarea = new ThreadWSUpdateProfile();
        tarea.execute(
                getIntent().getStringExtra("username"),
                txtName.getText().toString(),
                txtLastName.getText().toString(),
                txtPhoneNumber.getText().toString(),
                txtAddress.getText().toString(),

                txtPostalCode.getText().toString(),
                txtCity.getText().toString(),
                txtState.getText().toString(),
                txtCountry.getText().toString(),
                txtEmail.getText().toString(),

                txtCompanyName.getText().toString(),
                txtRFC.getText().toString(),
                txtPosition.getText().toString()


        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        button = (Button) findViewById(R.id.button);

        url = getIntent().getStringExtra("url");

        System.out.println(getIntent().getStringExtra("url"));
        System.out.println(getIntent().getStringExtra("username"));


        executeQuery();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirm();


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

    public class ThreadWSProfile extends AsyncTask<String, Integer, Boolean> {
        String newDataCustomer[] = new String[15];

        public Boolean doInBackground(String... params) {


            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://" + url + "/packetsWS/queryDataCustomer.php");

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
                    System.out.println(i);
                    System.out.println(newDataCustomer[i]);
                }

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

        /**
         * **************************METODO PARA REALIZAR UPDATE *******************************
         */
        public class ThreadWSUpdateProfile extends AsyncTask<String, Integer, Boolean> {
            String newDataCustomer[];

            public Boolean doInBackground(String... params) {


                boolean resul = false;

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost post1 = new
                        HttpPost("http://"+url+"/packetsWS/updateDataCustomer.php");

                post1.setHeader("content-type", "application/json");

                try {

                    //Construimos el objeto cliente en formato JSON
                    JSONObject dato = new JSONObject();

                    dato.put("user", params[0]);
                    dato.put("newName", params[1]);
                    dato.put("newLastName", params[2]);
                    dato.put("newAddress", params[3]);
                    dato.put("newPhoneNumber", params[4]);
                    dato.put("newPostalCode", params[5]);
                    dato.put("newCity", params[6]);
                    dato.put("newState", params[7]);
                    dato.put("newCountry", params[8]);
                    dato.put("newEmail", params[9]);
                    dato.put("newCompanyName", params[10]);
                    dato.put("newRFC", params[11]);
                    dato.put("newPosition", params[12]);


                    StringEntity entity = new StringEntity(dato.toString());
                    post1.setEntity(entity);

                    HttpResponse resp = httpClient.execute(post1);
                    HttpEntity respStr = resp.getEntity();
                    String respuesta = EntityUtils.toString(respStr).replaceAll("[^A-Za-z0-9_@. ]", "");;

                    String dataCustomer[] = respuesta.split(",");
                    newDataCustomer = new String[dataCustomer.length];

                    //System.out.println("tamano  "+newDataCustomer.length);

                    for (int i = 0; i < dataCustomer.length; i++) {

                        if (dataCustomer[i].equals("NULL") || dataCustomer[i].equals("null")) {
                            dataCustomer[i] = " ";
                        } else {

                            newDataCustomer[i] = dataCustomer[i].replaceAll("[^A-Za-z0-9_@. ]", "");

                        }
                        System.out.println(i);
                        System.out.println(newDataCustomer[i]);
                    }



                    if(newDataCustomer[0].equals("guardado")) {
                        resul = true;

                    }

                } catch (Exception ex) {
                    Log.e("ServicioRest", "Error!", ex);
                    resul = false;
                }

                return resul;
            }

            public void onPostExecute(Boolean result) {


                if (result == true) {

                    Toast.makeText(EditProfileActivity.this, "Apply changes, please wait.", Toast.LENGTH_SHORT).show();
                    executeQuery();
                    Intent intent = new Intent();
                    intent.setClass(EditProfileActivity.this, MainMenuActivity.class);
                    intent.putExtra("username", getIntent().getStringExtra("username"));
                    intent.putExtra("url", url);
                    startActivity(intent);

                }


            }


        }
    }




