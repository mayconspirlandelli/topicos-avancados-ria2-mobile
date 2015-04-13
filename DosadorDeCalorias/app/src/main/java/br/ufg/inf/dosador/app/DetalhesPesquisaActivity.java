package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.entidades.Alimento;

public class DetalhesPesquisaActivity extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pesquisa);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_pesquisa, menu);
        return true;
    }
}
