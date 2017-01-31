package com.temnenkov.glvrd;

import lombok.Data;

@Data
public class Fragment {
    private int start;
    private int end;
    private String url;
    private Hint hint;

}
