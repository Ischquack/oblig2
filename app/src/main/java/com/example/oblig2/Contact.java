package com.example.oblig2;

public class Contact {
    private String name;
    private String tel;
    private int id;



    public Contact(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }
    public Contact(){}

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
