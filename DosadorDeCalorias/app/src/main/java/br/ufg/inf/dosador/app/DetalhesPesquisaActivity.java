package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.entidades.Alimento;

public class DetalhesPesquisaActivity extends ActionBarActivity {

//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayHomeAsUpEnabled(true); //Botão UP para retornar a activity anterior.
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pesquisa);

        //restoreActionBar();

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle parametros = new Bundle();
            Intent intent = getIntent();

            Alimento alimento = (Alimento) intent.getSerializableExtra(DetalhesPesquisaFragment.EXTRA_ALIMENTO);
            parametros.putSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO, alimento);

            DetalhesPesquisaFragment fragment = new DetalhesPesquisaFragment();
            fragment.setArguments(parametros);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detalhes_pesquisa, fragment)
                    .commit();
        }
    }
}
