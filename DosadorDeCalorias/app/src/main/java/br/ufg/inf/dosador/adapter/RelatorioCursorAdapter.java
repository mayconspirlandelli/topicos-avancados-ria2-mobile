package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.data.DosadorContract;

/**
 * Created by Maycon on 17/04/2015.
 */
public class RelatorioCursorAdapter extends CursorAdapter {

    public RelatorioCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtData = (TextView) view.findViewById(R.id.item_data);
        TextView txtCalorias = (TextView) view.findViewById(R.id.item_calorias);
        txtCalorias.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS)));
        Calendar calendar = Util.convertDataFromStringToCalendar(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA)), "yyyy-MM-dd");
        txtData.setText(Util.convertDataFromCalendarToString(calendar, "dd/MM/yyyy"));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_relatorio, null);
    }
}
