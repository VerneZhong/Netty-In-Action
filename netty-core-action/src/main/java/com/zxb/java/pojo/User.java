package com.zxb.java.pojo;

import java.io.Serializable;

/**
 * 用户 POJO
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class User implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
