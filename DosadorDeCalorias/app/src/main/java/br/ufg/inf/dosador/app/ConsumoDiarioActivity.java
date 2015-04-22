package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.Consumo;

public class ConsumoDiarioActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_diario);

        this.setTitle("Consumo Diário");

        TextView calendario = (TextView) findViewById(R.id.textCalendario);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String data = df.format(new Date());
        calendario.setText(data);
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

    public void pesquisa(View v){
        String tipo = new String();

        switch (v.getId()) {
            case R.id.imageButtonCafe:
                tipo = "Café da Manhã";
                break;
            case R.id.imageButtonAlmoco:
                tipo = "Almoço";
                break;
            case R.id.imageButtonJantar:
                tipo = "Jantar";
                break;
            case R.id.imageButtonLanche:
                tipo = "Lanche";
                break;
        }

        Intent intencao = new Intent(this, PesquisaActivity.class);
        intencao.putExtra("tipo",tipo);
        startActivity(intencao);
    }

}
