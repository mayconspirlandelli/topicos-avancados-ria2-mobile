
//http://term.ie/oauth/example/client.php
//https://github.com/ethan-james/cookbox/tree/master/src/com/vitaminc4/cookbox

package br.ufg.inf.dosador.app;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.gson.Gson;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.temp.FatSecret;
import br.ufg.inf.dosador.temp.FoodListAdapter;
import br.ufg.inf.dosador.temp.Root;


public class MainActivity extends ActionBarActivity {

    HttpClient client;
    EditText searchText;
    Button searchButton;
    TextView txtResultado;
    TextView txtFoodId;
    TextView txtFoodName;

    String foodId;
    String foodName;

    String q = "";
    JSONObject json;

    private ListView listView;
    private FoodListAdapter adapterListFood;


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

    public void initialize() {
        searchText = (EditText) findViewById(R.id.etSearch);
        searchButton = (Button) findViewById(R.id.btn_pesquisar);
        txtResultado = (TextView) findViewById(R.id.txtResultado);
        txtFoodId = (TextView) findViewById(R.id.food_id);
        txtFoodName = (TextView) findViewById(R.id.food_name);

        listView = (ListView) findViewById(R.id.listaSublocais);
        listView.setFastScrollEnabled(true); //Habilita o scroll.

        client = new DefaultHttpClient();

        searchButton.setOnClickListener(searchButtonOnClickListener);


        /*searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q=searchText.getText().toString();
                searchFood(q);
            }
        });*/
    }

    final private View.OnClickListener searchButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            q = searchText.getText().toString();
            // searchFood(q);
            // pesquisarAlimento(q);
            txtFoodId.setText(foodId);
            txtFoodName.setText(foodName);

            abrirTelaPesquisa();
        }
    };

    public void pesquisarAlimento(String q) {
       // ListaAlimentoTask dt = new ListaAlimentoTask(this);
        //dt.execute(br.ufg.inf.dosador.api.FatSecret.METHOD_FOODS_SEARCH, "arroz");
       // dt.execute(br.ufg.inf.dosador.api.FatSecret.METHOD_FOOD_GET, "35755");
    }

    private void abrirTelaPesquisa(){
        Intent intent = new Intent(MainActivity.this, PesquisaActivity.class);
        startActivity(intent);
    }


    public void searchFood(final String q) {
        final String searchKeyword = q;

        AsyncTask<String, Integer, String> myAsyncTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {

                    FatSecret api = new FatSecret();
                    //br.ufg.inf.dosador.api.FatSecret api = new br.ufg.inf.dosador.api.FatSecret(this);
                    String requestString = FatSecret.getFood(q).toString();
                    Log.d("CALORIAS_GETFOOD: ", requestString);


                    java.net.URL url = new java.net.URL(requestString);
                    Log.d("CALORIAS_FINAL REQUEST STRING:", requestString);

                    StringBuilder sb = new StringBuilder(requestString);
                    StringBuilder foodRequest = new StringBuilder();

                    HttpGet get = new HttpGet(url.toString());
                    HttpResponse r = client.execute(get);

                    Log.d("CALORIAS_EXECUTED:", "Executed post successfully!");

                    int status = r.getStatusLine().getStatusCode();
                    Log.d("CALORIAS_STATUS:", status + "");

                    if (status >= 200 && status < 300) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
                        String t;
                        while ((t = br.readLine()) != null) {
                            foodRequest.append(t + "\n");
                        }
                        br.close();

                        Log.d("CALORIAS_VALORES:", foodRequest.toString());
                        Log.d("CALORIAS_GOT:", "GOT JSON OBJECT");


                        Gson gson = new Gson();
                        Root root = gson.fromJson(foodRequest.toString(), Root.class);
                        foodId = root.getFood().getFood_id();
                        foodName = root.getFood().getFood_name();


                        //Cria o adapter
                        //adapterListFood = new FoodListAdapter(MainActivity.this, root.getFood().getServings().getServing());
                        //Define o Adapter
                        // listView.setAdapter(adapterListFood);

                        Log.d("CALORIAS_TESTE:", "teste");



/*
                        Gson gson = new Gson();
                        Food food = gson.fromJson(br,Food.class);

                        ArrayList<Food> foods = new ArrayList<Food>();
                        foods.add(food);
*/

                        /*
                        ArrayList<Food> foods = new ArrayList<Food>();
                        JsonParser parser = new JsonParser();
                        JsonArray array = parser.parse(foodRequest.toString()).getAsJsonArray();


                        for (int i = 0; i < array.size(); i++) {
                            foods.add(gson.fromJson(array.get(i), Food.class));
                        }


*/
                        //Cria o adapter
    /*                    adapterListFood = new FoodListAdapter(MainActivity.this, foods);
                        //Define o Adapter
                        listView.setAdapter(adapterListFood);

*/

                        //   txtResultado.setText(foodRequest.toString());

                    }
                } catch (Exception e) {
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