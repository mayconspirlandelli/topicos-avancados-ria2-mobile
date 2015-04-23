package br.ufg.inf.dosador.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.entidades.Consumo;
import br.ufg.inf.dosador.entidades.Usuario;


/**
 * Created by Maycon on 01/04/2015.
 */
public class DosadorContract {

    public static final String CONTENT_AUTHORITY = "br.ufg.inf.dosador.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CONSUMO = "consumo";
    public static final String PATH_USUARIO = "usuario";


    public static long normalizeDate(long date) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(date);
        int julianDay = Time.getJulianDay(date, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    public static ContentValues createContentValuesUsuario(Usuario usuario) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DosadorContract.UsuarioEntry.COLUMN_NOME, usuario.getNome());
        contentValues.put(DosadorContract.UsuarioEntry.COLUMN_SEXO, usuario.getSexo());
        contentValues.put(DosadorContract.UsuarioEntry.COLUMN_IDADE, usuario.getIdade());
        contentValues.put(DosadorContract.UsuarioEntry.COLUMN_ALTURA, usuario.getAltura());
        contentValues.put(DosadorContract.UsuarioEntry.COLUMN_PESO, usuario.getPeso());
        return contentValues;
    }

    /**
     * Tabela: CONSUMO, registra todos os consumos diários da pessoa.
     */
    public static final class ConsumoEntry implements BaseColumns {

        // O aplicativo deve armazenar o ID, nome, quantidade, calorias, gordura, carboidratos e proteínas do alimento, tipo de refeição e data.

        //content://br.ufg.inf.dosador.app/consumo
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONSUMO).build();

        //vnd.android.cursor.dir/br.ufg.inf.dosador.app/consumo
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSUMO;

        //vnd.android.cursor.item/br.ufg.inf.dosador.app/consumo
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSUMO;

        public static final String TABLE_NAME = "consumo";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_QTD = "quantidade";
        public static final String COLUMN_CALORIAS = "calorias";
        public static final String COLUMN_GORDURA = "gordura";
        public static final String COLUMN_CARBOIDRATO = "carboidrato";
        public static final String COLUMN_PROTEINA = "proteina";
        public static final String COLUMN_TIPO_REFEICAO = "refeicao";
        public static final String COLUMN_DATA = "data";

        //content://br.ufg.inf.dosador.app/consumo/22
        public static Uri buildConsumoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://br.ufg.inf.dosador.app/consumo/1419033600
        public static Uri buildConsumoPorData(String data) {
            return CONTENT_URI.buildUpon().appendPath(data).build();
        }

        //content://br.ufg.inf.dosador.app/consumo/1419033600/1519033600
        public static Uri buildConsumoPorPeriodo(String dataInicial, String dataFinal) {
            return CONTENT_URI.buildUpon().
                    appendPath(dataInicial).
                    appendPath(dataFinal).
                    build();
        }

        //content://br.ufg.inf.dosador.app/consumo/mes=01
        public static Uri buildConsumoPorMes(String mes) {
            return CONTENT_URI.buildUpon().
                    appendQueryParameter("mes", mes).
                    build();
        }

        //TODO: em teste
        public static Uri buildConsumoPorDiarioAndTipoRefeicao(String data, String tipoRefeicao) {
            return CONTENT_URI.buildUpon().
                    appendPath(data).
                    appendPath(tipoRefeicao).
                    build();
        }

        public static String getDataDiariaFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDataInicialFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDataFinalFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        //TODO: corrigir o parametro date.
        public static String getMesFromUri(Uri uri) {
            String dateString = uri.getQueryParameter("mes");
            if (dateString != null && !dateString.isEmpty()) {
                return dateString;
            } else
                return null;
        }

        //TODO: em teste
        public static String getTipoRefeicaoFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

    /**
     * Renilson 17/04/15
     * Tabela: USUARIO, registra os dados da pessoa.
     */
    public static final class UsuarioEntry implements BaseColumns {

        // O aplicativo deve armazenar o ID, nome, idade, sexo, peso e altura.

        //content://br.ufg.inf.dosador.app/usuario
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USUARIO).build();

        //vnd.android.cursor.dir/br.ufg.inf.dosador.app/usuario
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIO;

        //vnd.android.cursor.item/br.ufg.inf.dosador.app/usuario
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIO;

        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_SEXO = "sexo";
        public static final String COLUMN_IDADE = "idade";
        public static final String COLUMN_PESO = "peso";
        public static final String COLUMN_ALTURA = "altura";

        //content://br.ufg.inf.dosador.app/usuario/22
        public static Uri buildUsuarioUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
