package com.mycompany.myapp.service.telegram.service;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineButtonUtil {

    public static InlineKeyboardButton button(String text, String callbackDate) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackDate).build();
    }

    public static List<InlineKeyboardButton> rows(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> rows = new ArrayList<>();
        rows.addAll(List.of(inlineKeyboardButtons));

        return rows;
    }

    public static List<List<InlineKeyboardButton>> collection(List<InlineKeyboardButton>... inlineKeyboardButtonsLists) {
        List<List<InlineKeyboardButton>> collection = new ArrayList<>();
        collection.addAll(List.of(inlineKeyboardButtonsLists));
        return collection;
    }

    public static InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> collection) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(collection);
        return keyboardMarkup;
    }
}
