package com.temnenkov.glvrd;

import lombok.Data;

@SuppressWarnings("squid:S1068")
@Data
public class Fragment {
    private int start;
    private int end;
    private String url;
    private Hint hint;

}
