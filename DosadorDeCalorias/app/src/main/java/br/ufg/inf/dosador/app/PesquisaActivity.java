package br.ufg.inf.dosador.app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.adapter.AlimentoListAdapter;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;

public class PesquisaActivity extends ActionBarActivity {

    private ListView listView;
    public AlimentoListAdapter alimentoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializaObjetosDeTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pesquia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializaObjetosDeTela() {
        listView = (ListView) findViewById(R.id.listaAlimentos);
        listView.setFastScrollEnabled(true); //Habilita o scroll.

        //Cria o Adapter.
        alimentoListAdapter = new AlimentoListAdapter(PesquisaActivity.this);
        //Define o Adapater.
        listView.setAdapter(alimentoListAdapter);

        pesquisarAlimento("sfasfasfa");

    }

//    public void exibirDadosListView(ArrayList<Alimento> listaAlimentos) {
//        //Cria o Adapter.
//        alimentoListAdapter = new AlimentoListAdapter(PesquisaActivity.this, listaAlimentos);
//        //Define o Adapater.
//        listView.setAdapter(alimentoListAdapter);
//    }

    private void pesquisarAlimento(String q) {
        DosadorTask dt = new DosadorTask(this);
        dt.execute(br.ufg.inf.dosador.api.FatSecret.METHOD_FOODS_SEARCH, "arroz");
        //dt.execute(br.ufg.inf.dosador.api.FatSecret.METHOD_FOOD_GET, "35755");
    }


    /*
 *  Primeiro callback é parametro do método doInBackground
 *  Segundo callback é parametro do método onProgressUpdate.
 *  Terceiro callback é parametro do método onPostExecute e retorno do método doInBackground.
 */
    public class DosadorTask extends AsyncTask<String, Void, ArrayList<Alimento>> {

        final private String LOG_CAT = DosadorTask.class.getSimpleName();
        private final Context mContext;

        public DosadorTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Alimento> doInBackground(String... params) {

            ArrayList<Alimento> listaDeAlimentos = new ArrayList<Alimento>();

            if (params.length == 0) {
                return listaDeAlimentos;
            }

            URL url = null;
            FatSecret fatSecret = new FatSecret(mContext);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String foodJsonStr = null;

            try {

                if (params[0].contains(FatSecret.METHOD_FOODS_SEARCH)) {
                    url = FatSecret.pesquisarAlimentoPorExpressao(params[1]);
                } else if (params[0].contains(FatSecret.METHOD_FOOD_GET)) {
                    url = FatSecret.pesquisarAlimentoPorID(params[1]);
                }

                if (url == null) {
                    return null;
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                foodJsonStr = buffer.toString();

                Log.d(LOG_CAT + " ResultadoJSON: ", foodJsonStr);

                Json json = new Json();

                if (params[0].contains(FatSecret.METHOD_FOODS_SEARCH)) {
                    listaDeAlimentos = json.obterListaAlimentoFromJson(foodJsonStr);
                } else if (params[0].contains(FatSecret.METHOD_FOOD_GET)) {
                    listaDeAlimentos.add(json.obterAlimentoFromJson(foodJsonStr));
                }

                return listaDeAlimentos;

            } catch (IOException e) {
                Log.e(LOG_CAT, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                //return null;
            }
//        catch (JSONException e) {
//            Log.e(LOG_CAT, e.getMessage(), e);
//            e.printStackTrace();
//        }

            catch (Exception e) {
                Log.e(LOG_CAT, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_CAT, "Error closing stream", e);
                    }
                }
            }
            return listaDeAlimentos;
        }

        @Override
        protected void onPostExecute(ArrayList<Alimento> result) {
            super.onPostExecute(result);
            alimentoListAdapter.setListaAlimentos(result);
            alimentoListAdapter.notifyDataSetChanged();
        }
    }
}
