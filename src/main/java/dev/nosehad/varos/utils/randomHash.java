package dev.nosehad.varos.utils;

import java.util.Random;

public class randomHash {
    public static String generate() {
        final char[] optionals = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'
                , 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
                , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        Random r = new Random();

        StringBuilder result = new StringBuilder ( );
        for (int b = 0; b < 50; b++) {
            result.append ( optionals[r.nextInt (optionals.length)] );
        }
        return String.valueOf ( result );
    }
}
