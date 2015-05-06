package br.ufg.inf.dosador.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.adapter.ConsumoCursorTreeAdapter;
import br.ufg.inf.dosador.adapter.ConsumoCursorTreeAdapter.customButtonListener;
import br.ufg.inf.dosador.adapter.ConsumoCursorTreeAdapter.customOnItemChildListener;
import br.ufg.inf.dosador.adapter.ConsumoCursorTreeAdapter.customOnItemListener;
import br.ufg.inf.dosador.adapter.ConsumoCursorTreeAdapter.customOnLongListener;
import br.ufg.inf.dosador.dao.ConsumoDAO;
import br.ufg.inf.dosador.entidades.Consumo;
import br.ufg.inf.dosador.entidades.TipoRefeicao;

public class ConsumoDiarioActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        customButtonListener, customOnItemListener, customOnItemChildListener, customOnLongListener {

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private ConsumoCursorTreeAdapter mAdapterAlmocoTeste;

    private ConsumoDAO consumoDAO;

    private ExpandableListView listViewGrupos;

    private ScrollView scrollView;

    private String dataAtualStr; //Data atual do sistema, obtida da classe Utils.
    private Calendar dataAtual;

    private boolean isExpanded = true; //ExpandableListView está expandida.

    private ImageButton btnDecrementaData;
    private ImageButton btnIncrementaData;
    private TextView textCalendario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbacks = this;

        setContentView(R.layout.activity_consumo_diario);

        consumoDAO = new ConsumoDAO(this);

        textCalendario = (TextView) findViewById(R.id.txt_mes);
        listViewGrupos = (ExpandableListView) findViewById(R.id.expand_lista_almoco);
        btnDecrementaData = (ImageButton) findViewById(R.id.btn_decrementa_data);
        btnIncrementaData = (ImageButton) findViewById(R.id.btn_incrementa_data);
        btnDecrementaData.setOnClickListener(btnDecrementaDataOnClickListener);
        btnIncrementaData.setOnClickListener(btnIncrementaDataOnClickListener);

        //http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
        //http://www.truiton.com/2014/05/android-simplecursortreeadapter-cursorloader-expandablelistview/
        //http://stackoverflow.com/questions/16382368/scrollview-distrubing-listview-inside-of-linearlayout
        //http://www.londatiga.net/it/programming/android/make-android-listview-gridview-expandable-inside-scrollview/

        //Implementar a ListView como ExpandableListView.
        //http://stackoverflow.com/questions/12972398/android-better-alternative-to-expandable-listview
        //http://dj-android.blogspot.com.br/2013/01/android-better-alternative-of.html
        //alternative expandable list android


        //Cria o Adapter.
        mAdapterAlmocoTeste = new ConsumoCursorTreeAdapter(this, null);
        //Define os eventos.
        mAdapterAlmocoTeste.setCustomButtonListner(ConsumoDiarioActivity.this);
        mAdapterAlmocoTeste.setCustomOnItemListener(ConsumoDiarioActivity.this);
        mAdapterAlmocoTeste.setCustomOnItemChildListener(ConsumoDiarioActivity.this);
        mAdapterAlmocoTeste.setCustomOnLongListener(ConsumoDiarioActivity.this);

        //Define o Adapater.
        listViewGrupos.setAdapter(mAdapterAlmocoTeste);

        this.dataAtual = Util.obterDataAtual();
        this.dataAtualStr = Util.conveteDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");

        textCalendario.setText(Util.conveteDataFromCalendarToString(this.dataAtual, "dd/MM/yyyy"));

        carregarDados();
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

    private void carregarDados() {
        //Carrega os dados para tela.
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(-1);
        if (loader != null && !loader.isReset()) {
            getSupportLoaderManager().restartLoader(-1, null, this);
        } else {
            getSupportLoaderManager().initLoader(-1, null, this);
        }
    }

    private void decrementaOUincrementaData(boolean decrementa) {
        if (decrementa) {
            this.dataAtual = Util.getDiaAnteriorPosterior(this.dataAtual, true);
            this.dataAtualStr = Util.conveteDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");

        } else {
            this.dataAtual = Util.getDiaAnteriorPosterior(this.dataAtual, false);
            this.dataAtualStr = Util.conveteDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");
        }
        textCalendario.setText(Util.conveteDataFromCalendarToString(this.dataAtual, "dd/MM/yyyy"));
        carregarDados();
    }


    final private View.OnClickListener btnDecrementaDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decrementaOUincrementaData(true);
        }
    };

    final private View.OnClickListener btnIncrementaDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decrementaOUincrementaData(false);
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        HashMap<Integer, Integer> groupMap = mAdapterAlmocoTeste.getGroupMap();
        if (id != -1) {
            int groupPos = groupMap.get(id);

            if (groupPos == 0) { //cafe
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtualStr, TipoRefeicao.CAFE_DA_MANHA.name());
            } else if (groupPos == 1) { //almoco
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtualStr, TipoRefeicao.ALMOCO.name());

            } else if (groupPos == 2) { // janta
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtualStr, TipoRefeicao.LANCHE.name());

            } else if (groupPos == 3) { //lanche
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtualStr, TipoRefeicao.JANTAR.name());
            }
        }
        return consumoDAO.buscarTodosTipoRefeicao(this);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        if (id != -1) {
            if (!cursor.isClosed()) {
                HashMap<Integer, Integer> groupMap = mAdapterAlmocoTeste.getGroupMap();
                try {
                    int groupPos = groupMap.get(loader.getId());
                    mAdapterAlmocoTeste.setChildrenCursor(groupPos, cursor);
                } catch (NullPointerException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        } else {
            mAdapterAlmocoTeste.setGroupCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id != -1) {
            try {
                mAdapterAlmocoTeste.setChildrenCursor(id, null);
            } catch (NullPointerException e) {
                Log.e("Error", e.getMessage());
            }
        } else {
            mAdapterAlmocoTeste.setGroupCursor(null);
        }
    }

    @Override
    public void onButtonClickListner(int position, String tipoRefeicao) {

        SharedPreferences sharedPref = ConsumoDiarioActivity.this.getSharedPreferences("teste", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // Limpar as configurações anteriores.
        editor.clear();
        editor.commit();
        // Salva as novas configurações.
        editor.putString("teste", tipoRefeicao);
        editor.commit();


        Intent intencao = new Intent(this, PesquisaActivity.class);
        //intencao.putExtra("tipo", tipoRefeicao);
        startActivity(intencao);

        //Expandir o grupo expand;
        isExpanded = true;
        onItemClickListener(position);

    }

    @Override
    public void onItemClickListener(int position) {
        if (isExpanded) {
            isExpanded = false;
            this.listViewGrupos.expandGroup(position);
        } else {
            isExpanded = true;
            this.listViewGrupos.collapseGroup(position);
        }
    }

    @Override
    public void onItemChildClickListener(int position, Cursor cursor) {
        Consumo consumo = ConsumoDAO.obterConsumoFromCursor(cursor);
        Bundle parametros = new Bundle();
        parametros.putSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO, consumo);

        //TODO: remover a tela DetalhesPesquisaActivity do noHistory.
        Intent intent = new Intent(this, DetalhesPesquisaActivity.class);
        intent.putExtra(DetalhesPesquisaFragment.EXTRA_ALIMENTO, consumo);
        startActivity(intent);
    }

    @Override
    public void onLongClickListener(int position, Cursor cursor) {
        Consumo consumo = ConsumoDAO.obterConsumoFromCursor(cursor);
        exibirAlertaParaConfirmarExclucao(consumo);
    }

    private void exibirAlertaParaConfirmarExclucao(final Consumo consumo) {
        Dialog dialog = new AlertDialog.Builder(ConsumoDiarioActivity.this)
                .setTitle(getString(R.string.dialog_titulo))
                .setMessage(getString(R.string.dialog_mensagem_excluir_dieta))
                .setPositiveButton(getString(R.string.dialog_opcao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //excluir os dados.
                        consumoDAO.excluir(consumo);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_opcao_nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancela operação.
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
