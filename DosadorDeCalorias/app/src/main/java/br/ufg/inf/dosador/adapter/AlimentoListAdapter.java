package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.api.Json;
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
    public TextView txtCalorias;
    public TextView txtPorcao;

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

        String foodName = itemAlimento.getFood_name();
        String description = itemAlimento.getFood_description();


        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(foodName.length() > 20) {
                foodName = foodName.substring(0,20) + "...";
            }
            txtPorcao = (TextView) view.findViewById(R.id.list_item_porcao);
            txtCalorias = (TextView) view.findViewById(R.id.list_item_calorias);
            String[] temp = Util.obterDadosFromDescription(description);
            txtPorcao.setText("Porção: " + temp[0]);
            txtCalorias.setText("Cal.: " + temp[1] + Json.UNIDADE_QUILO_CALORIAS);
        } else {
            txtFoodDescription = (TextView) view.findViewById(R.id.list_item_food_description);
            txtFoodDescription.setText(description);
        }
        txtFoodName.setText(foodName);
        return view;
    }

    public void setListaAlimentos(ArrayList<Alimento> itemList) {
        this.listaAlimentos = itemList;
        notifyDataSetChanged();
    }


}
