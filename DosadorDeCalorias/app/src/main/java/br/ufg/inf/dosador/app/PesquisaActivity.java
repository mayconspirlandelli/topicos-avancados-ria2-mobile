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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        handleIntent(getIntent());

        if (findViewById(R.id.detalhes_pesquisa) != null) {
            mTwoPane = true;
//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.detalhes_pesquisa, new DetalhesPesquisaFragment(), DETAILFRAGMENT_TAG)
//                        .commit();
//            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        //PesquisaFragment pesquisaFragment = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_pesquisa);
        PesquisaFragment pesquisaFragment = (PesquisaFragment) getSupportFragmentManager().findFragmentById(R.id.pesquisa_fragment_container);
    }

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
            pf.pesquisarAlimento(query);
        }
    }
}
