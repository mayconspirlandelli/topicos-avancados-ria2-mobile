package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import java.util.HashMap;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.app.ConsumoDiarioActivity;
import br.ufg.inf.dosador.data.DosadorContract;

/**
 * Created by Maycon on 23/04/2015.
 */
public class TruitonSimpleCursorTreeAdapter extends CursorTreeAdapter {

    private final String LOG_TAG = getClass().getSimpleName().toString();

    private LayoutInflater mInflater;
    private ConsumoDiarioActivity mActivity;
    protected final HashMap<Integer, Integer> mGroupMap;

    public TruitonSimpleCursorTreeAdapter(Context context, Cursor cursor) {
        super(cursor, context);
        mActivity = (ConsumoDiarioActivity) context;
        mInflater = LayoutInflater.from(context);
        mGroupMap = new HashMap<Integer, Integer>();
    }


    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Given the group, we return a cursor for all the children within that
        // group
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor
                .getColumnIndex(DosadorContract.ConsumoEntry._ID));

        Log.d(LOG_TAG, "getChildrenCursor() for groupPos " + groupPos);
        Log.d(LOG_TAG, "getChildrenCursor() for groupId " + groupId);

        mGroupMap.put(groupId, groupPos);

        Loader loader = mActivity.getSupportLoaderManager().getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            mActivity.getSupportLoaderManager().restartLoader(groupId, null, mActivity);
        } else {
            mActivity.getSupportLoaderManager().initLoader(groupId, null, mActivity);
        }

        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {

        final View view = mInflater.inflate(R.layout.list_group_consumo, parent, false);
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);

        if (lblListHeader != null) {
            lblListHeader.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
        }

        //TextView txtNome = (TextView) view.findViewById(R.id.item_nome);

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.list_item_consumo, parent, false);
        return view;

    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        TextView txtListChild = (TextView) view.findViewById(R.id.item_nome);

        if (txtListChild != null) {
            txtListChild.setText(cursor.getString(1)); // Selects E-mail or
            // Display Name
        }
    }

    // Access method
    public HashMap<Integer, Integer> getGroupMap() {
        return mGroupMap;
    }
}
