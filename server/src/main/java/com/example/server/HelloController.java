package com.example.server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_msg;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vbox_msg;

    private ServerHighOrLow server;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.server = new ServerHighOrLow(new ServerSocket(1234));
        } catch (IOException e){
            System.out.println("Error creating server");
            e.printStackTrace();
        }
        vbox_msg.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double) t1);
            }
        });
        this.server.receiveMsgFromClient(vbox_msg);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String msgToSend = tf_msg.getText();
                if(!msgToSend.isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));

                    Text txt = new Text(msgToSend);
                    TextFlow textFlow = new TextFlow(txt);

                    textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                            "-fx-background-color: rgb(15,125,242);" +
                            " -fx-background-radius: 20px;");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    txt.setFill(Color.color(0.934, 0.945, 0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_msg.getChildren().add(hBox);

                    server.sendMsgToClient(msgToSend);
                    tf_msg.clear();
                }
            }
        });
    }
    public static void addLabel(String msgFromClient, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text txt = new Text(msgFromClient);
        TextFlow textFlow = new TextFlow(txt);

        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                " -fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
