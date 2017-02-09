package com.temnenkov.tgibot.tgapi.dto.keyboard;

import java.io.Serializable;
import java.util.ArrayList;

public class Keyboard extends ArrayList<KeyboardRow> implements Serializable {
    public Keyboard addRow(KeyboardRow row){
        add(row);
        return this;
    }
}
