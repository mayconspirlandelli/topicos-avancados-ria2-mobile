package br.ufg.inf.dosador.app;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.CursorAdapter;
import android.widget.ListView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.adapter.RelatorioCursorAdapter;
import br.ufg.inf.dosador.dao.ConsumoDAO;

//TODO: somente para teste, Maycom irá implementar essa tela.
public class RelatorioActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;
    private ConsumoDAO consumoDAO;
    private ListView listView;

    private String dataAtual; //Data atual do sistema, obtida da classe Utils.
    private String dataInicial; //Data obtida da tela.
    private String dataFinal; //Data obtida da tela.
    private String dataMes; //Mês obtido da tela.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        buscarPorData(Util.obterDataAtualToString());

        consumoDAO = new ConsumoDAO(this);

        listView = (ListView) findViewById(R.id.listaRelatorio);
        listView.setFastScrollEnabled(true); //Habilita o scroll.
        //Cria o Adapter.
        mAdapter = new RelatorioCursorAdapter(this, null);
        //Define o Adapater.
        listView.setAdapter(mAdapter);
         getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_relatorio, menu);
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

    private void buscarPorData(String data){
        dataAtual = TextUtils.isEmpty(data) ? null : data;
    }
    private void buscarPorPeriodo(){

    }

    private void buscarPorMes(){

    }

    private void aplicarFiltro(){}


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return consumoDAO.buscarPorData(this, this.dataAtual);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
