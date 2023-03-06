package tech.carrotly.restapi.chat.dtos;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class GsonSerializable implements Serializable {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
