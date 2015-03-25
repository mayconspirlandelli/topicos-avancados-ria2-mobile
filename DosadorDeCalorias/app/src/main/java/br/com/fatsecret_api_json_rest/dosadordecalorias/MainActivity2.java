
//http://term.ie/oauth/example/client.php
//https://github.com/ethan-james/cookbox/tree/master/src/com/vitaminc4/cookbox

package br.com.fatsecret_api_json_rest.dosadordecalorias;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import fatsecret.platform.FatSecretAPI;
import fatsecret.platform.FatSecretAuth;

public class MainActivity2 extends ActionBarActivity {

    HttpClient client;
    EditText searchText;
    Button searchButton;
    String q="";
    String encoder="";
    String signatureString="";
    String encodedSignString="";
    String finalSignatureString="";
    String key="";
    String hmacKey="";
    JSONObject json;

    final static String URL="http://platform.fatsecret.com/rest/server.api";
    //    final static String SIGN="a=foo&oauth_consumer_key=demo&oauth_nonce=abc&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345678&oauth_version=1.0&z=bar";
    final static String OAUTH_CONSUMER_KEY="131418e143734b9e8496f811c793ff5b";
    final static String OAUTH_ACCESS_SECRET="623fe615f74d4c97921a25639bb2a92e";
    final static String OAUTH_SIGNATURE_METHOD="HMAC-SHA1";
    final static String OAUTH_VERSION="1.0";
    final static String ENCODED_SIGN_STRING="a%3Dbar%26%26oauth_consumer_key%3Ddemo%26oauth_nonce%3Dabc%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D12345678%26oauth_version%3D1.0%26z%3Dbar";
    static String OAUTH_TIMESTAMP="";
    static String OAUTH_NOUNCE="shreyas";
    final static String a="foo";
    final static String z="bar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static String getCurrentTimeStamp(){
        try {
            Date date = new Date();
            String currentTimeStamp = String.valueOf(date.getTime());
            Log.d("CALORIAS_currentTimeStamp:", currentTimeStamp);
            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void initialize() {
        searchText=(EditText)findViewById(R.id.etSearch);
        searchButton=(Button)findViewById(R.id.bSearch);

        client=new DefaultHttpClient();
        //long timestamp=(System.currentTimeMillis() / 1000);

//        OAUTH_TIMESTAMP=timestamp+"";
//        OAUTH_NOUNCE += timestamp;

        OAUTH_TIMESTAMP = getCurrentTimeStamp()+"";
        OAUTH_NOUNCE += getCurrentTimeStamp();
        Log.d("CALORIAS_TIMESTAMP:", OAUTH_TIMESTAMP);

        try {
            encoder=URLEncoder.encode(URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("CALORIAS_URL ENCODED:", encoder);

        Map<String, String> params=new HashMap<String, String>();

        params.put("oauth_consumer_key", OAUTH_CONSUMER_KEY);
        params.put("oauth_signature_method", OAUTH_SIGNATURE_METHOD);
        params.put("oauth_timestamp", OAUTH_TIMESTAMP);
        params.put("oauth_nonce", OAUTH_NOUNCE);
        params.put("oauth_version", OAUTH_VERSION);
//		params.put("a", a);
//		params.put("z", z);
        //params.put("search_expression", "banana");
        //params.put("method", "foods.search");
        params.put("food_id", "33691");
        params.put("method", "foods.get");


        Map<String, String> sortedMap=new TreeMap<String, String>(params);
        printMap(sortedMap);
        String sigString=concatenate(sortedMap);
        Log.d("CALORIAS_CONCATENATED STRING :", sigString);

        encodedSignString=encodeSignatureString(sigString);
        Log.d("CALORIAS_ORIGINAL ENCODED STRING:", ENCODED_SIGN_STRING);
        Log.d("CALORIAS_MY ENCODED STRING:", encodedSignString);

        //finalSignatureString="POST" + "&" + encoder + "&" + encodedSignString;
        finalSignatureString="GET" + "&" + encoder + "&" + encodedSignString;

        Log.d("CALORIAS_FINAL SIGN STRING:", finalSignatureString);

        //acho que está errado.
        key=OAUTH_CONSUMER_KEY + "&" + OAUTH_ACCESS_SECRET;


        hmacKey=computeHmac("HmacSHA1", finalSignatureString, key);
        hmacKey=encodeBase(hmacKey);
        try{
            hmacKey=URLEncoder.encode(hmacKey, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("CALORIAS_HMAC KEY:", hmacKey);
        Log.d("CALORIAS_DONE:", "Done with initialisaton");

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                q=searchText.getText().toString();
                searchFood(q);
            }
        });
    }

    private String encodeBase(String hmacKey2) {
        byte[] data=null;
        try {
            data=hmacKey2.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        String base64= Base64.encodeToString(data, Base64.DEFAULT);
        Log.d("CALORIAS_Base 64", base64);
        return base64;

    }

    /*  private String computeHmac(String type, String value, String key) {
          try{
              Mac mac=Mac.getInstance(type);
              SecretKeySpec secret=new SecretKeySpec(key.getBytes(), type);
              mac.init(secret);

              mac.update(value.getBytes());

              byte[] bytes = mac.doFinal();

              String sig = Base64.encodeToString(bytes, 0);
              sig = URLEncoder.encode(sig,"UTF-8");

              Log.d("CALORIAS_Results: ", sig);

              return sig;
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          return "";
      }*/
    private String computeHmac(String type, String value, String key) {
        try{
            Mac mac=Mac.getInstance(type);
            SecretKeySpec secret=new SecretKeySpec(key.getBytes(), type);
            mac.init(secret);
            byte[] bytes = mac.doFinal(value.getBytes());
            Log.d("CALORIAS_Results: ", bytes.toString());
            return new String(Base64.encode(bytes,0));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String encodeSignatureString(String sigString) {
        String encodedSignString="";
        try {
            encodedSignString=URLEncoder.encode(sigString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedSignString;
    }

    private String concatenate(Map<String, String> sortedMap) {
        String signatureString="";
        Set s=sortedMap.entrySet();
        Iterator i=s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String key=(String)entry.getKey();
            String val=(String)entry.getValue();
            signatureString+=key + "=" + val +"&";
        }
        return signatureString.substring(0, (signatureString.length()-1));
    }

    private void printMap(Map<String, String> sortedMap) {
        Set s=sortedMap.entrySet();
        Iterator i=s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String key=(String)entry.getKey();
            String val=(String)entry.getValue();
            Log.d("CALORIAS_SORTED :", key + "=" + val);
        }
    }


    public void searchFood(String q) {
        final String searchKeyword=q;

        AsyncTask<String, Integer, String> myAsyncTask = new AsyncTask<String, Integer, String>()
        {

            @Override
            protected String doInBackground(String... params) {
                try{

                    //String requestString=URL + "?search_expression=" +searchKeyword + "&format=json" + "&" + "oauth_consumer_key="+OAUTH_CONSUMER_KEY + "&" + "oauth_signature_method="+OAUTH_SIGNATURE_METHOD + "&" + "oauth_timestamp="+Long.parseLong(OAUTH_TIMESTAMP) + "&" + "oauth_nonce="+OAUTH_NOUNCE + "&" + "oauth_version="+OAUTH_VERSION + "&" + "oauth_signature="+hmacKey + "&" + "method=foods.search";
                    String requestString=URL + "?food_id=" + "33691" + "&"
                            + "method=" + "food.get" + "&"
                            + "method=" + "food.get" + "&"
                            + "format=json" + "&"
                            + "oauth_consumer_key="+OAUTH_CONSUMER_KEY + "&"
                            + "oauth_nonce="+OAUTH_NOUNCE + "&"
                            + "oauth_signature="+hmacKey + "&"
                            + "oauth_signature_method="+OAUTH_SIGNATURE_METHOD + "&"
                            + "oauth_timestamp="+Long.parseLong(OAUTH_TIMESTAMP) + "&"
                            + "oauth_version="+OAUTH_VERSION;


                    //teste
                    //FatSecret api = new FatSecret();
                    //requestString = FatSecret.getFood("banana");
                    Log.d("CALORIAS_GETFOOD: ", requestString);


                    java.net.URL url=new java.net.URL(requestString);
                    Log.d("CALORIAS_FINAL REQUEST STRING:", requestString);
                    StringBuilder sb=new StringBuilder(requestString);
                    StringBuilder foodRequest=new StringBuilder();
                    HttpGet get=new HttpGet(url.toString());
                    HttpResponse r= client.execute(get);
                    Log.d("CALORIAS_EXECUTED:", "Executed post successfully!");
                    int status=r.getStatusLine().getStatusCode();
                    Log.d("CALORIAS_STATUS:", status+"");

                    if(status >= 200 && status < 300)
                    {
                        BufferedReader br=new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
                        String t;
                        while((t = br.readLine())!=null)
                        {
                            foodRequest.append(t);
                        }
                        Log.d("", foodRequest.toString());
                        json=new JSONObject(foodRequest.toString());
                        Log.d("CALORIAS_GOT:", "GOT JSON OBJECT");
                    }
                }
                catch (JSONException e) {
                    Log.e("CALORIAS_ERROR:", "This is not a valid JSON request");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        };
        myAsyncTask.execute();

    }

}
