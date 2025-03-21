package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.Connections.searchCity;

public class Bot extends TelegramLongPollingBot {
    private final Map<Long, String> oneWordFromChat = new ConcurrentHashMap<>();
    final static Logger logger = LoggerFactory.getLogger(Bot.class);
    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;


    @Override
    public String getBotUsername() {
        return "MoiseeVPN";
    }
    @Override
    public String getBotToken() {
        return "8025936217:AAFOqA2ywo4AqpU9sRLxMjJrQILWpzbCLo4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        /*
        var callback = update.getCallbackQuery();
        var id = callback.getId();
        */
        /*
        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();
        var back = InlineKeyboardButton.builder()
                .text("Back").callbackData("back")
                .build();

        var url = InlineKeyboardButton.builder()
                .text("Tutorial")
                .url("https://core.telegram.org/bots/api")
                .build();

        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();

//Buttons are wrapped in lists since each keyboard is a set of button rows
        keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();
                */
       // sendText(id,msg.getText());

        if(msg.isCommand()){
            if(msg.getText().equals("/start")){
                sendText(id,"ВВЕДИТЕ ГОРОД");
            }
        }else {
            if(msg.getText().equals("МОСКВА")){

                if(searchCity("МОСКВА")) {
                    sendText(id,"ГОРОД НАЙДЕН");
                    sendText(id,"ВВЕДИ УЛИЦУ");
                }else {

                }
            }
        }

        //git
        //System.out.println(user.getUserName()+"("+id+")"+": "+msg.getText());
        //sendText(id,"hello");
        /*
         sendMenu(id, "<b>Menu 1</b>", keyboardM1);
        if(msg.isCommand()){
            if(msg.getText().equals("/status")){ // обработка status
                try {
                    sendText(id, execCommand("top -b -n 1 > top1.txt && head -5 top1.txt","admin"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(msg.getText().equals("/reboot")){ //обработка help
                try {
                    execCommand("reboot", "root");
                    sendText(id, "Successfully rebooted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(msg.getText().equals("/uptime")){ //обработка help
                try {
                    sendText(id,execCommand("uptime -p", "admin"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(msg.getText().equals("/get")){ //обработка get
                try {
                    sendConfig(id);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            sendText(id, msg.getText());
        }
        */




    }


    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();

        if(data.equals("next")) {
            newTxt.setText("MENU 2");
            newKb.setReplyMarkup(keyboardM2);
        } else if(data.equals("back")) {
            newTxt.setText("MENU 1");
            newKb.setReplyMarkup(keyboardM1);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }
    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    private void undefinedKeyboard(long chatId, String word) {
        // 5 шаг. Кладём слово для конкретного гостя в ранее созданную коллекцию
        oneWordFromChat.put(chatId, word); // word = "Москва"

        // Делаем двухслойный торт из кнопок
        Map<String, String> floor1 = new HashMap<>();
        Map<String, String> floor2 = new HashMap<>();

        // Понятно озвучиваем ингридиенты для себя и гостей
        floor1.put("ADD_KEYWORD_FROM_CHAT", "Сохранить для автопоиска");
        floor2.put("SEARCH_BY_WORD", "Искать новости");

        // код maker на GitHub
        sendMessage(chatId, "Что с этим сделать? " + "\uD83E\uDDD0",
                InlineKeyboards.maker(floor1, floor2));
    }
    */

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what)
                .build();    //Message content


        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
