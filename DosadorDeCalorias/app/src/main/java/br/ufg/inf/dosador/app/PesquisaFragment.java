package br.ufg.inf.dosador.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.adapter.AlimentoListAdapter;
import br.ufg.inf.dosador.api.FatSecret;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.task.ListaAlimentoTask;

/**
 * Created by Maycon on 09/04/2015.
 */
public class PesquisaFragment extends Fragment implements IDosadorUpdater {

    private ListView listView;
    public AlimentoListAdapter adapterListAlimento;



    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected();
    }


    public PesquisaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pesquisa_activity, container, false);

        //Usar rootView.findViewById no lugar de this.getActivity.findViewById, como reportado em
        // http://stackoverflow.com/questions/13564945/listview-declared-in-layout-is-null-in-oncreateview
        //Define ListView
        listView = (ListView) rootView.findViewById(R.id.listaAlimentos);

        listView.setFastScrollEnabled(true); //Habilita o scroll.
        listView.setOnItemClickListener(clickListaItemAlimento);
        //Cria o Adapter.
        adapterListAlimento = new AlimentoListAdapter(getActivity());
        //Define o Adapater.
        listView.setAdapter(adapterListAlimento);

        return rootView;
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

    public void pesquisarAlimento(String expressao) {
        ListaAlimentoTask task = new ListaAlimentoTask(getActivity(), adapterListAlimento, this);
        task.execute(FatSecret.METHOD_FOODS_SEARCH, expressao);
    }


    final private ListView.OnItemClickListener clickListaItemAlimento = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Alimento alimento = (Alimento) adapterListAlimento.getItem(position);
            abrirTelaDetalhesPesquisaActivity(alimento);
        }
    };


    private void abrirTelaDetalhesPesquisaActivity(Alimento ali) {
        Alimento alimento = obterDadosFromDescription(ali);
        Intent intent = new Intent(getActivity(), DetalhesPesquisaActivity.class);
        intent.putExtra(Json.FOOD_ID, alimento.getFood_id());
        intent.putExtra(Json.FOOD_NAME, alimento.getFood_name());
        intent.putExtra(Json.SERVING_DESCRIPTION, alimento.getServing_description());
        intent.putExtra(Json.CALORIES, alimento.getCalories());
        intent.putExtra(Json.FAT, alimento.getFat());
        intent.putExtra(Json.CARBOHYDRATE, alimento.getCarbohydrate());
        intent.putExtra(Json.PROTEIN, alimento.getProtein());
        startActivity(intent);
    }

    public Alimento obterDadosFromDescription(Alimento alimento) {
        String description = alimento.getFood_description();
        //Exemplo "Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g"
        if (!description.isEmpty()) {
            String[] temp = description.split("-");
            String[] temp2 = temp[1].split("\\|");

            String calories = temp2[0].replaceAll("[^0-9.]", "");
            String fat = temp2[1].replaceAll("[^0-9.]", "");
            String carbs = temp2[2].replaceAll("[^0-9.]", "");
            String protein = temp2[3].replaceAll("[^0-9.]", "");

            alimento.setServing_description(temp[0]);
            alimento.setCalories(Double.parseDouble(calories));
            alimento.setFat(Double.parseDouble(fat));
            alimento.setCarbohydrate(Double.parseDouble(carbs));
            alimento.setProtein(Double.parseDouble(protein));
        }
        return alimento;
    }
}
