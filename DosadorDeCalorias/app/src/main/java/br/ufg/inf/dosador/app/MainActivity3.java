package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import br.ufg.inf.dosador.R;

public class MainActivity3 extends ActionBarActivity {

    Button btnPesquisar;
    Button btnRelatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        btnPesquisar = (Button) findViewById(R.id.btn_pesquisar);
        btnPesquisar.setOnClickListener(btnPesquisarOnClickListener);

        btnRelatorio = (Button) findViewById(R.id.btn_relatorio);
        btnRelatorio.setOnClickListener(btnRelatorioOnClickListener);


    }

    final private View.OnClickListener btnPesquisarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            abrirTelaDePesquisa();
        }
    };

    final private View.OnClickListener btnRelatorioOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            abrirTelaDeRelatorio();
        }
    };

    private void abrirTelaDePesquisa() {
        Intent intent = new Intent(this, PesquisaActivity.class);
        startActivity(intent);
    }

    private void abrirTelaDeRelatorio() {
        Intent intent = new Intent(this, RelatorioActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity3, container, false);
            return rootView;
        }
    }
}
