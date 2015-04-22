package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;

public class ResumoDiarioActivity extends ActionBarActivity {
    TextView caloriasConsumidas;
    TextView caloriasRestante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_diario);
        Double iCaloriasConsumidas = 0.00;
        Double iCaloriasRestante   = 2000.00;

        iCaloriasConsumidas = caloriasDiariasConsumidas();
        iCaloriasRestante = iCaloriasRestante - iCaloriasConsumidas;

        //Formatando para duas casas decimais
        // DecimalFormat fmt = new DecimalFormat("0.00");
        //iCaloriasRestante = Double.valueOf(fmt.format(iCaloriasRestante));

        this.setTitle("Resumo Diário");

        caloriasConsumidas = (TextView) findViewById(R.id.textViewCaloriasConsumidas);
        caloriasConsumidas.setText("Calorias Consumidas: " + Double.toString(iCaloriasConsumidas));

        caloriasRestante   = (TextView) findViewById(R.id.textViewCaloriasRestantes);
        caloriasRestante.setText("Calorias Restante: " + Double.toString(iCaloriasRestante));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumo_diario, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_usuario:
                dadosUsuarioMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void dadosUsuarioMenu(){
        Intent intencao = new Intent(this, UsuarioActivity.class);
        startActivity(intencao);
    }

    public void consumoDiario(View v){
        Intent intencao = new Intent(this, ConsumoDiarioActivity.class);
        startActivity(intencao);
    }

    public void relatorio(View v){
        Intent intencao = new Intent(this, RelatorioActivity.class);
        startActivity(intencao);
    }


    private Double caloriasDiariasConsumidas(){
        Double calorias = 0.00;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String data = df.format(new Date());
        Cursor cursor = this.getContentResolver().query(
                DosadorContract.ConsumoEntry.buildConsumoPorData(data),
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );
        try {
            if (cursor.moveToFirst()) {
                do {
                    calorias = calorias + ( cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS) * cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_QTD))) );
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return calorias;

    }

}
