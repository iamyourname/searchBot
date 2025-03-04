package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Bot extends TelegramLongPollingBot {

    final static Logger logger = LoggerFactory.getLogger(Bot.class);

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
        //git

        /*
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

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
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
