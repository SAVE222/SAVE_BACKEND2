package com.save_backend.config.exception;

import com.save_backend.config.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception{

    private BaseResponseStatus status;
}