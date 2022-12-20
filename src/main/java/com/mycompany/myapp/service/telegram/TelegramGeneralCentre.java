package com.mycompany.myapp.service.telegram;

import static com.mycompany.myapp.service.telegram.service.CallBackQuyeryService.*;

import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.repository.TelegramAccountRepository;
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
    private TelegramAccountRepository telegramAccountRepository;

    public TelegramGeneralCentre(
        MessageControl messageControl,
        CallBackQuyeryService callBackQuyeryService,
        TelegramAccountRepository telegramAccountRepository
    ) {
        this.messageControl = messageControl;
        this.callBackQuyeryService = callBackQuyeryService;
        this.telegramAccountRepository = telegramAccountRepository;
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

            sendMsg(callBackQuyeryService.callBackQuyery(data, String.valueOf(message.getChatId()), message.getMessageId()));
        } else if (message != null) {
            var telegramAccount = telegramAccountRepository.findByChatId(message.getChatId());
            String text = message.getText();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            Integer messageId = message.getMessageId();

            if (text != null) {
                if (text.equals("/start") || text.equals("/about") || text.equals("/turnir") || text.equals("/help")) {
                    sendMsg(messageControl.messageControl(message.getText(), message.getChatId(), message.getMessageId()));
                } else if (telegramAccount.isPresent()) {
                    sendMsg(
                        callBackQuyeryService.createTurnirs(
                            text,
                            String.valueOf(message.getChatId()),
                            message.getMessageId(),
                            telegramAccount.get()
                        )
                    );
                } else {
                    sendMessage.setText(
                        "Uzur bu kamandani bilmiman.\n\n Iltimos qaytadan murojat qiling /start orqali yoki @Shukrullaev_47 ga murojat qiling"
                    );
                    sendMsg(sendMessage);
                }
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
