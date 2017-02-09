package com.temnenkov.glvrd;

import lombok.Data;

@SuppressWarnings("squid:S1068")
@Data
public class Hint {
   private String penalty;
   private String name;
   private int weight;
   private String description;
}
