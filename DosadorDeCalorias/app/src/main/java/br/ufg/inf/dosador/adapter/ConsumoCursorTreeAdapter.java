package br.ufg.inf.dosador.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.app.ConsumoDiarioActivity;
import br.ufg.inf.dosador.data.DosadorContract;

/**
 * Created by Maycon on 23/04/2015.
 */
public class ConsumoCursorTreeAdapter extends CursorTreeAdapter {

    private final String LOG_TAG = getClass().getSimpleName().toString();

    private LayoutInflater mInflater;
    private ConsumoDiarioActivity mActivity;
    protected final HashMap<Integer, Integer> mGroupMap;

    customButtonListener customListner;
    customOnItemListener customOnItemListener;
    customOnItemChildListener customOnItemChildListener;
    customOnLongListener customOnLongListener;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String tipoRefeicao);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public interface customOnItemListener {
        public void onItemClickListener(int position);
    }

    public void setCustomOnItemListener(customOnItemListener listener) {
        this.customOnItemListener = listener;
    }

    public interface customOnItemChildListener {
        public void onItemChildClickListener(int position, Cursor cursor);
    }

    public void setCustomOnItemChildListener(customOnItemChildListener listener) {
        this.customOnItemChildListener = listener;
    }

    public interface customOnLongListener {
        public void onLongClickListener(int position, Cursor cursor);
    }

    public void setCustomOnLongListener(customOnLongListener listener) {
        this.customOnLongListener = listener;
    }


    public ConsumoCursorTreeAdapter(Context context, Cursor cursor) {
        super(cursor, context);
        mActivity = (ConsumoDiarioActivity) context;
        mInflater = LayoutInflater.from(context);
        mGroupMap = new HashMap<Integer, Integer>();
    }


    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Given the group, we return a cursor for all the children within that group
        int groupPos = groupCursor.getPosition();

        int groupId = groupCursor.getInt(groupCursor.getColumnIndex(DosadorContract.TipoRefeicaoEntry._ID));

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
    protected View newGroupView(Context context, Cursor cursor, final boolean isExpanded, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.list_group_consumo, parent, false);
        final int groupPosition = cursor.getPosition();
        final String tipoRefeicao = cursor.getString(cursor.getColumnIndex(DosadorContract.TipoRefeicaoEntry.COLUMN_NOME));

        ImageButton btnAdicionarAlimento = (ImageButton) view.findViewById(R.id.btn_adicionar_alimento);
        btnAdicionarAlimento.setFocusable(false);
        btnAdicionarAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(groupPosition, tipoRefeicao);
                }
            }
        });
        return view;
    }


    @Override
    protected void bindGroupView(View view, Context context, final Cursor cursor, boolean isExpanded) {
        TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);

        if (lblListHeader != null) {
            lblListHeader.setText(cursor.getString(cursor.getColumnIndex(DosadorContract.TipoRefeicaoEntry.COLUMN_NOME)));
        }

        final int groupPosition = cursor.getPosition();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customOnItemListener != null) {
                    customOnItemListener.onItemClickListener(groupPosition);
                }
            }
        });
    }

    @Override
    protected View newChildView(Context context,  final Cursor cursor, boolean isLastChild, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.list_item_consumo, parent, false);

        final ExpandableListView expand = (ExpandableListView) parent.findViewById(R.id.expand_lista_almoco);
        final int groupPosition = cursor.getPosition();
        final int childId = cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry._ID));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customOnItemChildListener != null) {
                    cursor.moveToPosition(groupPosition);
                    customOnItemChildListener.onItemChildClickListener(childId, cursor);
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (customOnLongListener != null) {
                    cursor.moveToPosition(groupPosition);
                    customOnLongListener.onLongClickListener(childId, cursor);
                }
                return true;
            }
        });


        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, final Cursor cursor, boolean isLastChild) {
        TextView txtListChild = (TextView) view.findViewById(R.id.item_nome);

        if (txtListChild != null) {
            txtListChild.setText(cursor.getString(1));
        }
    }

    public HashMap<Integer, Integer> getGroupMap() {
        return this.mGroupMap;
    }

}
