package br.ufg.inf.dosador.api;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import br.ufg.inf.dosador.R;

/**
 * Created by Maycon on 25/03/2015.
 */
public class FatSecret {

    //final static private String LOG_CAT = Resources.getSystem().getString(R.string.app_name) + "." + FatSecret.class.getSimpleName();
    final static private String LOG_CAT = FatSecret.class.getSimpleName();

    final static private String APP_METHOD_GET = "GET";
    final static private String APP_METHOD_POST = "POST";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    final static private String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    final static private String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    final static private String OAUTH_SIGNATURE = "oauth_signature";
    final static private String HMAC_SHA1 = "HMAC-SHA1";
    final static private String HMAC_SHA1_VALUE = "HmacSHA1";
    final static private String OAUTH_TIMESTAMP = "oauth_timestamp";
    final static private String OAUTH_NONCE = "oauth_nonce";
    final static private String OAUTH_VERSION = "oauth_version";
    final static private String OAUTH_VERSION_VALUE = "1.0";
    final static private String FORMAT = "format";
    final static private String FORMAT_JSON = "json";
    final static private String FORMAT_XML = "xml";

    final static private String METHOD = "method";
    final static public String METHOD_FOODS_SEARCH = "foods.search";
    final static public String METHOD_FOOD_GET = "food.get";
    final static private String FOOD_ID = "food_id";
    final static private String SEARCH_EXPRESSION = "search_expression";
    final static private String PAGE_NUMBER = "page_number";
    final static private String MAX_RESULTS = "max_results";

    final static public String ERROR = "error";


    private static Context mContext;

    private static String getOauthConsumerKey() {
        return mContext.getResources().getString(R.string.oauth_consumer_key);
    }

    private static String getOauthAccessSecret() {
        return mContext.getResources().getString(R.string.oauth_access_secret);
    }


    public FatSecret(Context context){
        this.mContext = context;
    }

    public static URL pesquisarAlimentoPorID(String id) {
        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add(METHOD + "=" + METHOD_FOOD_GET);
        params.add(FOOD_ID + "=" + id);
        params.add(OAUTH_SIGNATURE + "=" + sign(APP_METHOD_GET, APP_URL, params.toArray(template)));

        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            return url;

        } catch (Exception e) {
            Log.d(LOG_CAT, "ooo kay");
            e.printStackTrace();
        }
        return null;
    }

    public static URL pesquisarAlimentoPorExpressao(String expressao) {
        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add(METHOD + "=" + METHOD_FOODS_SEARCH);
        params.add(SEARCH_EXPRESSION + "=" + Uri.encode(expressao));
        params.add(OAUTH_SIGNATURE + "=" + sign(APP_METHOD_GET, APP_URL, params.toArray(template)));

        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            return url;

        } catch (Exception e) {
            Log.d(LOG_CAT, "ooo kay");
            e.printStackTrace();
        }
        return null;
    }


    public static String[] generateOauthParams() {
        String[] a = {
                OAUTH_CONSUMER_KEY + "=" + getOauthConsumerKey(),
                OAUTH_SIGNATURE_METHOD + "=" + HMAC_SHA1,
                OAUTH_TIMESTAMP + "=" + new Long(System.currentTimeMillis() / 1000).toString(),
                OAUTH_NONCE + "=" + nonce(),
                OAUTH_VERSION + "=" + OAUTH_VERSION_VALUE,
                FORMAT + "=" + FORMAT_JSON
        };
        return a;
    }

    /**
     * Método para obter a assinatura.
     * @param method
     * @param uri
     * @param params
     * @return
     */
    public static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        SecretKey sk = new SecretKeySpec((getOauthAccessSecret()+"&").getBytes(), HMAC_SHA1_VALUE);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_VALUE);
            m.init(sk);
            String signature = Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
            Log.d(LOG_CAT, "signature: " + signature);
            return signature;
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.d(LOG_CAT, e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.d(LOG_CAT, e.getMessage());
            return null;
        }
    }

    /**
     * Ordena os parametros
     *
     * @param params
     * @return
     */
    public static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    public static String join(String[] array, String separator) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    public static String nonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) n.append(r.nextInt(26) + 'a');
        return n.toString();
    }
}




