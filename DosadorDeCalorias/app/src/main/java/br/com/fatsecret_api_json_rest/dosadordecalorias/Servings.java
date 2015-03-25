package br.com.fatsecret_api_json_rest.dosadordecalorias;

import java.util.List;

/**
 * Created by Maycon on 03/03/2015.
 */
public class Servings {

    public List<Serving> getServing() {
        return serving;
    }

    public void setServing(List<Serving> serving) {
        this.serving = serving;
    }

    private List<Serving> serving;
}
