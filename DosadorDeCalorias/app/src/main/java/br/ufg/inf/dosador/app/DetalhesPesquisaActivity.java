package br.ufg.inf.dosador.app;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.api.Json;

public class DetalhesPesquisaActivity extends ActionBarActivity {

    private EditText editDescricao;
    private TextView txtCalorias;
    private TextView txtGorduras;
    private TextView txtCarboidratos;
    private TextView txtProteinas;
    private EditText editPorcao;
    private Button btnMaisDetalhes;


    @Override
    protected void onResume() {
        super.onResume();
        inicializaObjetosDeTela();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pesquisa);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_pesquisa, menu);
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

    private void inicializaObjetosDeTela() {

        editDescricao = (EditText) findViewById(R.id.edit_descricao);
        txtCalorias = (TextView) findViewById(R.id.txt_calorias_valor);
        txtGorduras = (TextView) findViewById(R.id.txt_gorduras_valor);
        txtCarboidratos = (TextView) findViewById(R.id.txt_carboidratos_valor);
        txtProteinas = (TextView) findViewById(R.id.txt_proteinas_valor);
        editPorcao = (EditText) findViewById(R.id.edit_porcao);
        btnMaisDetalhes = (Button) findViewById(R.id.btn_mais_detalhes);
        btnMaisDetalhes.setOnClickListener(btnMaisDetalhesOnClickListener);

        Bundle valoresEntreActivity = getIntent().getExtras();
        int id = valoresEntreActivity.getInt(Json.FOOD_ID);
        String nome = valoresEntreActivity.getString(Json.FOOD_NAME);
        String porcao = valoresEntreActivity.getString(Json.SERVING_DESCRIPTION);
        Double calorias = valoresEntreActivity.getDouble(Json.CALORIES);
        Double gorduras = valoresEntreActivity.getDouble(Json.FAT);
        Double carbs = valoresEntreActivity.getDouble(Json.CARBOHYDRATE);
        Double proteinas = valoresEntreActivity.getDouble(Json.PROTEIN);

        editDescricao.setText(nome);
        txtCalorias.setText(calorias.toString());
        txtGorduras.setText(gorduras.toString());
        txtCarboidratos.setText(carbs.toString());
        txtProteinas.setText(proteinas.toString());
        editPorcao.setText(porcao);

    }

    final private View.OnClickListener btnMaisDetalhesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mostrarMaisDetalhes();
        }
    };


    private void mostrarMaisDetalhes() {
        Bundle valoresEntreActivity = getIntent().getExtras();
        int id = valoresEntreActivity.getInt(Json.FOOD_ID);

        Intent intent = new Intent(DetalhesPesquisaActivity.this, DetalhesAlimentoActivity.class);
        intent.putExtra(Json.FOOD_ID, id);
        startActivity(intent);
    }
}
