package com.mainapp.passwordlayout;

import java.util.HashMap;

/**
 * Created by Andrej Russkikh on 14.11.2019.
 */
public class PasswordHelper {

    private HashMap<String,String> translateMap = new HashMap<>();


    public PasswordHelper(String[] russians, String[] latins){
        if(russians.length!=latins.length){
            throw new IllegalArgumentException();
        }
        for(int i=0;i<russians.length;i++)
            translateMap.put(russians[i],latins[i]);
    }


    public String convert(CharSequence source){
        StringBuilder result = new StringBuilder();

        for(int i=0;i<source.length();i++){
            char c = source.charAt(i);
            String value = translateMap.get(Character.toString(c));
                if(value!=null){
                    result.append(Character.isUpperCase(c)? value.toUpperCase():value);
                }
                else
                    result.append(c);
            }

        return result.toString();
    }
}
