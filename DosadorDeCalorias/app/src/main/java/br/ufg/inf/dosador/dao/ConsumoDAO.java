package br.ufg.inf.dosador.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.Consumo;
import br.ufg.inf.dosador.entidades.TipoRefeicao;

/**
 * Created by Maycon on 16/04/2015.
 */

public class ConsumoDAO {
    private Context context;

    public ConsumoDAO(Context ctx) {
        this.context = ctx;
    }


    public static ContentValues createContentValuesConsumo(Consumo consumo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_NOME, consumo.getFood_name());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_QTD, consumo.getQuantidade());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CALORIAS, consumo.getCalories());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_GORDURA, consumo.getFat());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO, consumo.getCarbohydrate());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_PROTEINA, consumo.getProtein());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_TIPO_REFEICAO, consumo.getTipoRefeicao());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_DATA, consumo.getData().toString());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_PORCAO, consumo.getServing_description());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_FOOD_ID, consumo.getFood_id());
        return contentValues;
    }

    private long inserir(Consumo consumo) {
        Uri uri = context.getContentResolver().insert(DosadorContract.ConsumoEntry.CONTENT_URI, createContentValuesConsumo(consumo));

        long consumoRowId = ContentUris.parseId(uri);
        if (consumoRowId != -1) {
            //consumo.setID(consumoRowId);
        }
        return consumoRowId;
    }

    private int atualizar(Consumo consumo) {
        Uri uri = DosadorContract.ConsumoEntry.CONTENT_URI;
        int updatedRows = context.getContentResolver()
                .update(uri, createContentValuesConsumo(consumo),
                        DosadorContract.ConsumoEntry._ID + " = ? ",
                        new String[]{Long.toString(consumo.getId())});

        return updatedRows;
    }

    public void salvar(Consumo consumo) {
        if (consumo.getId() == 0) {
            inserir(consumo);
        } else {
            atualizar(consumo);
        }
    }

    public int excluir(Consumo consumo) {
        Uri uri = DosadorContract.ConsumoEntry.CONTENT_URI;
        int deletedRows = context.getContentResolver()
                .delete(uri, DosadorContract.ConsumoEntry._ID + " = ? ",
                        new String[]{Long.toString(consumo.getId())});

        return deletedRows;
    }

    public CursorLoader buscarPorData(Context ctx, String data) {
        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorData(data),
                null,
                null,
                null,
                null);
    }

    public CursorLoader buscarPorDataAndTipoRefeicao(Context ctx, String data, String tipoRefeicao) {
        String tipo = "";

        if (tipoRefeicao.equals(TipoRefeicao.CAFE_DA_MANHA.name())) {
            tipo = TipoRefeicao.CAFE_DA_MANHA.name();
        } else if (tipoRefeicao.equals(TipoRefeicao.ALMOCO.name())) {
            tipo = TipoRefeicao.ALMOCO.name();
        } else if (tipoRefeicao.equals(TipoRefeicao.LANCHE.name())) {
            tipo = TipoRefeicao.LANCHE.name();
        } else if (tipoRefeicao.equals(TipoRefeicao.JANTAR.name())) {
            tipo = TipoRefeicao.JANTAR.name();
        }

        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorDataAndTipoRefeicao(data, tipo),
                null,
                null,
                null,
                null);
    }

    public CursorLoader buscarTodosTipoRefeicao(Context ctx) {
        return new CursorLoader(ctx,
                DosadorContract.TipoRefeicaoEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    public CursorLoader buscarPorPeriodo(Context ctx, String dataInicial, String dataFinal) {
        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorPeriodo(dataInicial, dataFinal),
                null,
                null,
                null,
                null);
    }

    public CursorLoader buscarPorMes(Context ctx, String mes) {
        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorMes(mes),
                null,
                null,
                null,
                DosadorContract.ConsumoEntry.COLUMN_DATA + " DESC ");
    }

    public static Consumo obterConsumoFromCursor(Cursor cursor) {
        Consumo consumo = new Consumo();
        //try {
        consumo.setId(cursor.getLong(cursor.getColumnIndex(DosadorContract.ConsumoEntry._ID)));
        //consumo.setData(new Date(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA))));
        consumo.setQuantidade(cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_QTD)));
        consumo.setTipoRefeicao(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_TIPO_REFEICAO)));
        consumo.setFood_id(cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_FOOD_ID)));
        consumo.setFood_name(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
        consumo.setCalories(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS)));
        consumo.setFat(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_GORDURA)));
        consumo.setCarbohydrate(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO)));
        consumo.setProtein(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_PROTEINA)));
        consumo.setServing_description(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_PORCAO)));

        //}
        //finally {
//            cursor.close();
//        }
        return consumo;
    }
}
