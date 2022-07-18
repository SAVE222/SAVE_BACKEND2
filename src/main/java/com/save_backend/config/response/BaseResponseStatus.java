package com.save_backend.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 :요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request오류
     */
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),

    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003,"권한이 없는 유저의 접근입니다."),
    USERS_EMPTY_USER_ID(false, 2004, "유저 아이디 값을 확인해주세요."),

    USERS_EMPTY_PASSWORD(false, 2010, "비밀번호를 입력해주세요."),
    USERS_INVALID_PASSWORD(false, 2011, "비밀번호 형식을 확인해주세요."),

    USERS_EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    USERS_INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
    USERS_EXISTS_EMAIL(false, 2022,"중복된 이메일입니다."),
    USERS_EXISTS_PHONE_NUMBER(false, 2023,"중복된 전화번호입니다."),

    POSTS_INVALID_CONTENTS(false, 2030, "내용의 글자수를 확인해주세요."),
    EMPTY_POST_ID(false, 2031, "게시물 아이디 값을 확인해주세요."),

    // 필수 정보 누락
    MISSING_ESSENTIAL_CHILD_INDEX(false,2050,"누락된 해당 아동의 번호는 필수값입니다."),
    MISSING_ESSENTIAL_GENDER(false,2051,"누락된 성별에 대한 정보는 필수값입니다."),
    MISSING_ESSENTIAL_AGE(false,2052,"누락된 나이 정보가 필수값입니다."),
    MISSING_ESSENTIAL_RELATION_WITH_CHILD(false,2053,"누락된 아동과의 관계 정보는 필수값입니다."),

    // 존재하지 않는 정보
    NOT_EXIST_CHILD(false,2060,"존재하지 않는 아동에 대한 접근입니다."),


    /**
     * 3000 : Response오류
     */
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    FAILED_TO_LOGIN(false, 3010,"없는 아이디거나 비밀번호가 틀렸습니다."),

    MODIFY_FAIL_POST(false, 3020, "게시물 수정에 실패하였습니다."),
    DELETE_FAIL_POST(false, 3021, "게시물 삭제에 실패하였습니다."),



    /**
     * 4000 : Database, Server오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),


    PASSWORD_ENCRYPTION_ERROR(false, 4010, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4011, "비밀번호 복호화에 실패하였습니다."),

    /**
     * 6000 : child 관련 오류
     */
    POST_CHILD_EMPTY_USERIDX(false, 6000, "유저 정보를 확인해주세요."),
    POST_CHILD_EMPTY_NAME(false, 6001, "아동 이름을 입력해주세요."),
    POST_CHILD_EMPTY_GENDER(false, 6002, "아동 성별을 입력해주세요."),
    POST_CHILD_EMPTY_AGE(false, 6003, "아동 나이를 입력해주세요."),
    POST_CHILD_EMPTY_ADDRESS(false, 6004, "아동 주소를 입력해주세요."),
    POST_CHILD_EMPTY_DETAIL_ADDRESS(false, 6005, "아동 상세주소를 입력해주세요."),
    NOT_EXIST_USER(false, 6006, "유저가 존재하지 않습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}