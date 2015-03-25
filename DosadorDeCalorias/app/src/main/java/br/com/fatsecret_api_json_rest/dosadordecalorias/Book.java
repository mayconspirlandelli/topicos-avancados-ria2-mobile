package br.com.fatsecret_api_json_rest.dosadordecalorias;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maycon on 02/03/2015.
 */
public class Book {

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    private List<User> users;
}
