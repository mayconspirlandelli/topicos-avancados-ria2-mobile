package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.adapter.AlimentoListAdapter;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;

public class PesquisaActivity extends ActionBarActivity {

    private ListView listView;
    public AlimentoListAdapter adapterListAlimento;

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
        listView.setOnItemClickListener(clickListaItemAlimento);

        //Cria o Adapter.
        adapterListAlimento = new AlimentoListAdapter(PesquisaActivity.this);
        //Define o Adapater.
        listView.setAdapter(adapterListAlimento);

        pesquisarAlimento("sfasfasfa");
    }

    private void pesquisarAlimento(String q) {
        ListaAlimentoTask task = new ListaAlimentoTask(this, adapterListAlimento);
        task.execute(FatSecret.METHOD_FOODS_SEARCH, "arroz");
        //task.execute(FatSecret.METHOD_FOOD_GET, "35755");
    }

    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Alimento alimento = (Alimento) adapterListAlimento.getItem(position);
            abrirTelaDetalhesPesquisaActivity(alimento);
        }
    };

    private void abrirTelaDetalhesPesquisaActivity(Alimento alimento) {
        Intent intent = new Intent(PesquisaActivity.this, DetalhesPesquisaActivity.class);
        intent.putExtra(Json.FOOD_ID, alimento.getFood_id());

        //Passar somente o FOOD_ID

//        intent.putExtra(Json.CALORIES, alimento.getCalories());
//        intent.putExtra(Json.FAT, alimento.getFat());
//        intent.putExtra(Json.CARBOHYDRATE, alimento.getCarbohydrate());
//        intent.putExtra(Json.PROTEIN, alimento.getProtein());
        startActivity(intent);
    }
}
