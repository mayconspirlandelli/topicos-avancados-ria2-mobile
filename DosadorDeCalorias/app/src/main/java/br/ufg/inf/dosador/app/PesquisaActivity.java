package br.ufg.inf.dosador.app;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.entidades.Alimento;

public class PesquisaActivity extends ActionBarActivity implements PesquisaFragment.Callback {

    private final String LOG_TAG = PesquisaActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String FRAGMENT_TAG = "TAG";

    private boolean mTwoPane;
    //
//    // private CheckBox checbox;
//    private EditText editPesquisa;
//    private ImageButton btnPesquisar;
//    private ListView listView;
//    public AlimentoListAdapter adapterListAlimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        handleIntent(getIntent());

        if (findViewById(R.id.detalhes_pesquisa) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detalhes_pesquisa, new DetalhesPesquisaFragment(), DETAILFRAGMENT_TAG)
                        .commit();
//
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.pesquisa_fragment, new PesquisaFragment(), FRAGMENT_TAG)
//                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        //PesquisaFragment pesquisaFragment = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_pesquisa);
        PesquisaFragment pesquisaFragment = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.pesquisa_fragment_container);

        //testar
        //PesquisaFragment pesquisaFragment = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_pesquisa);
        //PesquisaFragment pesquisaFragment = ((PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_pesquisa));


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        inicializaObjetosDeTela();
//    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        PesquisaFragment pf = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
//        if (pf != null) {
//            ///ff.onLocationChanged();
//
//        }
//        DetalhesPesquisaFragment dpf = (DetalhesPesquisaFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
//        if (null != dpf) {
//            //df.onLocationChanged(location);
//        }
//
//    }

    @Override
    public void onItemSelected(Alimento alimento) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle parametros = new Bundle();
            parametros.putSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO, alimento);

            DetalhesPesquisaFragment fragment = new DetalhesPesquisaFragment();
            fragment.setArguments(parametros);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detalhes_pesquisa, fragment, DetalhesPesquisaFragment.TAG_DETALHE)
                    .commit();
        } else {

            //TODO: teste das duas alinhas abaixo:
            Intent intent = new Intent(this, DetalhesPesquisaActivity.class);
            intent.putExtra(DetalhesPesquisaFragment.EXTRA_ALIMENTO, alimento);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //abrirTelaParetActivity();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true); //Botão UP para retornar a activity anterior.
        actionBar.setDisplayShowTitleEnabled(true); //Habilitar a exibição do titulo na action bar.
        actionBar.setTitle(PesquisaActivity.class.getSimpleName());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


            PesquisaFragment pf;
            if (mTwoPane) {
                //table.
                pf = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.pesquisa_fragment_container);
            } else {
                //celular.
                pf = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.activity_pesquisa);
            }


            //anterior - funciona para celular
            //PesquisaFragment pf = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_pesquisa);
            //em teste - funciona para table
            //PesquisaFragment pf = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.pesquisa_fragment);
            pf.pesquisarAlimento(query);
        }
    }


//
//    private void inicializaObjetosDeTela() {
//
//        //checbox = (CheckBox) findViewById(R.id.checkbox);
//        editPesquisa = (EditText) findViewById(R.id.edit_pesquisa);
//        btnPesquisar = (ImageButton) findViewById(R.id.btn_pesquisar);
//        btnPesquisar.setOnClickListener(btnPesquisarOnClickListener);
//
//        //Define ListView
//        listView = (ListView) findViewById(R.id.listaAlimentos);
//        listView.setFastScrollEnabled(true); //Habilita o scroll.
//        listView.setOnItemClickListener(clickListaItemAlimento);
//
//        //Cria o Adapter.
//        adapterListAlimento = new AlimentoListAdapter(PesquisaActivity.this);
//        //Define o Adapater.
//        listView.setAdapter(adapterListAlimento);
//    }
//
//    final private View.OnClickListener btnPesquisarOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            pesquisarAlimento(editPesquisa.getText().toString());
//        }
//    };
//
//    private void pesquisarAlimento(String expressao) {
//        ListaAlimentoTask task = new ListaAlimentoTask(this, adapterListAlimento);
//        task.execute(FatSecret.METHOD_FOODS_SEARCH, expressao);
//    }
//
//    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Alimento alimento = (Alimento) adapterListAlimento.getItem(position);
//            abrirTelaDetalhesPesquisaActivity(alimento);
//        }
//    };
//
//
//    private void abrirTelaDetalhesPesquisaActivity(Alimento ali) {
//        Alimento alimento = obterDadosFromDescription(ali);
//        Intent intent = new Intent(PesquisaActivity.this, DetalhesPesquisaActivity.class);
//        intent.putExtra(Json.FOOD_ID, alimento.getFood_id());
//        intent.putExtra(Json.FOOD_NAME, alimento.getFood_name());
//        intent.putExtra(Json.SERVING_DESCRIPTION, alimento.getServing_description());
//        intent.putExtra(Json.CALORIES, alimento.getCalories());
//        intent.putExtra(Json.FAT, alimento.getFat());
//        intent.putExtra(Json.CARBOHYDRATE, alimento.getCarbohydrate());
//        intent.putExtra(Json.PROTEIN, alimento.getProtein());
//        startActivity(intent);
//    }
//
//    public Alimento obterDadosFromDescription(Alimento alimento) {
//        String description = alimento.getFood_description();
//        //Exemplo "Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g"
//        if (!description.isEmpty()) {
//            String[] temp = description.split("-");
//            String[] temp2 = temp[1].split("\\|");
//
//            String calories = temp2[0].replaceAll("[^0-9.]", "");
//            String fat = temp2[1].replaceAll("[^0-9.]", "");
//            String carbs = temp2[2].replaceAll("[^0-9.]", "");
//            String protein = temp2[3].replaceAll("[^0-9.]", "");
//
//            alimento.setServing_description(temp[0]);
//            alimento.setCalories(Double.parseDouble(calories));
//            alimento.setFat(Double.parseDouble(fat));
//            alimento.setCarbohydrate(Double.parseDouble(carbs));
//            alimento.setProtein(Double.parseDouble(protein));
//        }
//        return alimento;
//    }
//
//    private void abrirTelaParetActivity() {
//
//    }


}
