package br.ufg.inf.dosador.temp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Maycon on 27/02/2015.
 */
public class FoodListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Food> listaFoods;

    public FoodListAdapter(Context contexto, ArrayList<Food> lista) {
        //Lista que preenche o listview
        this.listaFoods = lista;
        this.mInflater = LayoutInflater.from(contexto);
    }

    /**
     * Retorna a quantidade de patrimonios da lista.
     *
     * @return
     */
    @Override
    public int getCount() {
        return listaFoods.size();
    }

    /**
     * Retorna o patrimonio de acordo com a posição dele na tela.
     *
     * @param posicao
     * @return
     */
    @Override
    public Object getItem(int posicao) {
        return listaFoods.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        //return posicao;
        return Long.parseLong(listaFoods.get(posicao).getFood_id());
    }

    @Override
    public View getView(int posicao, View view, ViewGroup parent) {
        //Pega o item de acordo com a posição.
        Food itemFood = listaFoods.get(posicao);

           // view = mInflater.inflate(R.layout.item_listview_seleciona_sublocal, null);


        //Infla o layout para podermos preencher os dados.
        //view = mInflater.inflate(R.layout.item_listview_sublocal, null);

        //atraves do layou pego pelo LayoutInflaer, pegamos cada id relacionado
        //ao item e definimos as informações.
       // ((TextView) view.findViewById(R.id.tvItemSublocal)).setText(itemFood.getBrand_name());
        //((TextView) view.findViewById(R.id.tvNumPatrChecado)).setText("40");
        //ou
//        TextView tvItemSublocal = (TextView) view.findViewById(R.id.tvItemSublocal);
//        tvItemSublocal.setText(itemFood.getDescricao());

        /*if(posicao % 2 == 0) {
       	    view.setBackgroundColor(Color.parseColor("#ebebeb"));
        }*/


        return view;
    }
}

