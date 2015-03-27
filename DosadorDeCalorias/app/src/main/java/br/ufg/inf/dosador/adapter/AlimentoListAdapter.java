package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 27/03/2015.
 */
public class AlimentoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Alimento> listaAlimentos = new ArrayList<Alimento>();

    public AlimentoListAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    public AlimentoListAdapter(Context context, ArrayList<Alimento> lista){
        this.listaAlimentos = lista;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Obtem a quantidade de alimentos da lista.
     * @return
     */
    @Override
    public int getCount() {
        return listaAlimentos.size();
    }

    /**
     * Obtem o alimento de acordo com a posição dele na listview.
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return listaAlimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaAlimentos.get(position).getFood_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alimento itemAlimento = listaAlimentos.get(position);

        //convertView = mInflater.inflate(R.layout.list_item_alimento, null);
        View view = mInflater.inflate(R.layout.list_item_alimento, parent, false);

        //((TextView) convertView.findViewById(R.id.tvItemSublocal)).setText(itemSublocal.getDescricao());
        ((TextView) view.findViewById(R.id.list_item_food_name)).setText(itemAlimento.getFood_name());

        ((TextView) view.findViewById(R.id.list_item_food_description)).setText(itemAlimento.getFood_description());


        return view;
    }

    public void setListaAlimentos(ArrayList<Alimento> itemList) {
        this.listaAlimentos = itemList;
    }


}
