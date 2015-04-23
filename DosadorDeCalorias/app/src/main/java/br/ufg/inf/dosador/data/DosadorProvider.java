package br.ufg.inf.dosador.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Maycon on 02/04/2015.
 */
public class DosadorProvider extends ContentProvider {

    private static final String LOG_CAT = DosadorProvider.class.getSimpleName();


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DosadorDbHelper mOpenHelper;

    static final int CONSUMO = 100;
    static final int CONSUMO_DIARIO = 101;
    static final int CONSUMO_PERIODO = 102;
    static final int CONSUMO_MENSAL = 103;
    static final int CONSUMO_DIARIO_TIPO_REFEICAO = 104;

    static final int USUARIO = 200;


    private static SQLiteQueryBuilder sConsumoQueryBuilder = new SQLiteQueryBuilder();
    private static SQLiteQueryBuilder sUsuarioQueryBuilder = new SQLiteQueryBuilder();

    static {
        sConsumoQueryBuilder.setTables(DosadorContract.ConsumoEntry.TABLE_NAME);
    }

    static {
        sUsuarioQueryBuilder.setTables(DosadorContract.UsuarioEntry.TABLE_NAME);
    }


    //Pesquisar consumo por data ou diário.
    //select * from consumo where strftime('%Y-%m-%d', data) = '2015-01-01';
//    private static final String sConsumoPorData =
//            "strftime('%Y-%m-%d', " +
//            DosadorContract.ConsumoEntry.TABLE_NAME + "." +
//                    DosadorContract.ConsumoEntry.COLUMN_DATA + ")" + " = ? ";
    //OU
    private static final String sConsumoPorData =
            DosadorContract.ConsumoEntry.TABLE_NAME + "." +
                    DosadorContract.ConsumoEntry.COLUMN_DATA + " = ? ";

    //Pesquisar consumo por periodo específico.
    //select * from consumo where strftime('%Y-%m-%d', data) between '2015-01-01' and '2015-02-02';
    private static final String sConsumoPorPeriodo =
            DosadorContract.ConsumoEntry.TABLE_NAME + "." +
                    DosadorContract.ConsumoEntry.COLUMN_DATA + " BETWEEN ? AND ? ";

    //Pesquisar consumo por mes.
    //select * from consumo where strftime('%m', data) = '01';
    private static final String sConsumoPorMes =
            DosadorContract.ConsumoEntry.TABLE_NAME + "." +
                    "strftime('%m', " + DosadorContract.ConsumoEntry.COLUMN_DATA + " ) " + " = ? ";


    //Pesquisar (DEFAULT) todos os consumos ordenados por data, o primeiro da lista é o ultimo consumo.
    private Cursor getTodosConsumos(Uri uri, String[] projection, String sortOrder) {
        return sConsumoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    //Pesquisar consumo por data ou diário.
    private Cursor getConsumoPorData(Uri uri, String[] projection, String sortOrder) {
        String data = DosadorContract.ConsumoEntry.getDataDiariaFromUri(uri);

        return sConsumoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sConsumoPorData,
                new String[]{data},
                null,
                null,
                sortOrder
        );
    }

    //Pesquisar consumo por periodo específico.
    private Cursor getConsumoPorPeriodo(Uri uri, String[] projection, String sortOrder) {
        String dataInicial = DosadorContract.ConsumoEntry.getDataInicialFromUri(uri);
        String dataFinal = DosadorContract.ConsumoEntry.getDataFinalFromUri(uri);

        return sConsumoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sConsumoPorPeriodo,
                new String[]{dataInicial, dataFinal},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getConsumoMensal(Uri uri, String[] projection, String sortOrder) {
        String mes = DosadorContract.ConsumoEntry.getMesFromUri(uri);

        return sConsumoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sConsumoPorMes,
                new String[]{mes},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getConsumoPorDataAndTipoRefeicao(Uri uri, String[] projection, String sortOrder) {
        String data = DosadorContract.ConsumoEntry.getDataDiariaFromUri(uri);
        String tipoRefeicao = DosadorContract.ConsumoEntry.getTipoRefeicaoFromUri(uri);

        return sConsumoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sConsumoPorData,
                new String[]{data, tipoRefeicao},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DosadorContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DosadorContract.PATH_CONSUMO, CONSUMO);
        matcher.addURI(authority, DosadorContract.PATH_CONSUMO + "/*", CONSUMO_DIARIO);
        matcher.addURI(authority, DosadorContract.PATH_CONSUMO + "/*/*", CONSUMO_PERIODO);

        //TODO: colocar outro URI para o consumo mensal.
        //matcher.addURI(authority, DosadorContract.PATH_CONSUMO + "", CONSUMO_MENSAL);

        //TODO: tem teste, http://developer.android.com/reference/android/content/UriMatcher.html
        matcher.addURI(authority, DosadorContract.PATH_CONSUMO + "/*" + CONSUMO_DIARIO + "/#", CONSUMO_DIARIO_TIPO_REFEICAO);


        matcher.addURI(authority, DosadorContract.PATH_USUARIO, USUARIO);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DosadorDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case CONSUMO: {
                retCursor = mOpenHelper.getReadableDatabase().query(DosadorContract.ConsumoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case USUARIO: {
                retCursor = mOpenHelper.getReadableDatabase().query(DosadorContract.UsuarioEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CONSUMO_DIARIO: {
                retCursor = getConsumoPorData(uri, projection, sortOrder);
                break;
            }
            case CONSUMO_PERIODO: {
                retCursor = getConsumoPorPeriodo(uri, projection, sortOrder);
                break;
            }
            case CONSUMO_MENSAL: {
                retCursor = getConsumoMensal(uri, projection, sortOrder);
                break;
            }
            case CONSUMO_DIARIO_TIPO_REFEICAO: {
                retCursor = getConsumoPorDataAndTipoRefeicao(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USUARIO:
                return DosadorContract.UsuarioEntry.CONTENT_TYPE;
            case CONSUMO:
                return DosadorContract.ConsumoEntry.CONTENT_TYPE;
            case CONSUMO_DIARIO:
                return DosadorContract.ConsumoEntry.CONTENT_ITEM_TYPE;
            case CONSUMO_PERIODO:
                return DosadorContract.ConsumoEntry.CONTENT_ITEM_TYPE;
            case CONSUMO_MENSAL:
                return DosadorContract.ConsumoEntry.CONTENT_ITEM_TYPE;
            case CONSUMO_DIARIO_TIPO_REFEICAO:
                return DosadorContract.ConsumoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CONSUMO: {
                long id = db.insert(DosadorContract.ConsumoEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = DosadorContract.ConsumoEntry.buildConsumoUri(id);
                else
                    throw new SQLException("Falha ao inserir a linha " + uri);
                break;
            }
            case USUARIO: {
                long id = db.insert(DosadorContract.UsuarioEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = DosadorContract.UsuarioEntry.buildUsuarioUri(id);
                else
                    throw new SQLException("Falha ao inserir a linha " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {
            case CONSUMO: {
                rowsDeleted = db.delete(DosadorContract.ConsumoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case USUARIO: {
                rowsDeleted = db.delete(DosadorContract.UsuarioEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case CONSUMO: {
                rowsUpdated = db.update(DosadorContract.ConsumoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case USUARIO: {
                rowsUpdated = db.update(DosadorContract.UsuarioEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
