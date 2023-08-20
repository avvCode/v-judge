package com.vv.sandbox.model;

import lombok.Data;

/**
 * @author vv
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;
}
