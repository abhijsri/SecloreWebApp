package com.oracle.casb.seclore.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By : abhijsri
 * Date  : 05/06/18
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Users"
})
public class Users {
    @JsonProperty("Users")
    private Map<Integer, User> users = new HashMap<>();

    @JsonProperty("Users")
    public Map<Integer, User> getUsers() {
        return users;
    }

    @JsonProperty("Users")
    public void setUsers(Map<Integer, User> users) {
        this.users = users;
    }
}
