package com.temnenkov.glvrd;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadFrag {
    private String text;
    private int start;
    private int end;

    boolean contains(int p) {
        return p > start && p < end;
    }
}
