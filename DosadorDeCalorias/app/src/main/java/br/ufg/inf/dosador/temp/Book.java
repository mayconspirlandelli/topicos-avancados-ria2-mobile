package br.ufg.inf.dosador.temp;

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
