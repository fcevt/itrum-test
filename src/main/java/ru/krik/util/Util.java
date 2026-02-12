package ru.krik.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    byte CENT_IN_DOLLAR = 100;

    public static double convertCentsToDollars(double cents){
        return cents / CENT_IN_DOLLAR;
    }

    public static long convertDollarsToCents(double dollars) {
        return (long) dollars * CENT_IN_DOLLAR;
    }
}
