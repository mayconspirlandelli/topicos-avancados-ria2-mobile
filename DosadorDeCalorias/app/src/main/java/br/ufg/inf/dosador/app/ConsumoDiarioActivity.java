package br.ufg.inf.dosador.app;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.Consumo;

//TODO: somente para teste.
public class ConsumoDiarioActivity extends ActionBarActivity {

    private EditText editDescricao;
    private TextView txtCalorias;
    private TextView txtGorduras;
    private TextView txtCarboidratos;
    private TextView txtProteinas;
    private EditText editQuantidade;

    @Override
    protected void onResume() {
        super.onResume();
        inicializaObjetosDeTela();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_diario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consumo_diario, menu);
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
        editQuantidade = (EditText) findViewById(R.id.edit_quantidade);

        exibirDados();

    }
    private void exibirDados(){
        String data = "2015-04-07";
        Cursor cursor = this.getContentResolver().query(
                DosadorContract.ConsumoEntry.buildConsumoPorData(data),
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );
        Consumo consumo = construirConsumoDiarioPorCursor(cursor);

        editDescricao.setText(consumo.getNomeAlimento());
        txtCalorias.setText(consumo.getCalorias().toString());
        txtGorduras.setText(consumo.getGorduras().toString());
        txtCarboidratos.setText(consumo.getCarboidratos().toString());
        txtProteinas.setText(consumo.getProteinas().toString());

    }

    private Consumo construirConsumoDiarioPorCursor(Cursor cursor) {
        Consumo consumo = new Consumo();

        try {
            if (cursor.moveToFirst()) {
                do {

                    consumo.setNomeAlimento(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
                    consumo.setCalorias(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS)));
                    consumo.setGorduras(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_GORDURA)));
                    consumo.setCarboidratos(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO)));
                    consumo.setProteinas(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_PROTEINA)));
                    //consumo.setData(new Date(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA))));
                    consumo.setQuantidade(cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_QTD)));
                    //consumo.setTipoRefeicao();

                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return consumo;
    }

}
