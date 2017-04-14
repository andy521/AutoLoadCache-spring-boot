package com.jarvis.app.to;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserTO implements Serializable {

    private static final long serialVersionUID=-2510806368101425770L;

    private Integer id;

    private String name;

    private Integer age;

}
