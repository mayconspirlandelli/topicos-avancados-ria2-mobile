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

    private static final String LOG_TAG = ConsumoDiarioActivity.class.getSimpleName();

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private ConsumoCursorTreeAdapter mAdapter;
    private ConsumoDAO consumoDAO;
    private ExpandableListView listViewGrupos;
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

        textCalendario = (TextView) findViewById(R.id.txt_dia);
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
        mAdapter = new ConsumoCursorTreeAdapter(this, null);
        //Define os eventos.
        mAdapter.setCustomButtonListner(ConsumoDiarioActivity.this);
        mAdapter.setCustomOnItemListener(ConsumoDiarioActivity.this);
        mAdapter.setCustomOnItemChildListener(ConsumoDiarioActivity.this);
        mAdapter.setCustomOnLongListener(ConsumoDiarioActivity.this);

        //Define o Adapater.
        listViewGrupos.setAdapter(mAdapter);

        Intent intent = getIntent();
        String dataRecibida = intent.getStringExtra(RelatorioActivity.EXTRA_MESSAGE);
        if (dataRecibida != null && !dataRecibida.isEmpty()) {
            Log.d(LOG_TAG, "data recebida: " + dataRecibida);
            this.dataAtual = Util.convertDataFromStringToCalendar(dataRecibida, "yyyy-MM-dd");
        } else {
            this.dataAtual = Util.obterDataAtual();
        }
        this.dataAtualStr = Util.convertDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");
        textCalendario.setText(Util.convertDataFromCalendarToString(this.dataAtual, "dd/MM/yyyy"));

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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            this.dataAtualStr = Util.convertDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");

        } else {
            this.dataAtual = Util.getDiaAnteriorPosterior(this.dataAtual, false);
            this.dataAtualStr = Util.convertDataFromCalendarToString(this.dataAtual, "yyyy-MM-dd");
        }
        textCalendario.setText(Util.convertDataFromCalendarToString(this.dataAtual, "dd/MM/yyyy"));
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
        HashMap<Integer, Integer> groupMap = mAdapter.getGroupMap();
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
                HashMap<Integer, Integer> groupMap = mAdapter.getGroupMap();
                try {
                    int groupPos = groupMap.get(loader.getId());
                    mAdapter.setChildrenCursor(groupPos, cursor);
                } catch (NullPointerException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        } else {
            mAdapter.setGroupCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id != -1) {
            try {
                mAdapter.setChildrenCursor(id, null);
            } catch (NullPointerException e) {
                Log.e("Error", e.getMessage());
            }
        } else {
            mAdapter.setGroupCursor(null);
        }
    }

    @Override
    public void onButtonClickListner(int position, String tipoRefeicao) {

        //TODO: trocar "teste" por um R.id.string.
        SharedPreferences sharedPref = ConsumoDiarioActivity.this.getSharedPreferences("teste", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // Limpar as configurações anteriores.
        editor.clear();
        editor.commit();
        // Salva as novas configurações.
        editor.putString("teste", tipoRefeicao);
        editor.commit();

        Intent intencao = new Intent(this, PesquisaActivity.class);
        startActivity(intencao);
        Log.d(LOG_TAG, "Posição do grupo no evento onButtonClickListner:" + position);
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
        Log.d(LOG_TAG, "Posição do grupo no evento onItemClickListener:" + position);
    }

    @Override
    public void onItemChildClickListener(int position, Cursor cursor) {
        Consumo consumo = ConsumoDAO.obterConsumoFromCursor(cursor);
        Bundle parametros = new Bundle();
        parametros.putSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO, consumo);

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
