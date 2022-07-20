package com.save_backend.src.utils;

import java.util.regex.Pattern;

public class ValidationRegex {

    public static Boolean isValidPhone(String phone){
        String pattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        return Pattern.matches(pattern, phone);
    }

    public static Boolean isValidEmail(String email){
        String pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        return Pattern.matches(pattern, email);
    }

    public static Boolean isValidPassword(String password){
        String pattern = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&+=]).*$"; // 숫자, 문자, 특수문자 포함 8~20자리 이내
        return Pattern.matches(pattern, password);
    }
}
