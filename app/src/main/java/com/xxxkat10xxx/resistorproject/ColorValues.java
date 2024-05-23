package com.xxxkat10xxx.resistorproject;

import java.util.HashMap;
import java.util.Map;

public class ColorValues {

    public static final Integer UNKNOWN_COLOR_VALUE = -1;
    private static Map<ColorName, Integer> colorValues = new HashMap<>();
    static {
        colorValues.put(ColorName.Black, 0);
        colorValues.put(ColorName.Brown, 1);
        colorValues.put(ColorName.Red, 2);
        colorValues.put(ColorName.Orange, 3);
        colorValues.put(ColorName.Yellow, 4);
        colorValues.put(ColorName.Green, 5);
        colorValues.put(ColorName.Blue, 6);
        colorValues.put(ColorName.Violet, 7);
        colorValues.put(ColorName.Grey, 8);
        colorValues.put(ColorName.White, 9);
    }

    public static int getValueForColor(ColorName colorName) {
        if (colorValues.containsKey(colorName)) {
            return colorValues.get(colorName);
        }

        return UNKNOWN_COLOR_VALUE;
    }
}
