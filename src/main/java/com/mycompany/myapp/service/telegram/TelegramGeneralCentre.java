package com.mycompany.myapp.service.telegram;

import static com.mycompany.myapp.service.telegram.service.CallBackQuyeryService.*;

import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.service.dto.CodeMessage;
import com.mycompany.myapp.service.telegram.service.CallBackQuyeryService;
import com.mycompany.myapp.service.telegram.service.InlineButtonUtil;
import com.mycompany.myapp.service.telegram.service.MessageControl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramGeneralCentre extends TelegramLongPollingBot {

    private MessageControl messageControl;
    private CallBackQuyeryService callBackQuyeryService;
    private InlineButtonUtil inlineButtonUtil;

    public TelegramGeneralCentre(MessageControl messageControl) {
        this.messageControl = messageControl;
    }

    @Override
    public String getBotUsername() {
        return "MobileLegendsUzBot";
    }

    @Override
    public String getBotToken() {
        return "5926225652:AAFBk8u7uWsvSetwfb6BtwPH2Ea1pR4xr9A";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            User user = callbackQuery.getFrom();
            message = callbackQuery.getMessage();

            sendMsg(callBackQuyery(callbackQuery.getData(), String.valueOf(message.getChatId()), message.getMessageId()));
        } else if (message != null) {
            if (message.isCommand()) {
                sendMsg(messageControl.messageControl(message.getText(), message.getChatId(), message.getMessageId()));
            }
        }
    }

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(CodeMessage codeMessage) {
        try {
            switch (codeMessage.getType()) {
                case MESSAGE:
                    execute(codeMessage.getSendMessage());
                    break;
                case EDIT:
                    execute(codeMessage.getEditMessageText());
                    break;
                case MESSAGE_VIDEO:
                    execute(codeMessage.getSendMessage());
                    execute(codeMessage.getSendVideo());
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
