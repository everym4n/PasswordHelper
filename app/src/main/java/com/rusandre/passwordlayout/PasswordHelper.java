package com.rusandre.passwordlayout;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Andrej Russkikh on 14.11.2019.
 */
public class PasswordHelper {



    private final HashMap<String, String> translateMap = new HashMap<>();

    //Наборы символов для генерации передаются чтобы можно было через ресурсы их изменить
    private final String upperCaseCharacters;
    private final String lowerCaseCharacters;
    private final String numbers;
    private final String specialCharacters;

    public PasswordHelper(String[] russians,
                          String[] latins,
                          String upperCaseCharacters,
                          String lowerCaseCharacters,
                          String numbers,
                          String specialCharacters) {
        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < russians.length; i++)
            translateMap.put(russians[i], latins[i]);


        this.upperCaseCharacters = upperCaseCharacters;
        this.lowerCaseCharacters = lowerCaseCharacters;
        this.numbers = numbers;
        this.specialCharacters = specialCharacters;
    }


    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String value = translateMap.get(Character.toString(c));
            if(value == null)
                value = translateMap.get(Character.toString(c).toLowerCase());
            if (value != null) {
                result.append(Character.isUpperCase(c) ? value.toUpperCase() : value);
            } else
                result.append(c);
        }

        return result.toString();
    }

    //Оценка качества пароля, здесь может быть более сложная логика но пока просто по длинне
    public int getQuality(CharSequence s) {
        return Math.min(s.length(), 10);
    }

    //Генерация пароля
    public String generatePassword(boolean useUpperCase, boolean useNumbers, boolean useSpecialCharacters, int length) {
        StringBuilder password = new StringBuilder();
        StringBuilder chars = new StringBuilder(lowerCaseCharacters);
        if (useNumbers)
            chars.append(numbers);
        if (useSpecialCharacters)
            chars.append(specialCharacters);
        if (useUpperCase)
            chars.append(upperCaseCharacters);
        Random random = new Random();
        String passwordCharacters = chars.toString();
        for (int i = 0; i < length; i++)
            password.append(passwordCharacters.charAt(random.nextInt(passwordCharacters.length())));

        return password.toString();
    }

}
