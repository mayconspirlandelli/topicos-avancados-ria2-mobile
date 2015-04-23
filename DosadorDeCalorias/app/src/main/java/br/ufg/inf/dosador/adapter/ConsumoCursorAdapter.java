package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;

/**
 * Created by Maycon on 22/04/2015.
 */
public class ConsumoCursorAdapter extends CursorAdapter {

    public ConsumoCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNome = (TextView) view.findViewById(R.id.item_nome);
        txtNome.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_consumo, null);
    }
}
