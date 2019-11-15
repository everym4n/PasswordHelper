package com.mainapp.passwordlayout;

/**
 * Created by Andrej Russkikh on 14.11.2019.
 */
public class PasswordHelper {

    private final String[] russians;
    private final String[] latins;


    public PasswordHelper(String[] russians, String[] latins){
        if(russians.length!=latins.length){
            throw new IllegalArgumentException();
        }
        this.russians = russians;
        this.latins = latins;
    }


    public String convert(CharSequence source){
        StringBuilder result = new StringBuilder();

        for(int i=0;i<source.length();i++){
            char c = source.charAt(i);
            String key = String.valueOf(Character.toLowerCase(c));
            for(int dict=0;dict<russians.length;dict++){
                if(key.equals(russians[dict])){
                    result.append(Character.isUpperCase(c)? latins[dict].toUpperCase():latins[dict]);
                }
            }
            if(result.length()<= i)
                result.append(c);


        }
        return result.toString();
    }
}
