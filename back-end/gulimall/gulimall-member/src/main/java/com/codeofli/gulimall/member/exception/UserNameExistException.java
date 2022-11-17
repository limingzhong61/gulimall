package com.codeofli.gulimall.member.exception;

public class UserNameExistException extends RuntimeException {
    public UserNameExistException() {
        super("该用户名已存在");
    }
}
