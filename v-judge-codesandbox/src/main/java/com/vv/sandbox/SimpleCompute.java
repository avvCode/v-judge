package com.vv.sandbox;


import org.glassfish.jersey.message.internal.StringBuilderUtils;

import java.util.Arrays;

public class SimpleCompute {

    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println("结果:" + (a + b));
    }
}
