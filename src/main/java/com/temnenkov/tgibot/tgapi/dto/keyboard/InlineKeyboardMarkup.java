package com.temnenkov.tgibot.tgapi.dto.keyboard;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InlineKeyboardMarkup implements ReplyMarkup, Serializable {
    /**
     * Array of button rows, each represented by an Array of InlineKeyboardButton objects
     */
    @SerializedName("inline_keyboard")
    private List<InlineKeyboardRow> inlineKeyboard;

}
