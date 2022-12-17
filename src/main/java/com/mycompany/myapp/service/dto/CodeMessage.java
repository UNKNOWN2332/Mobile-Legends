package com.mycompany.myapp.service.dto;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Data
public class CodeMessage {

    public CodeMessageType type;
    private SendMessage sendMessage;
    private EditMessageText editMessageText;
    private SendVideo sendVideo;
}
