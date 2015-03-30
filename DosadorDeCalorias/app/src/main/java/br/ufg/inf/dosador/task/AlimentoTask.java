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

import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 27/03/2015.
 */
///*
// *  Primeiro callback é parametro do método doInBackground
// *  Segundo callback é parametro do método onProgressUpdate.
// *  Terceiro callback é parametro do método onPostExecute e retorno do método doInBackground.
// */
//public class AlimentoTask extends AsyncTask<String, Void, Alimento> {
//
//    final static private String LOG_CAT = AlimentoTask.class.getSimpleName();
//    private final Context mContext;
//
//    public AlimentoTask(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    protected Alimento doInBackground(String... params) {
//
//        Alimento alimento = new Alimento();
//
//        if (params.length == 0) {
//            return alimento;
//        }
//
//        URL url = null;
//        FatSecret fatSecret = new FatSecret(mContext);
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain the raw JSON response as a string.
//        String foodJsonStr = null;
//
//        try {
//
//            if (params[0].contains(FatSecret.METHOD_FOOD_GET)) {
//                url = FatSecret.pesquisarAlimentoPorID(params[1]);
//            }
//
//            if (url == null) {
//                return null;
//            }
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//            if (buffer.length() == 0) {
//                return null;
//            }
//            foodJsonStr = buffer.toString();
//
//            Log.d(LOG_CAT + " ResultadoJSON: ", foodJsonStr);
//
//            Json json = new Json();
//            alimento = json.obterAlimentoFromJson(foodJsonStr);
//
//            return alimento;
//
//        } catch (IOException e) {
//            Log.e(LOG_CAT, "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attemping
//            // to parse it.
//            //return null;
//        }
////        catch (JSONException e) {
////            Log.e(LOG_CAT, e.getMessage(), e);
////            e.printStackTrace();
////        }
//
//        catch (Exception e) {
//            Log.e(LOG_CAT, e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_CAT, "Error closing stream", e);
//                }
//            }
//        }
//        return alimento;
//    }
//}