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

    /**
     * 1. 필수 입력값이 없을 때 (2000~)
     * EMPTY_도메인_목적어
     */
    // 계정관련(2000~)
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    EMPTY_JWT_CLAIMS(false, 2002, "JWT claims string이 비었습니다."),

    EMPTY_PASSWORD(false, 2003, "비밀번호를 입력해주세요."),
    EMPTY_EMAIL(false, 2004, "이메일을 입력해주세요."),


    // User (2010~)
    EMPTY_USER_INDEX(false, 2010, "유저 인덱스 값을 입력해주세요."),
    EMPTY_USER_NAME(false, 2011, "유저 이름을 입력해주세요"),
    EMPTY_USER_PHONE_NUMBER(false, 2012, "유저 이름을 입력해주세요."),

    // Child (2020~)
    EMPTY_CHILD_INDEX(false, 2020, "아동 인덱스 값을 입력해주세요."),
    EMPTY_CHILD_NAME(false, 2021, "아동 이름을 입력해주세요."),
    EMPTY_CHILD_GENDER(false, 2022, "아동 성별을 입력해주세요."),
    EMPTY_CHILD_AGE(false, 2023, "아동 나이를 입력해주세요."),
    EMPTY_CHILD_ADDRESS(false, 2024, "아동 주소를 입력해주세요."),

    // Suspect (2030~)
    EMPTY_SUSPECT_INDEX(false,2030,"학대행위자 인덱스 값을 입력해주세요."),
    EMPTY_SUSPECT_GENDER(false,2031,"학대행위자 성별을 입력해주세요."),
    EMPTY_SUSPECT_AGE(false,2032,"학대행위자 나이를 입력해주세요."),
    EMPTY_SUSPECT_RELATION_WITH_CHILD(false,2033,"학대행위자 아동과의 관계를 입력해주세요."),

    // Abuse_situation (2040~)
    EMPTY_ABUSE_INDEX(false,2040,"학대정황기록의 인덱스 값을 입력해주세요."),
    EMPTY_ABUSE_DATE(false,2041,"목격 날짜를 입력해주세요."),
    EMPTY_ABUSE_TIME(false,2042,"목격 시간을 입력해주세요."),
    EMPTY_ABUSE_PLACE(false,2043,"목격 장소를 입력해주세요."),
    EMPTY_ABUSE_TYPE(false,2044,"학대 유형을 입력해주세요."),
    EMPTY_ABUSE_DESCRIPTION(false,2045,"구체적인 의심정황을 입력해주세요."),

    // file (2050~)
    EMPTY_FILE(false,2050,"파일을 첨부해주세요."),
    EMPTY_FILE_INDEX(false,2051,"파일 인덱스 값을 입력해주세요."),

    /**
     * 2. 유효한 형식이 아닐 때 (2100~)
     * INVALID_목적어
     */

    INVALID_JWT(false, 2100, "유효하지 않은 JWT입니다."),
    INVALID_PASSWORD(false, 2101, "비밀번호 형식을 확인해주세요."),
    INVALID_PHONE_NUMBER(false, 2102, "전화번호 형식을 확인해주세요."),
    INVALID_EMAIL(false, 2103, "이메일 형식을 확인해주세요."),
    INVALID_GENDER(false,2104,"유효하지 않은 성별값입니다."),
    INVALID_CONTENTS_COUNT(false, 2105, "내용의 글자수를 확인해주세요."),
    INVALID_JWT_SIGNATURE(false, 2106, "JWT 서명이 유효하지 않습니다."),
    INVALID_JWT_EXPIRED_TIME(false, 2107, "JWT 유효기간이 만료되었습니다."),
    INVALID_UNSUPPORTED_JWT(false, 2108, "지원하지 않는 JWT 형식입니다."),


    /**
     * 3. 이미 존재하는 리소스와 중복된 값일 때(2200~)
     * EXIST_목적어
     */
    EXISTS_EMAIL(false, 2200,"중복된 이메일입니다."),
    EXISTS_PHONE_NUMBER(false, 2201,"중복된 전화번호입니다."),


    /**
     * 4. 접근이 유효하지 않거나 : INVALID_ACCESS_목적어
     * 존재하지 않는 리소스에 접근시도할 때 : NOT_EXIST_목적어
     * (2300~)
     */
    INVALID_ACCESS_USER_JWT(false, 2300,"권한이 없는 유저의 접근입니다."),
    NOT_EXIST_USER(false, 2301, "유저가 존재하지 않습니다."),

    NOT_EXIST_CHILD(false,2310,"해당 아동이 존재하지 않습니다."),
    NOT_EXIST_SUSPECT(false,2311,"해당 학대행위자가 존재하지 않습니다."),

    NOT_EXIST_ABUSE_SITUATION(false,2320,"해당 기록이 존재하지 않습니다."),

    NOT_EXIST_PICTURE(false,2330,"해당 사진이 존재하지 않습니다."),
    NOT_EXIST_VIDEO(false,2331,"해당 영상이 존재하지 않습니다."),
    NOT_EXIST_RECORDING(false,2332,"해당 녹음파일이 존재하지 않습니다."),
    INVALID_ACCESS_PASSWORD(false, 2333,"잘못된 비밀번호입니다."),


    /**
     * 3000 : Response오류
     */
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    FAILED_TO_LOGIN(false, 3010,"없는 아이디거나 비밀번호가 틀렸습니다."),

    MODIFY_FAIL_POST(false, 3020, "게시물 수정에 실패하였습니다."),
    DELETE_FAIL_POST(false, 3021, "게시물 삭제에 실패하였습니다."),

    MODIFY_FAIL_SUSPECT(false,3030,"학대행위자 정보 삭제에 실패하였습니다."),

    UPLOAD_FAIL_IMAGE(false, 3040, "이미지 업로드에 실패하였습니다."),
    UPLOAD_FAIL_VIDEO(false, 3041, "동영상 업로드에 실패하였습니다."),
    UPLOAD_FAIL_RECORDING(false, 3042, "녹음파일 업로드에 실패하였습니다."),
    DELETE_FAIL_IMAGE(false, 3043, "이미지 삭제에 실패하였습니다."),
    DELETE_FAIL_VIDEO(false, 3044, "동영상 삭제에 실패하였습니다."),
    DELETE_FAIL_RECORDING(false, 3045, "녹음파일 삭제에 실패하였습니다."),
    DOWNLOAD_FAIL_IMAGE(false, 3046, "이미지 다운로드에 실패하였습니다."),
    DOWNLOAD_FAIL_VIDEO(false, 3047, "동영상 다운로드에 실패하였습니다."),
    DOWNLOAD_FAIL_RECORDING(false, 3048, "녹음파일 다운로드에 실패하였습니다."),



    /**
     * 4000 : Database, Server오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),


    PASSWORD_ENCRYPTION_ERROR(false, 4010, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4011, "비밀번호 복호화에 실패하였습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}