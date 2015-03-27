/**
 * Created by Maycon on 27/02/2015.
 */

package br.ufg.inf.dosador.temp;

import android.content.Context;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Random;

import android.net.Uri;
import java.net.URL;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


public class FatSecret {
    final static private String APP_METHOD = "GET";
    final static private String OAUTH_CONSUMER_KEY = "131418e143734b9e8496f811c793ff5b";
    final static public String OAUTH_ACCESS_SECRET = "623fe615f74d4c97921a25639bb2a92e&";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static Context context;

   //public static JSONObject getFood(String f) {
   public static String getFood(String f) {
        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
      //  params.add("method=foods.search");
      //  params.add("search_expression=" + Uri.encode(f));
       params.add("method=food.get");
       params.add("food_id=35755");


        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));

        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));


/*



            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));

            while ((line = reader.readLine()) != null) builder.append(line);
            // Log.w("Cookbox", food_object.toString());
            JSONObject json = new JSONObject(builder.toString());
            JSONObject foods_object = json.optJSONObject("foods");
            JSONArray foods_array = json.optJSONArray("foods");
            JSONObject food = null;

            if (foods_object == null) {
                if (foods_array != null) {
                    // List<String> names = new ArrayList<String>();
                    // for (int i = 0; i < foods_array.length(); i++) names.add(foods_array.getJSONObject(i).optString("food_name"));
                    // Log.w("Cookbox", f + " ~ " + StringUtils.join(names, ", "));
                    food = foods_array.getJSONObject(0);
                }
            } else {
                foods_array = foods_object.optJSONArray("food");
                if (foods_array != null) {
                    // List<String> names = new ArrayList<String>();
                    // for (int i = 0; i < foods_array.length(); i++) names.add(foods_array.getJSONObject(i).optString("food_name"));
                    // Log.w("Cookbox", f + " = " + StringUtils.join(names, ", "));
                    food = foods_array.optJSONObject(0);
                } else {
                    Log.w("Cookbox", f + " - nothing found(?)");
                }
            }

            String food_id = (food == null) ? null : food.optString("food_id");

            if (food_id != null) {
                params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
                params.add("method=food.get");
                params.add("food_id=" + food_id);
                params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));

                url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
                api = url.openConnection();
                builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(api.getInputStream()));

                while ((line = reader.readLine()) != null) builder.append(line);
                return new JSONObject(builder.toString());
                }
*/

            return url.toString();

        } catch (Exception e) {
            Log.w("Cookbox", "ooo kay");
            e.printStackTrace();
        }

       return null;

    }

    public static String[] generateOauthParams() {
        String[] a = {
                "oauth_consumer_key=" + OAUTH_CONSUMER_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"
        };
        return a;
    }

    public static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        SecretKey sk = new SecretKeySpec(OAUTH_ACCESS_SECRET.getBytes(), "HmacSHA1");
        try {
            Mac m = Mac.getInstance("HmacSHA1");
            m.init(sk);
            String signature = Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
            // Log.w("Cookbox", "signature: " + signature);
            return signature;
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("Cookbox", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("Cookbox", e.getMessage());
            return null;
        }
    }

    /**
     * Ordena os parametros
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