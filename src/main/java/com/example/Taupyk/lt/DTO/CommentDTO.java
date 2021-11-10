package com.example.Taupyk.lt.DTO;

import java.time.LocalDate;
import java.util.Date;

public class CommentDTO {
    private String Text;

    private UserDto userDto;

    private LocalDate date;

    public CommentDTO(String text, UserDto userDto, LocalDate date) {
        Text = text;
        this.userDto = userDto;
        this.date = date;
    }

    public String getText() {
        return Text;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public LocalDate getDate() {
        return date;
    }
}
