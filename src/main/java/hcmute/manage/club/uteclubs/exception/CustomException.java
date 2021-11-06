package hcmute.manage.club.uteclubs.exception;

import lombok.Data;

@Data
public class CustomException {
    private int status;
    private String error;
    private String message;
}
