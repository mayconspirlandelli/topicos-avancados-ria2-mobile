package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.adapter.AlimentoListAdapter;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.task.ListaAlimentoTask;

public class PesquisaActivity extends ActionBarActivity {

    private EditText editPesquisa;
    private ImageButton btnPesquisar;
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

        editPesquisa = (EditText) findViewById(R.id.edit_pesquisa);
        btnPesquisar = (ImageButton) findViewById(R.id.btn_pesquisar);
        btnPesquisar.setOnClickListener(btnPesquisarOnClickListener);

        listView = (ListView) findViewById(R.id.listaAlimentos);
        listView.setFastScrollEnabled(true); //Habilita o scroll.
        listView.setOnItemClickListener(clickListaItemAlimento);

        //Cria o Adapter.
        adapterListAlimento = new AlimentoListAdapter(PesquisaActivity.this);
        //Define o Adapater.
        listView.setAdapter(adapterListAlimento);
    }

    final private View.OnClickListener btnPesquisarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pesquisarAlimento(editPesquisa.getText().toString());
        }
    };

    private void pesquisarAlimento(String expressao) {
        ListaAlimentoTask task = new ListaAlimentoTask(this, adapterListAlimento);
        task.execute(FatSecret.METHOD_FOODS_SEARCH, expressao);
        //task.execute(FatSecret.METHOD_FOODS_SEARCH, "arroz");
        //task.execute(FatSecret.METHOD_FOOD_GET, "35755");
    }

    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Alimento alimento = (Alimento) adapterListAlimento.getItem(position);
            abrirTelaDetalhesPesquisaActivity(alimento);
        }
    };

    private void abrirTelaDetalhesPesquisaActivity(Alimento ali) {

        Alimento alimento = obterDadosFromDescription(ali);

        Intent intent = new Intent(PesquisaActivity.this, DetalhesPesquisaActivity.class);
        intent.putExtra(Json.FOOD_ID, alimento.getFood_id());
        intent.putExtra(Json.FOOD_NAME,alimento.getFood_name());
        intent.putExtra(Json.SERVING_DESCRIPTION,alimento.getServing_description());
        intent.putExtra(Json.CALORIES, alimento.getCalories());
        intent.putExtra(Json.FAT, alimento.getFat());
        intent.putExtra(Json.CARBOHYDRATE, alimento.getCarbohydrate());
        intent.putExtra(Json.PROTEIN, alimento.getProtein());
        startActivity(intent);
    }

    public Alimento obterDadosFromDescription(Alimento alimento) {
        String description = alimento.getFood_description();
        //Exemplo "Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g"
        if(!description.isEmpty()) {
            String[] temp = description.split("-");
            String[] temp2 = temp[1].split("\\|");

            String calories =  temp2[0].replaceAll("[^0-9.]","");
            String fat = temp2[1].replaceAll("[^0-9.]","");
            String carbs = temp2[2].replaceAll("[^0-9.]","");
            String protein = temp2[3].replaceAll("[^0-9.]","");

            alimento.setServing_description(temp[0]);
            alimento.setCalories(Double.parseDouble(calories));
            alimento.setFat(Double.parseDouble(fat));
            alimento.setCarbohydrate(Double.parseDouble(carbs));
            alimento.setProtein(Double.parseDouble(protein));
        }
        return alimento;
    }
}
