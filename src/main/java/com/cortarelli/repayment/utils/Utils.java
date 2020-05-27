package com.cortarelli.repayment.utils;

public abstract class Utils {

    /**
     * Round a double value to two decimal places
     * @param value
     * @return rounded value with two decimal places
     */
    public static float round(double value) {
        return round(new Float(value));
    }

    /**
     * Round a float value to two decimal places
     * @param value
     * @return rounded value with two decimal places
     */
    public static float round(float value) {
        return (float) Math.round(value * 100) / 100;
    }

}
