package com.friendbook.userservice.utils;

import java.util.Random;

public class CodeGenerator {
    static String symbols = "0123456789";

    static public String generate(int length) {
        int n = symbols.length();
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(symbols.charAt(r.nextInt(n)));
        }
        return stringBuilder.toString();
    }
}
