package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.adapter.ConsumoCursorAdapter;
import br.ufg.inf.dosador.adapter.TruitonSimpleCursorTreeAdapter;
import br.ufg.inf.dosador.dao.ConsumoDAO;
import br.ufg.inf.dosador.entidades.TipoRefeicao;

public class ConsumoDiarioActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapterCafe;
    // private CursorAdapter mAdapterAlmoco;

    private ConsumoDAO consumoDAO;
    private ListView listViewCafeDaManha;
    private ListView listViewAlmoco;

    private ScrollView scrollView;

    private final int LOADER_CAFE_ID = 0;
    private final int LOADER_ALMOCO_ID = 1;

    private String dataAtual; //Data atual do sistema, obtida da classe Utils.


    //todo: SOEMNTE PARA TESTEG
    TruitonSimpleCursorTreeAdapter mAdapterAlmoco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_diario);

        this.setTitle("Consumo Diário");

        TextView calendario = (TextView) findViewById(R.id.textCalendario);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String data = df.format(new Date());
        calendario.setText(data);


        buscarPorData(Util.obterDataAtual());

        consumoDAO = new ConsumoDAO(this);


        scrollView = (ScrollView) findViewById(R.id.scrviewConsumo);

        listViewCafeDaManha = (ListView) findViewById(R.id.lista_cafe_da_manha);
        //listViewCafeDaManha.setFastScrollEnabled(true); //Habilita o scroll.

        //listViewAlmoco = (ListView) findViewById(R.id.lista_almoco);
        //listViewAlmoco.setFastScrollEnabled(true); //Habilita o scroll.

        //Cria o Adapter.
        mAdapterCafe = new ConsumoCursorAdapter(this, null);
        // mAdapterAlmoco = new ConsumoCursorAdapter(this, null);
        //Define o Adapater.
        listViewCafeDaManha.setAdapter(mAdapterCafe);
        //listViewAlmoco.setAdapter(mAdapterAlmoco);

        listViewCafeDaManha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }
        });


        //http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
        //http://www.truiton.com/2014/05/android-simplecursortreeadapter-cursorloader-expandablelistview/
        //http://stackoverflow.com/questions/16382368/scrollview-distrubing-listview-inside-of-linearlayout
        //http://www.londatiga.net/it/programming/android/make-android-listview-gridview-expandable-inside-scrollview/

        ExpandableListView expandableContactListView = (ExpandableListView) findViewById(R.id.lista_almoco);

//         mAdapterAlmoco = new TruitonSimpleCursorTreeAdapter(this,
//                 android.R.layout.simple_expandable_list_item_1,
//                 android.R.layout.simple_expandable_list_item_1,
//                 new String[] { ContactsContract.Contacts.DISPLAY_NAME },
//                 new int[] { android.R.id.text1 },
//                 new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
//                 new int[] { android.R.id.text1 });

        mAdapterAlmoco = new TruitonSimpleCursorTreeAdapter(this, null);

        expandableContactListView.setAdapter(mAdapterAlmoco);


        getSupportLoaderManager().initLoader(LOADER_CAFE_ID, null, this);
        getSupportLoaderManager().initLoader(LOADER_ALMOCO_ID, null, this);


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

    public void pesquisa(View v) {
        String tipo = new String();

        switch (v.getId()) {
            case R.id.imageButtonCafe:
                tipo = "Café da Manhã";
                break;
            case R.id.imageButtonAlmoco:
                tipo = "Almoço";
                break;
            case R.id.imageButtonJantar:
                tipo = "Jantar";
                break;
            case R.id.imageButtonLanche:
                tipo = "Lanche";
                break;
        }

        Intent intencao = new Intent(this, PesquisaActivity.class);
        intencao.putExtra("tipo", tipo);
        startActivity(intencao);
    }


    private void buscarPorData(String data) {
        dataAtual = TextUtils.isEmpty(data) ? null : data;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CAFE_ID:
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtual, TipoRefeicao.CAFE_DA_MANHA.name());
            case LOADER_ALMOCO_ID:
                //return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtual, TipoRefeicao.ALMOCO.name());
                return consumoDAO.buscarPorDataAndTipoRefeicao(this, this.dataAtual, TipoRefeicao.LANCHE.name());
            default:
                return consumoDAO.buscarPorData(this, this.dataAtual);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();

        switch (id) {
            case LOADER_CAFE_ID:
                mAdapterCafe.swapCursor(cursor);
                break;
            case LOADER_ALMOCO_ID: {
                //mAdapterAlmoco.swapCursor(cursor);


                //O GroupCursor é para setar o grupo, neste caso é cafe, almoço, lanche, jantar.
                mAdapterAlmoco.setGroupCursor(cursor);

//                if (id != -1) {
//                    if (!cursor.isClosed()) {
//                        HashMap<Integer, Integer> groupMap = mAdapterAlmoco.getGroupMap();
//
//                        try {
//                            int groupPos = groupMap.get(loader.getId());
//                            mAdapterAlmoco.setChildrenCursor(groupPos, cursor);
//                        } catch (NullPointerException e) {
//                            Log.e("Error", e.getMessage());
//                        }
//                    }
//                } else {
//                    mAdapterAlmoco.setGroupCursor(cursor);
//                }
//                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterCafe.swapCursor(null);
        // mAdapterAlmoco.swapCursor(null);

        //O GroupCursor é para setar o grupo, neste caso é cafe, almoço, lanche, jantar.
        // mAdapterAlmoco.setGroupCursor(null);

        mAdapterAlmoco.setChildrenCursor(loader.getId(), null);
    }

}
