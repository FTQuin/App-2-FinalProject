package com.example.anon;

import androidx.test.espresso.idling.CountingIdlingResource;

import kotlin.jvm.JvmField;

public class EspressoIdlingResource {

    @JvmField
    private static String CLASS_NAME = "EspressoIdlingResource";

    private static String RESOURCE = "GLOBAL";
    public static CountingIdlingResource countingIdlingResource = new CountingIdlingResource(RESOURCE);

    public EspressoIdlingResource() {
        countingIdlingResource = new CountingIdlingResource(RESOURCE);
    }

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }
}
