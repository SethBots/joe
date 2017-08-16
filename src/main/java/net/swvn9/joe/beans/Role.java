package net.swvn9.joe.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    public String id = "";
    public boolean admin = false;
    public List<String> permissions = new ArrayList<>(Collections.singletonList("none"));
}
