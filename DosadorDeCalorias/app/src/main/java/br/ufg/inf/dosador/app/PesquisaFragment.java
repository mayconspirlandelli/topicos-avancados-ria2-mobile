package br.ufg.inf.dosador.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.adapter.AlimentoListAdapter;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.task.ListaAlimentoTask;

/**
 * Created by Maycon on 09/04/2015.
 */
public class PesquisaFragment extends Fragment implements IDosadorUpdater {

    private ListView listView;
    public AlimentoListAdapter adapterListAlimento;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Alimento alimento);
    }

    //Por default, toda classe Fragment deve ter um construtor sem parametros.
    public PesquisaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pesquisa_activity, container, false);

        //Usar rootView.findViewById no lugar de this.getActivity.findViewById, como reportado em
        // http://stackoverflow.com/questions/13564945/listview-declared-in-layout-is-null-in-oncreateview
        //Define ListView
        listView = (ListView) rootView.findViewById(R.id.listaAlimentos);

        listView.setFastScrollEnabled(true); //Habilita o scroll.
        //Cria o Adapter.
        adapterListAlimento = new AlimentoListAdapter(getActivity());
        //Define o Adapater.
        listView.setAdapter(adapterListAlimento);

        listView.setOnItemClickListener(clickListaItemAlimento);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void hideProgress() {
        listView.setVisibility(View.VISIBLE);
        ProgressBar progress = (ProgressBar) getActivity().findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        listView.setVisibility(View.INVISIBLE);
        ProgressBar progress = (ProgressBar) getActivity().findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void exibeMensagem(String mensagem) {
        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
    }

    public void pesquisarAlimento(String expressao) {
        ListaAlimentoTask task = new ListaAlimentoTask(getActivity(), adapterListAlimento, this);
        task.execute(FatSecret.METHOD_FOODS_SEARCH, expressao);
    }

    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Alimento alimento = (Alimento) parent.getItemAtPosition(position); //Seguindo exemplo do Sunshine https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java

            if (alimento != null) {
                ((Callback) getActivity()).onItemSelected(alimento);
            }
            mPosition = position;
        }
    };
}
