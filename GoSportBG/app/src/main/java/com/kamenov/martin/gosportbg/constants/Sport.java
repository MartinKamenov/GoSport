package com.kamenov.martin.gosportbg.constants;


public enum Sport {
    Football(1),
    Basketball(2),
    Volleyball(3),
    Tennis(4),
    Ping_Pong(5),
    Boxing(6);

    private int value;

    public int getValue() {
        return value;
    }

    Sport(int value) {
        this.value = value;
    }
}
