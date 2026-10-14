package com.example.fishlog.exception;

/**
 * 指定したIDのリソースが存在しない場合に投げる例外。
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
