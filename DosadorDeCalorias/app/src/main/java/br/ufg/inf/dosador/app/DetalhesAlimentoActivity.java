package br.ufg.inf.dosador.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;

public class DetalhesAlimentoActivity extends ActionBarActivity implements IDosadorUpdater {

    private RelativeLayout rlEsquerdo;
    private RelativeLayout rlDireito;
    private TextView txtPorcao;
    private TextView txtCalorias;
    private TextView txtGorduras;
    private TextView txtCarboidratos;
    private TextView txtProteinas;
    private TextView txtGorduraSatura;
    private TextView txtGorduraMonoinsaturada;
    private TextView txtGorduraPoliinsaturada;
    private TextView txtGorduraTrans;
    private TextView txtColesterol;
    private TextView txtSodio;
    private TextView txtFibras;
    private TextView txtPotassio;
    private TextView txtAcucar;
    private TextView txtVitaminaA;
    private TextView txtVitaminaC;
    private TextView txtCalcio;
    private TextView txtFerro;


    @Override
    protected void onStart() {
        super.onStart();
        inicializaObjetosDeTela();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_alimento);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_alimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inicializaObjetosDeTela() {

        rlEsquerdo = (RelativeLayout) findViewById(R.id.grid_esquerda);
        rlDireito = (RelativeLayout) findViewById(R.id.grid_direita);

        txtPorcao = (TextView) findViewById(R.id.grid_line_porcao_valor);
        txtCalorias = (TextView) findViewById(R.id.grid_line_calorias_valor);
        txtGorduras = (TextView) findViewById(R.id.grid_line_gorduras_valor);
        txtCarboidratos = (TextView) findViewById(R.id.grid_line_carboidratos_valor);
        txtProteinas = (TextView) findViewById(R.id.grid_line_proteinas_valor);
        txtGorduraSatura = (TextView) findViewById(R.id.grid_line_gordura_saturada_valor);
        txtGorduraMonoinsaturada = (TextView) findViewById(R.id.grid_line_gordura_monoinsaturada_valor);
        txtGorduraPoliinsaturada = (TextView) findViewById(R.id.grid_line_gordura_poliinsaturada_valor);
        txtGorduraTrans = (TextView) findViewById(R.id.grid_line_gordura_trans_valor);
        txtColesterol = (TextView) findViewById(R.id.grid_line_colesterol_valor);
        txtSodio = (TextView) findViewById(R.id.grid_line_sodio_valor);
        txtFibras = (TextView) findViewById(R.id.grid_line_fibras_valor);
        txtPotassio = (TextView) findViewById(R.id.grid_line_potassio_valor);
        txtAcucar = (TextView) findViewById(R.id.grid_line_acucar_valor);
        txtVitaminaA = (TextView) findViewById(R.id.grid_line_vitamina_a_valor);
        txtVitaminaC = (TextView) findViewById(R.id.grid_line_vitamina_c_valor);
        txtCalcio = (TextView) findViewById(R.id.grid_line_calcio_valor);
        txtFerro = (TextView) findViewById(R.id.grid_line_ferro_valor);

        Bundle valoresEntreActivity = getIntent().getExtras();
        int id = valoresEntreActivity.getInt(Json.FOOD_ID);

        AlimentoTask task = new AlimentoTask(this, this);
        task.execute(FatSecret.METHOD_FOOD_GET, String.valueOf(id));

    }

    @Override
    public void hideProgress() {
        rlEsquerdo.setVisibility(View.VISIBLE);
        rlDireito.setVisibility(View.VISIBLE);
        ProgressBar progress = (ProgressBar) this.findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        rlEsquerdo.setVisibility(View.INVISIBLE);
        rlDireito.setVisibility(View.INVISIBLE);
        ProgressBar progress = (ProgressBar) this.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void exibeMensagem(String mensagem) {
        Toast.makeText(DetalhesAlimentoActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    /*
         *  Primeiro callback é parametro do método doInBackground
         *  Segundo callback é parametro do método onProgressUpdate.
         *  Terceiro callback é parametro do método onPostExecute e retorno do método doInBackground.
         */
    private class AlimentoTask extends AsyncTask<String, Void, Alimento> {

        final private String LOG_CAT = AlimentoTask.class.getSimpleName();
        private final Context mContext;
        private IDosadorUpdater dosadorUpdater;

        public AlimentoTask(Context context, IDosadorUpdater dosador) {
            this.mContext = context;
            this.dosadorUpdater = dosador;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dosadorUpdater.showProgress();
        }

        @Override
        protected Alimento doInBackground(String... params) {

            Alimento alimento = new Alimento();

            if (params.length == 0) {
                return alimento;
            }

            URL url = null;
            FatSecret fatSecret = new FatSecret(mContext);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String foodJsonStr = null;

            try {

                if (params[0].contains(FatSecret.METHOD_FOOD_GET)) {
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
                alimento = json.obterAlimentoFromJson(foodJsonStr);

                return alimento;

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
            return alimento;
        }

        @Override
        protected void onPostExecute(Alimento alimento) {
            super.onPostExecute(alimento);
            this.dosadorUpdater.hideProgress();
            if (alimento != null) {
                txtPorcao.setText(alimento.getServing_description());
                txtCalorias.setText(alimento.getCalories().toString() + Json.UNIDADE_QUILO_CALORIAS);
                txtGorduras.setText(alimento.getFat().toString() + Json.UNIDADE_GRAMAS);
                txtCarboidratos.setText(alimento.getCarbohydrate().toString() + Json.UNIDADE_GRAMAS);
                txtProteinas.setText(alimento.getProtein().toString() + Json.UNIDADE_GRAMAS);
                txtGorduraSatura.setText(alimento.getSaturated_fat().toString() + Json.UNIDADE_GRAMAS);
                txtGorduraMonoinsaturada.setText(alimento.getMonounsaturated_fat().toString() + Json.UNIDADE_GRAMAS);
                txtGorduraPoliinsaturada.setText(alimento.getPolyunsaturated_fat().toString() + Json.UNIDADE_GRAMAS);
                txtGorduraTrans.setText(alimento.getTrans_fat().toString() + Json.UNIDADE_GRAMAS);
                txtColesterol.setText(alimento.getCholesterol().toString() + Json.UNIDADE_MILIGRAMAS);
                txtSodio.setText(alimento.getSodium().toString() + Json.UNIDADE_MILIGRAMAS);
                txtFibras.setText(alimento.getFiber().toString() + Json.UNIDADE_GRAMAS);
                txtPotassio.setText(alimento.getPotassium().toString() + Json.UNIDADE_MILIGRAMAS);
                txtAcucar.setText(alimento.getSugar().toString() + Json.UNIDADE_GRAMAS);
                txtVitaminaA.setText(alimento.getVitamin_a().toString() + Json.UNIDADE_PORCENTAGEM);
                txtVitaminaC.setText(alimento.getVitamin_c().toString() + Json.UNIDADE_PORCENTAGEM);
                txtCalcio.setText(alimento.getCalcium().toString() + Json.UNIDADE_PORCENTAGEM);
                txtFerro.setText(alimento.getIron().toString() + Json.UNIDADE_PORCENTAGEM);
            } else {
                this.dosadorUpdater.exibeMensagem(mContext.getString(R.string.msn_info_alimento));
            }
        }
    }

}
