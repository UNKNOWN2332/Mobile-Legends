package com.mycompany.myapp.service.telegram.service;

import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.domain.enumeration.Types;
import com.mycompany.myapp.repository.TelegramAccountRepository;
import com.mycompany.myapp.service.dto.CodeMessage;
import com.mycompany.myapp.service.dto.CodeMessageType;
import com.mycompany.myapp.service.impl.TelegramAccountServiceImpl;
import com.mycompany.myapp.service.mapper.TelegramAccountMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
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
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setParseMode("Markdown");
        editMessageText.setMessageId(messageId);
        TelegramAccount telegramAccount = telegramAccountRepository.findByChatId(Long.valueOf(chatId)).orElse(null);
        if (text.startsWith("/create/")) {
            String commands = text.split("/")[2];

            switch (commands) {
                case "turnirs":
                    if (telegramAccount == null) {
                        telegramAccount = new TelegramAccount();
                        telegramAccount.setChatId(Long.valueOf(chatId));
                        telegramAccountRepository.save(telegramAccount);
                    }
                    if (!telegramAccount.getTypes().equals(Types.END)) {

                        editMessageText.setText("Ismingizni kirting:");
                        codeMessage.setEditMessageText(editMessageText);
                        codeMessage.setType(CodeMessageType.EDIT);
                    }
                    else if (telegramAccount.getTypes().equals(Types.END)) {
                        sendMessage.setText( "Turnir ochish bot hizmatidasiz turnir ochish pullik\n\n" +
                                "Turnir bir martalik ochish 20 000 ming so'm\n" +
                                "Turnir bir kunlik vip ochish 50 000 ming so'm\n" +
                                "Turnir bir haftalik ovip ochish 100 000 ming so'm\n" +
                                "Turnir bir oylik ovip ochish 100 000 ming so'm\n\n" +
                                "Ushbu tugmalardan birini tanlang va pul o'tkazmasini @Shukrullaev_47 ga murojat qiling\n" +
                            "Tolov toliq amalga oshirilgandan song admin tomondan tasdiqlovchi sorov yuboriladi\nAgar tolov tolaqonli amalga" +
                            "amalga oshirilsa siz berilgan tarif bo'ycha turnirlar ochishingiz mumkin bo'ladi");

                        sendMessage.setReplyMarkup(
                            InlineButtonUtil.keyboard(
                                InlineButtonUtil.collection(
                                    InlineButtonUtil.rows(InlineButtonUtil.button(
                                            "1 marta", "create/turnirs/birmarta"),
                                        InlineButtonUtil.button("kunlik", "/create/turnirs/kun")),
                                    InlineButtonUtil.rows(InlineButtonUtil.button(
                                            "hafta", "create/turnirs/hafta"),
                                        InlineButtonUtil.button("oy", "/create/turnirs/oy"))
                                )
                            )
                        );

                        codeMessage.setSendMessage(sendMessage);
                        codeMessage.setType(CodeMessageType.MESSAGE);
                    }
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
        if (text.startsWith("/turnirs/")) {
            String commands = text.split("/")[2];
            switch (commands) {
                case "no":
                    EditMessageText editMessageTexts = new EditMessageText();
                    editMessageTexts.setChatId(chatId);
                    editMessageTexts.setText("Ismingizni kirting:");
                    editMessageTexts.setParseMode("Markdown");
                    editMessageTexts.setMessageId(messageId);
                    codeMessage.setEditMessageText(editMessageTexts);
                    codeMessage.setType(CodeMessageType.EDIT);
                    telegramAccount.setTypes(Types.FIRSTNAME);
                    telegramAccountRepository.save(telegramAccount);
                    break;
            }
        }

        return codeMessage;
    }

    public CodeMessage createTurnirs(String text, String chatId, Integer messageId, TelegramAccount telegramAccount) {
        CodeMessage codeMessage = new CodeMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("Markdown");
        if (telegramAccount.getTypes().equals(Types.FIRSTNAME)) {
            telegramAccount.setTypes(Types.LASTNAME);
            telegramAccount.setFirstname(text);
            sendMessage.setText("Familyangizni kirting:");
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.LASTNAME)) {
            telegramAccount.setTypes(Types.PHONENUMBER);
            telegramAccount.setLastname(text);
            sendMessage.setText("Nomeringizni kirting:");
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.PHONENUMBER)) {
            sendMessage.setText("Mobile Legends id kirting:");
            telegramAccount.setTypes(Types.MOBILELEGENDID);
            telegramAccount.setPhoneNumber(text);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.MOBILELEGENDID)) {
            sendMessage.setText("Nick nam kirting:");
            telegramAccount.setTypes(Types.NICHNAME);
            telegramAccount.setMobileLegendId(Long.valueOf(text));
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
        } else if (Objects.equals(telegramAccount.getTypes(), Types.NICHNAME)) {
            telegramAccount.setNickName(text);
            telegramAccount.setTypes(Types.END);
            telegramAccountService.save(telegramAccountMapper.toDto(telegramAccount));
            sendMessage.setText(
                "Isim: " + telegramAccount.getFirstname() + "\n" +
                    "Familiya: " + telegramAccount.getLastname() + "\n" +
                    "Tel nomer: " + telegramAccount.getPhoneNumber() + "\n" +
                    "MobileLegends id: " + telegramAccount.getMobileLegendId() + "\n" +
                    "MobileLegends nick name: " + telegramAccount.getNickName() + "\n" +
                    "Shular barchasi to'grimi?"
            );
            sendMessage.setReplyMarkup(
                InlineButtonUtil.keyboard(
                    InlineButtonUtil.collection(
                        InlineButtonUtil.rows(InlineButtonUtil.button("\uD83D\uDC4D Ha", "create/turnirs"),
                            InlineButtonUtil.button("\uD83D\uDEAB Yoq", "/turnirs/no"))
                    )
                )
            );

        }
        codeMessage.setSendMessage(sendMessage);
        codeMessage.setType(CodeMessageType.MESSAGE);
        return codeMessage;
    }
}
