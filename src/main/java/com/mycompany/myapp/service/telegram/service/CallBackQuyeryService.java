package com.mycompany.myapp.service.telegram.service;

import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.domain.TelegramAccount_;
import com.mycompany.myapp.domain.enumeration.Types;
import com.mycompany.myapp.repository.TelegramAccountRepository;
import com.mycompany.myapp.service.dto.CodeMessage;
import com.mycompany.myapp.service.dto.CodeMessageType;
import com.mycompany.myapp.service.impl.TelegramAccountServiceImpl;
import com.mycompany.myapp.service.mapper.TelegramAccountMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class CallBackQuyeryService {

    private TelegramAccountServiceImpl telegramAccountService;

    private TelegramAccountRepository telegramAccountRepository;

    private TelegramAccountMapper telegramAccountMapper;

    public CallBackQuyeryService(
        TelegramAccountServiceImpl telegramAccountService,
        TelegramAccountRepository telegramAccountRepository,
        TelegramAccountMapper telegramAccountMapper
    ) {
        this.telegramAccountService = telegramAccountService;
        this.telegramAccountRepository = telegramAccountRepository;
        this.telegramAccountMapper = telegramAccountMapper;
    }

    public CodeMessage callBackQuyery(String text, String chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.startsWith("/create/")) {
            String commands = text.split("/")[2];
            switch (commands) {
                case "turnirs":
                    codeMessage = createTurnirs(text, chatId, messageId);
                    break;
                case "registry":
                    sendMessage.setText("hello registry");
                    sendMessage.setParseMode("Markdown");
                    codeMessage.setSendMessage(sendMessage);
                    codeMessage.setType(CodeMessageType.MESSAGE);
                    break;
                case "info":
                    sendMessage.setText("hello info");
                    sendMessage.setParseMode("Markdown");
                    codeMessage.setSendMessage(sendMessage);
                    codeMessage.setType(CodeMessageType.MESSAGE);
                    break;
            }
        }
        return codeMessage;
    }

    private CodeMessage createTurnirs(String text, String chatId, Integer messageId) {
        TelegramAccount telegramAccount = telegramAccountRepository.findByChatId(Long.valueOf(chatId)).orElse(null);

        CodeMessage codeMessage = new CodeMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);

        editMessageText.setParseMode("Markdown");
        if (telegramAccount == null) {
            telegramAccount = new TelegramAccount();
            telegramAccount.setChatId(Long.valueOf(chatId));
            telegramAccountRepository.save(telegramAccount);

            if (telegramAccount.getTypes().equals("FIRSTNAME")) {
                editMessageText.setText("Ismingizni kirting:");
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(CodeMessageType.EDIT);
                telegramAccount.setTypes(Types.LASTNAME);
                telegramAccount.setFirstname(text);
                telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            } else if (Objects.equals(telegramAccount.getTypes(), "LASTNAME")) {
                editMessageText.setText("Ismingizni kirting:");
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(CodeMessageType.EDIT);
                telegramAccount.setTypes(Types.PHONENUMBER);
                telegramAccount.setLastname(text);
                telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            } else if (Objects.equals(telegramAccount.getTypes(), "PHONENUMBER")) {
                editMessageText.setText("Ismingizni kirting:");
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(CodeMessageType.EDIT);
                telegramAccount.setTypes(Types.MOBILELEGENDID);
                telegramAccount.setMobileLegendId(Long.valueOf(text));
                telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            } else if (Objects.equals(telegramAccount.getTypes(), "MOBILELEGENDID")) {
                editMessageText.setText("Ismingizni kirting:");
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(CodeMessageType.EDIT);
                telegramAccount.setTypes(Types.NICHNAME);
                telegramAccount.setFirstname(text);
                telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            } else if (Objects.equals(telegramAccount.getTypes(), "NICHNAME")) {
                editMessageText.setText("Ismingizni kirting:");
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(CodeMessageType.EDIT);
                telegramAccount.setFirstname(text);
                telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            }
        }
        return codeMessage;
    }
}
