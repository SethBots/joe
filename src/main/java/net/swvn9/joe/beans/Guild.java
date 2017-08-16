package net.swvn9.joe.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Guild {
    private String id = "";
    private String owner = "";
    private String literal = "!";
    private Boolean deleteInvoking = false;
    private Map<String,Role> roles;

    public String getId() {
        return id;
    }

    public String getLiteral() {
        return literal;
    }

    public Map<String, Role> getRoles() {
        return roles;
    }

    public String getOwner() {
        return owner;
    }

    public Boolean getDeleteInvoking() {
        return deleteInvoking;
    }
}
