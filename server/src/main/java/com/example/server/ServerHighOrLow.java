package com.example.server;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHighOrLow {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public ServerHighOrLow(ServerSocket serverSocket){

        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating server");
            e.printStackTrace();
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    public void sendMsgToClient(String msgToClient){
        try{
            bufferedWriter.write(msgToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            System.out.println("Error sending message to the client");
            e.printStackTrace();
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void receiveMsgFromClient(VBox vBox){
        new Thread(new Runnable() {
            public void run() {
                while (socket.isConnected()){
                    try {
                        String msgFromClient = bufferedReader.readLine();
                        HelloController.addLabel(msgFromClient, vBox);
                    } catch (IOException e){
                        System.out.println("Error receiving message from the client");
                        e.printStackTrace();
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }

                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(socket != null){
                socket.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error creating server");
            e.printStackTrace();
        }

    }
}
