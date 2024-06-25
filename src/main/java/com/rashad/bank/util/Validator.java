package com.rashad.bank.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(regexEmail);

    private static final String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,20}$";
    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile(regexPassword);

    private static final String regexUsername = "^[a-z][a-z0-9_]{3,29}$";
    private static final Pattern VALID_USERNAME_REGEX = Pattern.compile(regexUsername);

    public static boolean checkEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public static boolean checkPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    public static boolean checkUsername(String username) {
        Matcher matcher = VALID_USERNAME_REGEX.matcher(username);
        return matcher.matches();
    }
}



// Gmail special (username+something@gmail.com)
//  "^(?=.{1,64}@)[A-Za-z0-9_-+]+(\\.[A-Za-z0-9_-+]+)*@[^-][A-Za-z0-9-+]+(\\.[A-Za-z0-9-+]+)*(\\.[A-Za-z]{2,})$"
