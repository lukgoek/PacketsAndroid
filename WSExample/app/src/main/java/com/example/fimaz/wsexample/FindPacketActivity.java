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


public class FindPacketActivity extends Activity {
    public String url, username;
    public Button btnFindIt;
    public TextView lblMessage;
    public EditText txtGuideNumber;


    public void showLblMessage(String []data){
        lblMessage = (TextView)findViewById(R.id.lblMessage);

        lblMessage.setText("This shipment was sent by "+data[0]+" "+data[1]+".\nThe shipment type is "+data[2]+" to deliver to "+data[3]+" "+data[4]+" at "+data[5]+
                ".");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_packet);

        this.url = getIntent().getStringExtra("url");
        this.username = getIntent().getStringExtra("username");

        txtGuideNumber = (EditText) findViewById(R.id.txtGuideNumber);
        final String guideNumber = txtGuideNumber.getText().toString();


        btnFindIt = (Button) findViewById(R.id.btnFindIt);

        btnFindIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(txtGuideNumber.length());

                if (txtGuideNumber.length() < 8 || txtGuideNumber.length() > 8) {
                    Toast.makeText(FindPacketActivity.this, "Incorrect Number Guide length", Toast.LENGTH_SHORT).show();
                } else {
                    ThreadWSFindPackets obj = new ThreadWSFindPackets();
                    obj.execute(guideNumber);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_packet, menu);
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

    public class ThreadWSFindPackets extends AsyncTask<String, Integer, Boolean> {
        String newDataPacket[] = new String[15];
        public Boolean doInBackground(String... params) {

            boolean resul = false;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new
                    HttpPost("http://" + url + "/packetsWS/findPacket.php");

            post.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("guideNumber", params[0]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                HttpEntity respStr = resp.getEntity();
                String respuesta = EntityUtils.toString(respStr);

                System.out.println(respuesta);
                //guardamos las
                String dataPacket[] = respuesta.split(",");

                for (int i = 0; i < dataPacket.length; i++) {

                    if (dataPacket[i].equals("NULL") || dataPacket[i].equals("null")) {
                        dataPacket[i] = " ";
                    } else {

                        newDataPacket[i] = dataPacket[i].replaceAll("[^A-Za-z0-9-()#. ]", "");

                    }
                    System.out.println("new data"+newDataPacket[i]);
                }

                if (!newDataPacket[0].equals("null")) {
                    resul = true;
                    System.out.println("resul " + resul);
                }

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        public void onPostExecute(Boolean result) {


            if (result == true) {
                showLblMessage(newDataPacket);

            }


        }

    }
}
