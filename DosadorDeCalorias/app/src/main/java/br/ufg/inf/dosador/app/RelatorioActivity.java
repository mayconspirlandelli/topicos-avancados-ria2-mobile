package br.ufg.inf.dosador.app;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.adapter.RelatorioCursorAdapter;
import br.ufg.inf.dosador.dao.ConsumoDAO;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.pickers.DatePickerFragment;

/**
 * Por padrão, a tela carrega os dados por mes.
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

    public static final String EXTRA_MESSAGE = "extra_message";

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
        editDataInicial.setOnClickListener(editDataInicialOnClickListener);
        editDataFinal.setOnClickListener(editDataFinalOnClickListener);

        consumoDAO = new ConsumoDAO(this);

        listView = (ListView) findViewById(R.id.listaRelatorio);
        listView.setFastScrollEnabled(true); //Habilita o scroll.
        //Cria o Adapter.
        mAdapter = new RelatorioCursorAdapter(this, null);
        //Define o Adapater.
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(clickListaItemAlimento);

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

    final private View.OnClickListener editDataInicialOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editDataInicial.setFocusable(true);
            showDatePickerDialog();
        }
    };

    final private View.OnClickListener editDataFinalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editDataFinal.setFocusable(true);
            showDatePickerDialog();
        }
    };

    private void exibirFiltro() {
        if (exibeFiltro) {
            rlMes.setVisibility(View.GONE);
            rlFiltro.setVisibility(View.VISIBLE);
            exibeFiltro = false;
        } else {
            rlMes.setVisibility(View.VISIBLE);
            rlFiltro.setVisibility(View.GONE);
            exibeFiltro = true;
        }

        if (rlMes.getVisibility() == View.VISIBLE) {
            buscarPorMes();
        }
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void removerFiltro() {
        editDataInicial.setText("");
        editDataInicial.setHint("");
        editDataFinal.setText("");
        editDataFinal.setHint("");
        buscarPorMes();
    }

    private void buscarPorMes() {
        FILTRO = FILTRO_MES;
        this.dataMes = Util.obterMesAtual();
        this.dataMesStr = Util.convertDataFromCalendarToString(this.dataMes, "MM");
        txtMes.setText(Util.convertDataFromCalendarToString(this.dataMes, "MMMM").toUpperCase());
        carregarDados();
    }

    private void definirDataHoje() {
        FILTRO = FILTRO_PERIODO;

        //Copia a data inicial para a data final
        this.dataInicial = Util.obterDataAtualToString();
        this.dataFinal = this.dataInicial;

        editDataInicial.setHint(this.dataInicial);
        editDataFinal.setHint(this.dataInicial);

        editDataInicial.setText(Util.convertDataFormatToFormatInString(this.dataInicial, "yyyy-MM-dd", "dd/MM/yyyy"));
        editDataFinal.setText(Util.convertDataFormatToFormatInString(this.dataFinal, "yyyy-MM-dd", "dd/MM/yyyy"));

        carregarDados();
    }

    private void aplicarFiltro() {
        FILTRO = FILTRO_PERIODO;

        //A editDataInicial.getText().toString() é formatado em dd/MM/yyyy.
        //A editDataInicial.getHint().toString() é formatado em yyyy-MM-dd e é usado para ser salvo no banco de dados.
        if(editDataInicial.getHint() != null && editDataFinal.getHint() != null) {
            this.dataInicial = editDataInicial.getHint().toString();
            this.dataFinal = editDataFinal.getHint().toString();

            if (Util.compararDatas(this.dataInicial, this.dataFinal, "yyyy-MM-dd")){
                carregarDados();
            } else {
                Toast.makeText(RelatorioActivity.this, "A data inicial deve ser menor ou igual a data final!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(RelatorioActivity.this, "Informe o periodo!", Toast.LENGTH_LONG).show();
        }
    }

    private void decrementaOUincrementaMes(boolean decrementa) {
        if (decrementa) {
            this.dataMes = Util.getMesAnteriorPosterior(this.dataMes, true);
        } else {
            this.dataMes = Util.getMesAnteriorPosterior(this.dataMes, false);
        }
        this.dataMesStr = Util.convertDataFromCalendarToString(this.dataMes, "MM");
        txtMes.setText(Util.convertDataFromCalendarToString(this.dataMes, "MMMM").toUpperCase());
        carregarDados();
    }


    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            String data = cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA));
            Intent intent = new Intent(RelatorioActivity.this, ConsumoDiarioActivity.class);
            intent.putExtra(EXTRA_MESSAGE, data);
            startActivity(intent);
        }
    };


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
