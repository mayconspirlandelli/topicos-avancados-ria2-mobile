package br.ufg.inf.dosador.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.adapter.RelatorioCursorAdapter;
import br.ufg.inf.dosador.dao.ConsumoDAO;
import br.ufg.inf.dosador.pickers.DatePickerFragment;

/**
 * TODO: somente para teste, Maycom irá implementar essa tela.
 * Por padrão, a tela carrega os dados por mes.
 * Falta colocar o Date Picker http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

public class RelatorioActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;
    private ConsumoDAO consumoDAO;
    private ListView listView;
    private EditText editDataInicial;
    private EditText editDataFinal;
    private TextView txtMes;
    private ImageButton btnAplicarFiltro;
    private ImageButton btnRemoverFiltro;
    private Button btnHoje;
    private ImageButton btnDecrementaMes;
    private ImageButton btnIncrementaMes;
    private RelativeLayout rlMes;
    private RelativeLayout rlFiltro;

    private static String FILTRO = "";
    private static final String FILTRO_MES = "mes";
    private static final String FILTRO_PERIODO = "periodo";
    private static final String FILTRO_DATA = "data";

    private String dataInicial; //Data obtida da tela.
    private String dataFinal; //Data obtida da tela.
    private String dataMesStr; //Mês obtido da tela.
    private Calendar dataMes;
    private boolean exibeFiltro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        txtMes = (TextView) findViewById(R.id.txt_mes);
        editDataInicial = (EditText) findViewById(R.id.editDataInicial);
        editDataFinal = (EditText) findViewById(R.id.editDataFinal);
        btnAplicarFiltro = (ImageButton) findViewById(R.id.btn_filtro);
        btnRemoverFiltro = (ImageButton) findViewById(R.id.btn_remover_filtro);
        btnHoje = (Button) findViewById(R.id.btn_hoje);
        btnDecrementaMes = (ImageButton) findViewById(R.id.btn_decrementa_mes);
        btnIncrementaMes = (ImageButton) findViewById(R.id.btn_incrementa_mes);
        rlMes = (RelativeLayout) findViewById(R.id.rl_mes);
        rlFiltro = (RelativeLayout) findViewById(R.id.rl_filtro);
        rlMes.setVisibility(View.VISIBLE);// exibe o layout que contem o filtro por mês.

        btnAplicarFiltro.setOnClickListener(btnAplicarFiltroOnClickListener);
        btnRemoverFiltro.setOnClickListener(btnRemoverFiltroOnClickListener);
        btnHoje.setOnClickListener(btnHojeOnClickListener);
        btnDecrementaMes.setOnClickListener(btnDecrementaMesOnClickListener);
        btnIncrementaMes.setOnClickListener(btnIncrementaOnClickListener);

        consumoDAO = new ConsumoDAO(this);

        listView = (ListView) findViewById(R.id.listaRelatorio);
        listView.setFastScrollEnabled(true); //Habilita o scroll.
        //Cria o Adapter.
        mAdapter = new RelatorioCursorAdapter(this, null);
        //Define o Adapater.
        listView.setAdapter(mAdapter);

        ///getSupportLoaderManager().initLoader(0, null, this);
        buscarPorMes();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); //Fecha Activity
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_filter:
                exibirFiltro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    final private View.OnClickListener btnAplicarFiltroOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            aplicarFiltro();
        }
    };

    final private View.OnClickListener btnRemoverFiltroOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removerFiltro();
        }
    };

    final private View.OnClickListener btnHojeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            definirDataHoje();
        }
    };

    final private View.OnClickListener btnDecrementaMesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decrementaOUincrementaMes(true);
        }
    };

    final private View.OnClickListener btnIncrementaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decrementaOUincrementaMes(false);
        }
    };

    private void exibirFiltro(){
        if(exibeFiltro) {
            rlMes.setVisibility(View.GONE);
            rlFiltro.setVisibility(View.VISIBLE);
            exibeFiltro = false;
        } else {
            rlMes.setVisibility(View.VISIBLE);
            rlFiltro.setVisibility(View.GONE);
            exibeFiltro = true;
        }

        //TODO: somente para teste
        //
        // showDatePickerDialog();
    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void removerFiltro() {
        editDataInicial.setText("");
        editDataFinal.setText("");
        buscarPorMes();
    }

    private void buscarPorMes() {
        FILTRO = FILTRO_MES;
        this.dataMes = Util.obterMesAtual();
        this.dataMesStr = Util.conveteDataFromCalendarToString(this.dataMes, "MM");
        txtMes.setText(Util.conveteDataFromCalendarToString(this.dataMes, "MMMM").toUpperCase());
        carregarDados();
    }

    private void definirDataHoje() {
        FILTRO = FILTRO_DATA;
        //Copia a data inicial para a data final
        this.dataInicial = Util.obterDataAtualToString();
        this.dataFinal = this.dataInicial;
        editDataInicial.setText(this.dataInicial);
        editDataFinal.setText(this.dataFinal);
        carregarDados();
    }

    private void aplicarFiltro() {
        FILTRO = FILTRO_PERIODO;

        this.dataInicial = editDataInicial.getText().toString();
        this.dataFinal = editDataFinal.getText().toString();

        if(this.dataInicial != null && !this.dataInicial.isEmpty() && this.dataFinal != null && !this.dataFinal.isEmpty()) {
            carregarDados();
        } else {
            Util.exibeAlerta(this, "Informe o período!");
        }
    }

    private void decrementaOUincrementaMes(boolean decrementa) {
        if (decrementa) {
            this.dataMes = Util.getMesAnteriorPosterior(this.dataMes, true);
        } else {
            this.dataMes = Util.getMesAnteriorPosterior(this.dataMes, false);
        }
        this.dataMesStr = Util.conveteDataFromCalendarToString(this.dataMes, "MM");
        txtMes.setText(Util.conveteDataFromCalendarToString(this.dataMes, "MMMM").toUpperCase());
        carregarDados();
    }


    /**
     * Método responsável por carregar os dados do Cursor para tela.
     */
    private void carregarDados() {
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(0);
        if (loader != null && !loader.isReset()) {
            getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (FILTRO) {
            case FILTRO_MES:
                return consumoDAO.buscarPorMes(this, this.dataMesStr);
            case FILTRO_PERIODO:
                return consumoDAO.buscarPorPeriodo(this, this.dataInicial, this.dataFinal);
            case FILTRO_DATA:
                return consumoDAO.buscarPorData(this, this.dataInicial);
            default:
                return consumoDAO.buscarPorMes(this, this.dataMesStr);
        }
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
