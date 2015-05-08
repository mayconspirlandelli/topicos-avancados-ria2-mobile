package br.ufg.inf.dosador.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import br.ufg.inf.dosador.app.IDosadorUpdater;
import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 25/03/2015.
 */
/*
 *  Primeiro callback é parametro do método doInBackground
 *  Segundo callback é parametro do método onProgressUpdate.
 *  Terceiro callback é parametro do método onPostExecute e retorno do método doInBackground.
 */
public class ListaAlimentoTask extends AsyncTask<String, Void, ArrayList<Alimento>> {

    final static private String LOG_CAT = ListaAlimentoTask.class.getSimpleName();
    private final AlimentoListAdapter adapterListAlimento;
    private final Context mContext;
    private IDosadorUpdater dosadorUpdater;

    public ListaAlimentoTask(Context context, AlimentoListAdapter adapter, IDosadorUpdater dosadorUpdater) {
        this.mContext = context;
        this.adapterListAlimento = adapter;
        this.dosadorUpdater = dosadorUpdater;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dosadorUpdater.showProgress();
    }

    @Override
    protected ArrayList<Alimento> doInBackground(String... params) {

        ArrayList<Alimento> listaDeAlimentos = new ArrayList<Alimento>();

        if (params.length == 0) {
            return  listaDeAlimentos;
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
            }

            listaDeAlimentos = json.obterListaAlimentoFromJson(foodJsonStr);

            return listaDeAlimentos;

        } catch (IOException e) {
            Log.e(LOG_CAT, "Error ", e);
        }
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
    protected void onPostExecute(ArrayList<Alimento> alimentos) {
        super.onPostExecute(alimentos);
        this.dosadorUpdater.hideProgress();
        adapterListAlimento.setListaAlimentos(alimentos);
        adapterListAlimento.notifyDataSetChanged();
        if(alimentos == null || alimentos.size() <= 0) {
            this.dosadorUpdater.exibeMensagem(mContext.getString(R.string.msn_info_alimento));
        }
    }

}

