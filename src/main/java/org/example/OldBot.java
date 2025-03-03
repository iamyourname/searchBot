package org.example;


import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.common.SSHRuntimeException;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import net.schmizz.sshj.SSHClient;
//
import java.io.File;
import java.io.IOException;

import java.io.IOException;
import java.net.ConnectException;


public class OldBot extends TelegramLongPollingBot {

    public static SSHClient ssh=null;
    public static  Session session;
    final static Logger logger = LoggerFactory.getLogger(Bot.class);

    @Override
    public String getBotUsername() {
        return "MoiseeVPN";
    }
    @Override
    public String getBotToken() {
        return "7884641664:AAHe0CNzozfsYAhR9IIuPQX0XL9rGNKMGXk";
    }
    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        //git
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

            //System.out.println(user.getFirstName() + "(" + id + "): SEND COMMAND " + msg.getText());
            // System.out.println(user.getFirstName() + "(" + id + "): HELP");
            // sendText(id,"command");
        }else {
            sendText(id, msg.getText());
        }



    }

    public void sendConfig(Long who) throws TelegramApiException {

        // File file = new File("/Users/m.moiseev/IdeaProjects/vpnBot/vpnclient.mobileconfig"); //local
        File file = new File("/home/admin/vpnclient.mobileconfig"); //remote
        SendDocument sd = SendDocument.builder().chatId(who.toString()).document(new InputFile(file)).build();
        execute(sd);
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

    public static void connectToServer(String user){
        try {
            logger.info("Connecting to server...");
            ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect("46.243.3.47");
            ssh.authPassword(user, "xid123mt");
            session = ssh.startSession();
            logger.info("SSH IS: " + ssh.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String execCommand(String command, String user) throws IOException {

        try{
            connectToServer(user);
        }catch (SSHRuntimeException e){
            e.printStackTrace();
            return "Server is down";
        }


        // System.out.println("SSH is: "+ ssh.isConnected());
        Session.Command cmd = session.exec(command);

        String response = IOUtils.readFully(cmd.getInputStream()).toString();

        logger.info(response);
        cmd.join();

        session.close();
        ssh.disconnect();

        return response;
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