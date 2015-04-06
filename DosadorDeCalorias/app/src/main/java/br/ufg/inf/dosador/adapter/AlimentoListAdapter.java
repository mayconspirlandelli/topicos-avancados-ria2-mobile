package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 27/03/2015.
 */
public class AlimentoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Alimento> listaAlimentos = new ArrayList<Alimento>();

    public TextView txtFoodName;
    public TextView txtFoodDescription;
    public CheckBox chckFoodSelectioin;

    public AlimentoListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    /**
     * Obtem a quantidade de alimentos da lista.
     *
     * @return
     */
    @Override
    public int getCount() {
        return listaAlimentos.size();
    }

    /**
     * Obtem o alimento de acordo com a posição dele na listview.
     *
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

        View view = mInflater.inflate(R.layout.list_item_alimento, parent, false);

        txtFoodName = (TextView) view.findViewById(R.id.list_item_food_name);
        txtFoodDescription = (TextView) view.findViewById(R.id.list_item_food_description);


        txtFoodName.setText(itemAlimento.getFood_name());
        txtFoodDescription.setText(itemAlimento.getFood_description());

        //TODO: exibir apenas nome, porção e calorias (kcal).

        return view;
    }

    public void setListaAlimentos(ArrayList<Alimento> itemList) {
        this.listaAlimentos = itemList;
        notifyDataSetChanged();
    }


}
