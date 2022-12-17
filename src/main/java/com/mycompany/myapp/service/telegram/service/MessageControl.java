package com.mycompany.myapp.service.telegram.service;

import com.mycompany.myapp.service.telegram.service.InlineButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageControl {

    public SendMessage messageControl(String text, Long chatId, Integer messageId) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(String.valueOf(chatId));
        if (text.equals("/start")) {
            sendMessage.setText(
                " Assalomu Alaykum Turnir botga hush kelibsiz. \n\n" +
                "Turnir tashkilashtrmoqchi bo'sangiz Turnir Tashkilashttirish tugmasini bosing.\n\n" +
                "Registratsya qilmoqchi bo'sangiz Registratsya qilish tugmasini bosing\n\n" +
                "Qaysi turnirlarda borligizni bilmoqchi bo'sangiz Qaysi turnirda borman tugmasini bosing."
            );
            sendMessage.setParseMode("Markdown");

            sendMessage.setReplyMarkup(
                InlineButtonUtil.keyboard(
                    InlineButtonUtil.collection(
                        InlineButtonUtil.rows(InlineButtonUtil.button("\uD83C\uDFC6Turnir tashkilashtirish", "/create/turnirs")),
                        InlineButtonUtil.rows(InlineButtonUtil.button("\uD83C\uDFAERegistratsya qilish", "/create/registry")),
                        InlineButtonUtil.rows(InlineButtonUtil.button("\uD83D\uDDD2Qaysi turnirlarda borman", "/create/info"))
                    )
                )
            );
        }
        return sendMessage;
    }
}
