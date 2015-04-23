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
//TODO: a tela que usarão essa classe: ConsumoDiarioActivity(consultar e excluir, editar chama a tela de DetalhesPesquisaFrament, mas não implementaremos o editar), DetalhesPesquisaActivity(salvar e editar) e RelatorioActivity (consultar).
public class ConsumoDAO {
    private Context context;

    public ConsumoDAO(Context ctx) {
        this.context = ctx;
    }


    public static ContentValues createContentValuesConsumo(Consumo consumo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_NOME, consumo.getNomeAlimento());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_QTD, consumo.getQuantidade());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CALORIAS, consumo.getCalorias());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_GORDURA, consumo.getGorduras());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO, consumo.getCarboidratos());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_PROTEINA, consumo.getProteinas());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_TIPO_REFEICAO, consumo.getTipoRefeicao());
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_DATA, consumo.getData().toString());
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
        //Uri uri = Uri.withAppendedPath(DosadorContract.ConsumoEntry.CONTENT_URI, String.valueOf())
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoUri(consumo.getId());
        int updatedRows = context.getContentResolver()
                .update(uri, createContentValuesConsumo(consumo), null, null);

        return updatedRows;
        /**
         * int count = mContext.getContentResolver().update(
         ConsumoEntry.CONTENT_URI, updatedValues, ConsumoEntry._ID + "= ?",
         new String[] { Long.toString(consumoRowId)});

         */
    }

    public void salvar(Consumo consumo) {
        if (consumo.getId() == 0) {
            inserir(consumo);
        } else {
            atualizar(consumo);
        }
    }

    public int excluir(Consumo consumo) {
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoUri(consumo.getId());
        int deletedRows = context.getContentResolver()
                .delete(uri, null, null);
        return deletedRows;
    }

    //TODO: falta implementar por ID.
//    public CursorLoader buscarPorId(Context ctx, String s){
//
//    }

    public CursorLoader buscarPorData(Context ctx, String data) {
        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorData(data),
                null,
                null,
                null,
                null);
    }

    //TODO: somente para teste.
    public CursorLoader buscarPorDataAndTipoRefeicao(Context ctx, String data, String tipoRefeicao) {

        String tipo = TipoRefeicao.LANCHE.name();
//
//        if (tipoRefeicao.equals(TipoRefeicao.CAFE_DA_MANHA.name())) {
//            tipo = TipoRefeicao.CAFE_DA_MANHA.name();
//
//        }
//        if (tipoRefeicao.equals(TipoRefeicao.ALMOCO.name())) {
//            tipo = TipoRefeicao.ALMOCO.name();
//        }

        return new CursorLoader(ctx,
                DosadorContract.ConsumoEntry.buildConsumoPorDiarioAndTipoRefeicao(data, tipo),
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
                null);
    }

    public static Consumo obterConsumoFromCursor(Cursor cursor) {
        Consumo consumo = new Consumo();
        try {
            consumo.setId(cursor.getLong(cursor.getColumnIndex(DosadorContract.ConsumoEntry._ID)));
            consumo.setNomeAlimento(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_NOME)));
            consumo.setCalorias(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS)));
            consumo.setGorduras(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_GORDURA)));
            consumo.setCarboidratos(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO)));
            consumo.setProteinas(cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_PROTEINA)));
            //consumo.setData(new Date(cursor.getString(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_DATA))));
            consumo.setQuantidade(cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_QTD)));
            //consumo.setTipoRefeicao();
        } finally {
            cursor.close();
        }
        return consumo;
    }
}
