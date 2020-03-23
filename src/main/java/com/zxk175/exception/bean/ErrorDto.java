package com.zxk175.exception.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019/03/23 19:15
 */
@Data
@Accessors(chain = true)
public class ErrorDto implements Serializable {

    private String field;

    private String message;

    private Object rejectedValue;

    @JsonIgnore
    private Integer index;
}
