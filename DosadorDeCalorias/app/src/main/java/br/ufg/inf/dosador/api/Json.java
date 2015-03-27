package br.ufg.inf.dosador.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 26/03/2015.
 */
public class Json {

    private final static String LOG_CAT = Json.class.getSimpleName();

    public final static String FOODS = "foods";
    public final static String FOOD = "food";
    public final static String SERVINGS = "servings";
    public final static String SERVING = "serving";

    public final static String FOOD_ID = "food_id";
    public final static String FOOD_NAME = "food_name";
    public final static String FOOD_TYPE = "food_type";
    public final static String BRAND_NAME = "brand_name";
    public final static String FOOD_URL = "food_url";
    public final static String FOOD_DESCRIPTION = "food_description";

    public final static String SERVING_ID = "serving_id";
    public final static String SERVING_DESCRIPTION = "serving_description";
    public final static String SERVING_URL = "serving_url";
    public final static String METRIC_SERVING_AMOUNT = "metric_serving_amount";
    public final static String METRIC_SERVING_UNIT = "metric_serving_unit";
    public final static String NUMBER_OF_UNITS = "number_of_units";
    public final static String MEASUREMENT_DESCRIPTION = "measurement_description";
    public final static String CALORIES = "calories";
    public final static String CARBOHYDRATE = "carbohydrate";
    public final static String PROTEIN = "protein";
    public final static String FAT = "fat";
    public final static String SATURATED_FAT = "saturated_fat";
    public final static String POLYUNSATURATED_FAT = "polyunsaturated_fat";
    public final static String MONOUNSATURATED_FAT = "monounsaturated_fat";
    public final static String TRANS_FAT = "trans_fat";
    public final static String CHOLESTEROL = "cholesterol";
    public final static String SODIUM = "sodium";
    public final static String POTASSIUM = "potassium";
    public final static String FIBER = "fiber";
    public final static String SUGAR = "sugar";
    public final static String VITAMIN_A = "vitamin_a";
    public final static String VITAMIN_C = "vitamin_c";
    public final static String CALCIUM = "calcium";
    public final static String IRON = "iron";


    public ArrayList<Alimento> obterListaAlimentoFromJson(String foodJsonStr) throws JSONException {

        ArrayList<Alimento> listaDeAlimentos = new ArrayList<Alimento>();
        Alimento alimento = null;

        try {
            JSONObject foodsJson = new JSONObject(foodJsonStr);
            JSONObject foods = foodsJson.getJSONObject(FOODS);
            JSONArray foodArray = foods.getJSONArray(FOOD);

            if (foodArray.length() <= 0) {
                return listaDeAlimentos;
            }
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject food = foodArray.getJSONObject(i);
                alimento = new Alimento();
                alimento.setFood_id(food.getInt(FOOD_ID));
                alimento.setFood_name(food.getString(FOOD_NAME));
                alimento.setFood_description(food.getString(FOOD_DESCRIPTION));
                listaDeAlimentos.add(alimento);
            }

            Log.d(LOG_CAT, "LIsta: " + listaDeAlimentos.size());

        } catch (JSONException e) {
            Log.e(LOG_CAT, e.getMessage(), e);
            e.printStackTrace();
        }
        return listaDeAlimentos;
    }

    public Alimento obterAlimentoFromJson(String foodJsonStr) {

        Alimento alimento = null;

        try {
            JSONObject foodsJson = new JSONObject(foodJsonStr);
            JSONObject food = foodsJson.getJSONObject(FOOD);

            alimento = new Alimento();
            alimento.setFood_id(food.getInt(FOOD_ID));
            alimento.setFood_name(food.getString(FOOD_NAME));

            JSONObject servings = food.getJSONObject(SERVINGS);
            JSONArray servingArray = servings.getJSONArray(SERVING);

            if (servingArray.length() <= 0) {
                return null;
            }
            JSONObject serving = servingArray.getJSONObject(0);
            alimento.setCalories(serving.getDouble(CALORIES));
            alimento.setFat(serving.getDouble(FAT));
            alimento.setCarbohydrate(serving.getDouble(CARBOHYDRATE));
            alimento.setProtein(serving.getDouble(PROTEIN));

//calories - cals
//fat - gord
//carbohydrate - carbs
//protein - prot


            Log.d(LOG_CAT, "LIsta: " + "sxfvdsafsa");

        } catch (JSONException e) {
            Log.e(LOG_CAT, e.getMessage(), e);
            e.printStackTrace();
        }
        return alimento;
    }
}
