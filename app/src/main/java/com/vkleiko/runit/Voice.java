package com.vkleiko.runit;

/**
 * Created by Neava on 2016-11-16.
 */
public enum Voice {

    MALE,
    FEMALE;

    public static Voice toVoice (String enumString) {
        try {
            return valueOf(enumString);
        } catch (Exception ex) {
            // For error cases
            return MALE;
        }
    }

}
