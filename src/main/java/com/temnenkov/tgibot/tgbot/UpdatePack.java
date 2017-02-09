package com.temnenkov.tgibot.tgbot;

import com.temnenkov.tgibot.tgapi.dto.Update;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePack implements Serializable {
    private static final long serialVersionUID = 4260795502416364302L;
    private final Update[] updates;
    private final long lastReceivedUpdate;

    public boolean isEmpty() {
        return updates == null || (updates.length == 0);
    }
}
