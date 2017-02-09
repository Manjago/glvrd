package com.temnenkov.tgibot.tgbot;

import com.temnenkov.tgibot.tgapi.dto.Update;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdatePackSplitter {
    public Collection<Message<Update>> split(UpdatePack updatePack){

        if (updatePack == null || updatePack.isEmpty()){
            return new ArrayList<>();
        }

        List<Message<Update>> result = new ArrayList<>();

        for(Update u : updatePack.getUpdates()){
            result.add(MessageBuilder.withPayload(u).build());
        }

        return result;
    }
}
