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
            TelegramAccount telegramAccount = telegramAccountRepository.findByChatId(Long.valueOf(chatId)).orElse(null);

            switch (commands) {
                case "turnirs":
                    if (telegramAccount == null) {
                        telegramAccount = new TelegramAccount();
                        telegramAccount.setChatId(Long.valueOf(chatId));
                        telegramAccountRepository.save(telegramAccount);
                    }
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(chatId);
                    editMessageText.setText("Ismingizni kirting:");
                    editMessageText.setParseMode("Markdown");
                    editMessageText.setMessageId(messageId);
                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(CodeMessageType.EDIT);
                    break;
                case "registry":
                    if (telegramAccount == null) {
                        telegramAccount = new TelegramAccount();
                        telegramAccount.setChatId(Long.valueOf(chatId));
                        telegramAccountRepository.save(telegramAccount);
                    }
                    sendMessage.setText("hello registry");
                    sendMessage.setParseMode("Markdown");
                    codeMessage.setSendMessage(sendMessage);
                    break;
                case "info":
                    sendMessage.setText("hello info");
                    sendMessage.setParseMode("Markdown");
                    codeMessage.setSendMessage(sendMessage);
                    break;
            }
        }

        return codeMessage;
    }

    public CodeMessage createTurnirs(String text, String chatId, Integer messageId, TelegramAccount telegramAccount) {
        CodeMessage codeMessage = new CodeMessage();
        EditMessageText editMessageText = new EditMessageText();

        editMessageText.setMessageId(messageId);
        if (telegramAccount.getTypes().equals(Types.FIRSTNAME)) {
            telegramAccount.setTypes(Types.LASTNAME);
            telegramAccount.setFirstname(text);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.LASTNAME)) {
            editMessageText.setText("familyangizni kirting:");
            telegramAccount.setTypes(Types.PHONENUMBER);
            telegramAccount.setLastname(text);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.PHONENUMBER)) {
            editMessageText.setText("Nomeringizni kirting:");
            telegramAccount.setTypes(Types.MOBILELEGENDID);
            telegramAccount.setMobileLegendId(Long.valueOf(text));
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.MOBILELEGENDID)) {
            editMessageText.setText("Mobile Legends id kirting:");
            telegramAccount.setTypes(Types.NICHNAME);
            telegramAccount.setFirstname(text);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.NICHNAME)) {
            editMessageText.setText("Nick nam kirting:");
            telegramAccount.setFirstname(text);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        }
        codeMessage.setEditMessageText(editMessageText);
        codeMessage.setType(CodeMessageType.EDIT);
        return codeMessage;
    }
}
