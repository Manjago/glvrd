package com.temnenkov.tgibot.tgapi.dto.keyboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyboardRow extends ArrayList<KeyboardButton> implements Serializable {
    public KeyboardRow addButton(KeyboardButton button){
        add(button);
        return this;
    }

    public KeyboardRow addButtons(List<KeyboardButton> buttons){
        addAll(buttons);
        return this;
    }
}
