package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;

/**
 * Created by Maycon on 17/04/2015.
 */
public class RelatorioCursorAdapter extends CursorAdapter {

    public RelatorioCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNome = (TextView) view.findViewById(R.id.item_nome);
        TextView txtData = (TextView) view.findViewById(R.id.item_data);
        TextView txtCalorias = (TextView) view.findViewById(R.id.item_calorias);

        txtNome.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
        txtData.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA)));
        txtCalorias.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_relatorio, null);
    }
}
