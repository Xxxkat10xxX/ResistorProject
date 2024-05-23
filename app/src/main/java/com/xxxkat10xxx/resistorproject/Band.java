package com.xxxkat10xxx.resistorproject;

public class Band {
    private ColorName color;
    private int width;
    public Band(ColorName color, int width) {
        this.color = color;
        this.width = width;
    }
    public ColorName getColor() {
        return color;
    }
    public int getWidth() {
        return width;
    }
}
