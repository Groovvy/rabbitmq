package com.groovvy.rabbitmq.entity;

import java.io.Serializable;

/**
 * @author wanghuaan
 * @date 2020/10/29
 **/
public class User implements Serializable {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
