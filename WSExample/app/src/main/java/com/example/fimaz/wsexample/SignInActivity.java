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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class SignInActivity extends Activity {

    public EditText txtName, txtLastName, txtPhoneNumber, txtAddress, txtPostalCode, txtCity, txtState, txtCountry, txtEmail, txtConfirmEmail, txtUsername, txtPassword, txtConfirmPassword, txtCompanyName, txtRFC, txtPosition;
    public Spinner spinnerDegree;
    public Button btnSignIn2;
    public String url = "";

    public String degrees[] = {"dr.", "dra."};


    public boolean comparePassword() {
        boolean confirmPassword = false;
        if (!txtPassword.getText().toString().equals("") && !txtPassword.getText().toString().equals("")) {
            if (txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {

                confirmPassword = true;
            } else {
                Toast.makeText(SignInActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(SignInActivity.this, "Please set the password.", Toast.LENGTH_SHORT);
        }


        return confirmPassword;
    }


    public boolean requiredFields() {
        boolean confirmFields = false;

        String name = txtName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();
        String address = txtAddress.getText().toString();
        String postalCode = txtPostalCode.getText().toString();
        String city = txtCity.getText().toString();
        String state = txtState.getText().toString();
        String country = txtCountry.getText().toString();
        //DEGREE

        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();


        if (!name.equals("") && !lastName.equals("") && !phoneNumber.equals("") && !address.equals("") && !postalCode.equals("") && !city.equals("") && !state.equals("") && !country.equals("") && !password.equals("") && !confirmPassword.equals("")) {
            confirmFields = true;
        } else {
            Toast.makeText(SignInActivity.this, "Please complete all the required fields(*).", Toast.LENGTH_SHORT);
        }

        return confirmFields;
    }


    public void setDataSpinnerDegree(String newData[]) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, newData);
        spinnerDegree.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.url = getIntent().getStringExtra("url");
        ThreadWSSpinner thread = new ThreadWSSpinner();
        thread.execute("1");
        //Actualiza la lista con los valores de Degree en la BD

        spinnerDegree = (Spinner) findViewById(R.id.spinnerDegree);


        txtName = (EditText) findViewById(R.id.txtName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPostalCode = (EditText) findViewById(R.id.txtPostalCode);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtState = (EditText) findViewById(R.id.txtState);
        txtCountry = (EditText) findViewById(R.id.txtCountry);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtConfirmEmail = (EditText) findViewById(R.id.txtConfirmEmail);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        txtCompanyName = (EditText)findViewById(R.id.txtCompanyName);
        txtRFC = (EditText)findViewById(R.id.txtRFC);
        txtPosition = (EditText)findViewById(R.id.txtPosition);

        btnSignIn2 = (Button) findViewById(R.id.btnSingin);
        btnSignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //required fields
                String name = txtName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String phoneNumber = txtPhoneNumber.getText().toString();
                String address = txtAddress.getText().toString();
                String postalCode = txtPostalCode.getText().toString();
                String city = txtCity.getText().toString();
                String state = txtState.getText().toString();
                String country = txtCountry.getText().toString();
                String degree = spinnerDegree.getSelectedItem().toString();
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                //optional fields
                String email = txtEmail.getText().toString();
                String confirmEmail = txtConfirmEmail.getText().toString();
                String companyName = txtCompanyName.getText().toString();
                String RFC = txtRFC.getText().toString();
                String position = txtPosition.getText().toString();

                //optional
                if (email.equals("")) {
                    email = "NULL";
                    confirmEmail = "NULL";
                }

                if (companyName.equals("")) {
                    companyName = "NULL";
                }

                if (RFC.equals("")) {
                    RFC = "NULL";
                }

                if (position.equals("")) {
                    position = "NULL";
                }

                //required
                if (name.equals("") || lastName.equals("") || phoneNumber.equals("") || address.equals("") || postalCode.equals("") || city.equals("") || state.equals("") || degree.equals("") || country.equals("") || username.equals("") || password.equals("")) {

                    Toast.makeText(SignInActivity.this, "Please complete all required fields(*).", Toast.LENGTH_SHORT).show();
                    return;
                }

                //equals
                if (!email.equals(confirmEmail)) {
                    Toast.makeText(SignInActivity.this, "Emails do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignInActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ThreadWSNewCustomer thread = new ThreadWSNewCustomer();
                thread.execute(name, lastName, address, phoneNumber, postalCode, city, state, country, degree, email, username, password, companyName, RFC, position);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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


    public class ThreadWSSpinner extends AsyncTask<String, Integer, Boolean> {
        String newDataCustomer[];

        public Boolean doInBackground(String... params) {


            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://" + url + "/packetsWS/spinner.php");

            post.setHeader("content-type", "application/json");

            try {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("queryType", params[0]);


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                HttpEntity respStr = resp.getEntity();
                String respuesta = EntityUtils.toString(respStr);

                System.out.println(respuesta);
                //guardamos las
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
                setDataSpinnerDegree(newDataCustomer);

            } else {


            }


        }
    }

    public class ThreadWSNewCustomer extends AsyncTask<String, Integer, Boolean> {
        String newDataCustomer[];

        public Boolean doInBackground(String... params) {


            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://" + url + "/packetsWS/newCustomer.php");

            post.setHeader("content-type", "application/json");

            try {

                //Construimos el objeto cliente en formato JSON
                JSONObject datos = new JSONObject();


                datos.put("name", params[0]);
                datos.put("lastName", params[1]);
                datos.put("address", params[2]);
                datos.put("phoneNumber", params[3]);
                datos.put("postalCode", params[4]);
                datos.put("city", params[5]);
                datos.put("state", params[6]);
                datos.put("country", params[7]);
                datos.put("degree", params[8]);
                datos.put("email", params[9]);
                datos.put("username", params[10]);
                datos.put("password", params[11]);
                datos.put("companyName", params[12]);
                datos.put("rfc", params[13]);
                datos.put("position", params[14]);


                StringEntity entity = new StringEntity(datos.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                HttpEntity respStr = resp.getEntity();
                String respuesta = EntityUtils.toString(respStr);

                System.out.println("respuesta de servidor PHP "+respuesta);
                //guardamos las
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

                if (newDataCustomer[0] != "dupliUsername") {
                    resul = false;
                    //System.out.println("resul " + resul);
                }

                if(newDataCustomer[0].equals("true")){
                    resul=true;
                }

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        public void onPostExecute(Boolean result) {


            if (result == true) {
                Intent intent = new Intent();
                intent.setClass(SignInActivity.this, MainMenuActivity.class);
                intent.putExtra("username", txtUsername.getText().toString());
                intent.putExtra("url", url);
                startActivity(intent);

            }else{
                Toast.makeText(SignInActivity.this, "Sorry the username is already used.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
