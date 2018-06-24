package com.automic.global.util;

public enum ColorEnum {
    YELLOW(45),BROWN(4),RED(11),BLUE(20),ORANGE(33);
    
    private int value;
    
    private ColorEnum(int value){
        this.value = value;
    }
    
     public int getValue() {
        return value;
    }
}
