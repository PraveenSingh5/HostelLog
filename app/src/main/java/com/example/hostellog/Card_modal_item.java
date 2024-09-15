package com.example.hostellog;

public class Card_modal_item {
    String c_email, c_room , c_complain,c_complain_type;
    int id;

    public Card_modal_item(String c_email, String c_room, String c_complain, String c_complain_type, int id) {
        this.c_email = c_email;
        this.c_room = c_room;
        this.c_complain = c_complain;
        this.c_complain_type = c_complain_type;
        this.id = id;
    }

    public Card_modal_item(int id) {
        this.id = id;
    }

    public Card_modal_item(String c_email, String c_room, String c_complain, String c_complain_type) {
        this.c_email = c_email;
        this.c_room = c_room;
        this.c_complain = c_complain;
        this.c_complain_type = c_complain_type;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_room() {
        return c_room;
    }

    public void setC_room(String c_room) {
        this.c_room = c_room;
    }

    public String getC_complain() {
        return c_complain;
    }

    public void setC_complain(String c_complain) {
        this.c_complain = c_complain;
    }

    public String getC_complain_type() {
        return c_complain_type;
    }

    public void setC_complain_type(String c_complain_type) {
        this.c_complain_type = c_complain_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
