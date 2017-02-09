package com.temnenkov.glvrd;

import lombok.Data;

import java.util.Collection;

@SuppressWarnings("squid:S1068")
@Data
public class ProofreadResponse {
    private boolean ok;
    private String score;
    private Collection<Fragment> fragments;
}
