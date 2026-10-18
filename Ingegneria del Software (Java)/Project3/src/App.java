import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.*; 
import java.io.*;

public class App extends Application {
    public static String clientnam = "";
    public static int countsys = 0;
    public static wine oggwine = new wine();
    public static users oggUsers = new users();
    public static employers oggempl = new employers();
    public static int countnewus = 2;
    public static int resultquantity = 0;
    public static int skiporder = 0;
    public static String requester = "";
    public static String winenamerequested = "";
    public static String[][] orderarray = new String[20][20]; // 20 orders maximum wine in depot
    public static String[] clientnameorder = new String[20];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        doonce();
        oggwine.fillwine();
        oggUsers.fillusers();
        oggempl.fillnewempl();
        ///////// Primary stage
        primaryStage.getIcons().add(new Image("wineicon.png"));
        primaryStage.setTitle("Program Search & Buy italian wine");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // gap between columns
        grid.setVgap(15); // gap between rows
        grid.setPadding(new Insets(25, 25, 25, 25)); // set padding

        Scene scene = new Scene(grid, 330, 330); // new scene
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Welcome to Log/Sign Panel");
        scenetitle.setFont(Font.font("Apple", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 3, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);// colonna,riga

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label passlabel = new Label("Password:");
        grid.add(passlabel, 0, 2);

        PasswordField passbox = new PasswordField();
        grid.add(passbox, 1, 2);

        Button btnsign = new Button("Sign up");
        HBox hbsing = new HBox(10);
        hbsing.setAlignment(Pos.BOTTOM_RIGHT);
        hbsing.getChildren().add(btnsign);
        grid.add(hbsing, 1, 4);

        Button btnlog = new Button("Log in");
        HBox hblog = new HBox(10);
        hblog.setAlignment(Pos.BOTTOM_LEFT);
        hblog.getChildren().add(btnlog);
        grid.add(hblog, 0, 4);

        Button btnsearch = new Button("Find Wine");
        HBox hbsearch = new HBox(10);
        hbsearch.setAlignment(Pos.BOTTOM_LEFT);
        hbsearch.getChildren().add(btnsearch);
        btnsearch.setDisable(true);
        grid.add(hbsearch, 0, 5);

        Button btnempllog = new Button("Employer Login");
        HBox hbempl = new HBox(10);
        hbempl.setAlignment(Pos.BOTTOM_RIGHT);
        hbempl.getChildren().add(btnempllog);
        btnempllog.setDisable(true);
        grid.add(hbempl, 1, 5);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        primaryStage.show();

        ///////// Sign stage
        Stage signformstage = new Stage();
        signformstage.setTitle("Sign Up Panel");

        GridPane gridformsign = new GridPane();
        gridformsign.setAlignment(Pos.TOP_CENTER);
        gridformsign.setHgap(10); // gap between columns
        gridformsign.setVgap(15); // gap between rows
        gridformsign.setPadding(new Insets(20, 20, 20, 20)); // set padding

        Scene secondSceneLogin = new Scene(gridformsign, 330, 300);

        signformstage.setScene(secondSceneLogin);

        signformstage.setX(primaryStage.getX() + 0);
        signformstage.setY(primaryStage.getY() + 0);

        Text titlesign = new Text("Compile the sign up form");
        titlesign.setFont(Font.font("Apple", FontWeight.NORMAL, 20));
        gridformsign.add(titlesign, 0, 0, 3, 1); // Y X riga,colonna

        Label usname = new Label("Name:");
        gridformsign.add(usname, 0, 1);// colonna,riga

        TextField txtusname = new TextField();
        gridformsign.add(txtusname, 1, 1);

        Label ussurname = new Label("Surname:");
        gridformsign.add(ussurname, 0, 2);// colonna,riga

        TextField txtsurname = new TextField();
        gridformsign.add(txtsurname, 1, 2);

        Label usemail = new Label("Email:");
        gridformsign.add(usemail, 0, 3);// colonna,riga

        TextField txtemail = new TextField();
        gridformsign.add(txtemail, 1, 3);

        Label uspass = new Label("Password:");
        gridformsign.add(uspass, 0, 4);// colonna,riga

        PasswordField txtpasssign = new PasswordField();
        gridformsign.add(txtpasssign, 1, 4);

        Button btnsend = new Button("Send info");
        HBox hbsend = new HBox(10);
        hbsend.setAlignment(Pos.BOTTOM_CENTER);
        hbsend.getChildren().add(btnsend);
        gridformsign.add(hbsend, 0, 5);

        ///////// Search panel
        Stage searchstage = new Stage();
        searchstage.setTitle("Search Panel");

        GridPane gridsearch = new GridPane();
        gridsearch.setAlignment(Pos.TOP_CENTER);
        gridsearch.setHgap(10); // gap between columns
        gridsearch.setVgap(15); // gap between rows
        gridsearch.setPadding(new Insets(20, 20, 20, 20)); // set padding

        Scene thirdscene = new Scene(gridsearch, 370, 450);

        searchstage.setScene(thirdscene);

        searchstage.setX(signformstage.getX() + 0);
        searchstage.setY(signformstage.getY() + 0);

        Text titlesearch = new Text("Search your wine");
        titlesearch.setFont(Font.font("Apple", FontWeight.NORMAL, 20));
        gridsearch.add(titlesearch, 0, 0, 3, 1); // Y X riga,colonna

        Label winenam = new Label("Name:");
        gridsearch.add(winenam, 0, 1);// colonna,riga

        TextField txtwinenam = new TextField();
        gridsearch.add(txtwinenam, 1, 1);

        Label yearwin = new Label("Year:");
        gridsearch.add(yearwin, 0, 2);// colonna,riga

        TextField txtyearwin = new TextField();
        gridsearch.add(txtyearwin, 1, 2);

        Label quantity = new Label("Quantity: ");
        gridsearch.add(quantity, 0, 3);// colonna,riga

        TextField txtquantity = new TextField();
        gridsearch.add(txtquantity, 1, 3);

        TextArea area = new TextArea();
        area.setPrefColumnCount(15);
        area.setPrefHeight(120);
        area.setPrefWidth(210);
        gridsearch.add(area, 1, 5);
        area.setDisable(true);
        area.setWrapText(true);

        Label displayinfo = new Label("Display");
        gridsearch.add(displayinfo, 0, 5);

        Label choise = new Label("Buy?");
        gridsearch.add(choise, 0, 6);

        Button yesbtn = new Button("Yes");
        HBox hbyes = new HBox(10);
        hbyes.setAlignment(Pos.CENTER_LEFT);
        hbyes.getChildren().add(yesbtn);
        gridsearch.add(hbyes, 0, 7);
        yesbtn.setDisable(true);

        Button nobtn = new Button("No");
        HBox hbno = new HBox(10);
        hbno.setAlignment(Pos.CENTER_LEFT);
        hbno.getChildren().add(nobtn);
        gridsearch.add(hbno, 1, 7);
        nobtn.setDisable(true);

        Button btnfind = new Button("Find wine");
        HBox hbfind = new HBox(10);
        hbfind.setAlignment(Pos.CENTER);
        hbfind.getChildren().add(btnfind);
        gridsearch.add(hbfind, 1, 4);

        Button btnquit = new Button("Quit");
        HBox hbquit = new HBox(10);
        hbquit.setAlignment(Pos.CENTER_LEFT);
        hbquit.getChildren().add(btnquit);
        gridsearch.add(hbquit, 0, 8);

        ///////// employer panel
        Stage employerstage = new Stage();
        employerstage.setTitle("Employer Panel");

        GridPane gridemplo = new GridPane();
        gridemplo.setAlignment(Pos.TOP_CENTER);
        gridemplo.setHgap(10); // gap between columns
        gridemplo.setVgap(15); // gap between rows
        gridemplo.setPadding(new Insets(20, 20, 20, 20)); // set padding

        Scene emploscene = new Scene(gridemplo, 600, 600);

        employerstage.setScene(emploscene);

        employerstage.setX(primaryStage.getX() + 0);
        employerstage.setY(primaryStage.getY() + 0);

        Text empltitle = new Text("Welcome Employer!");
        empltitle.setFont(Font.font("Apple", FontWeight.NORMAL, 20));
        gridemplo.add(empltitle, 0, 0, 3, 1); // Y X riga,colonna

        Label lbldisplay = new Label("Requested wine display");
        gridemplo.add(lbldisplay, 0, 1);

        TextArea notifydisplay = new TextArea();
        notifydisplay.setPrefColumnCount(15);
        notifydisplay.setPrefHeight(200);
        notifydisplay.setPrefWidth(200);
        gridemplo.add(notifydisplay, 0, 2);
        notifydisplay.setDisable(true);
        notifydisplay.setWrapText(true);

        Label lblwinepos = new Label("Wine position in display list");
        gridemplo.add(lblwinepos, 0, 3);

        TextField txtwineposlist = new TextField();
        gridemplo.add(txtwineposlist, 1, 3);

        Label qntlbl = new Label("Quantity");
        gridemplo.add(qntlbl, 0, 4);

        TextField txtquantitywineempl = new TextField();
        gridemplo.add(txtquantitywineempl, 1, 4);

        Button btnfill = new Button("Fill wine");
        HBox hbfill = new HBox(10);
        hbfill.setAlignment(Pos.CENTER);
        hbfill.getChildren().add(btnfill);
        gridemplo.add(hbfill, 0, 5);

        final Text actiontempl = new Text();
        gridemplo.add(actiontempl, 1, 6);

        Button btnclose = new Button("Quit & Close");
        HBox hbclose = new HBox(10);
        hbclose.setAlignment(Pos.CENTER);
        hbclose.getChildren().add(btnclose);
        gridemplo.add(hbclose, 0, 6);

        Label placedorder = new Label("Maked orders");
        gridemplo.add(placedorder, 0, 7);

        TextArea orderdisplay = new TextArea();
        orderdisplay.setPrefColumnCount(15);
        orderdisplay.setPrefHeight(200);
        orderdisplay.setPrefWidth(200);
        gridemplo.add(orderdisplay, 0, 8);
        orderdisplay.setWrapText(true);

        Button sendtoserver = new Button("Send order to server");
        HBox hbsendtoserver = new HBox(10);
        hbsendtoserver.setAlignment(Pos.CENTER);
        hbsendtoserver.getChildren().add(sendtoserver);
        gridemplo.add(hbsendtoserver, 1, 8);

        /////////// button event handler section
        btnsign.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.hide();
                signformstage.show();
                btnsign.setDisable(true);
                btnlog.setDisable(true);
                actiontarget.setText("");

                oggUsers.existingdb4login[0][showcountnewus()] = txtemail.getText();

                oggUsers.db4refresh[0] = txtusname.getText();

                oggUsers.db4refresh[1] = txtsurname.getText();

                oggUsers.existingdb4login[1][showcountnewus()] = txtpasssign.getText();
                oggUsers.refresnewuser(showcountnewus());

                userTextField.setText(txtemail.getText());
                passbox.setText(txtpasssign.getText());
                actiontarget.setText("");
                incrcountnewus();
            }
        }); // sign up event

        btnsend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                signformstage.hide();

                primaryStage.show();
                btnsearch.setDisable(false);
            }
        });// send event

        btnlog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("");
                boolean exist = false, existpass = false;

                for (int i = 0; i < 4; i++) {
                    if (oggUsers.existingdb4login[0][i].equals(userTextField.getText())) {
                        exist = true;
                    }
                    if (exist) {
                        if (oggUsers.existingdb4login[1][i].equals(passbox.getText())) {
                            existpass = true;
                        }
                    }
                }

                if (exist && existpass) {
                    btnsign.setDisable(true);
                    btnlog.setDisable(true);
                    btnsearch.setDisable(false);
                    actiontarget.setText("");
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Wrong email or password");
                }
            }
        });// login event

        btnsearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { // search in primary stage wine
                primaryStage.hide();
                searchstage.show();
            }
        });

        btnfind.setOnAction(new EventHandler<ActionEvent>() {// findwine in search stage
            @Override
            public void handle(ActionEvent e) {
                String winename = "", year = "";
                int result = 10;
                winename = txtwinenam.getText();
                year = txtyearwin.getText();

                try { //
                    result = oggwine.searchwine(winename, year);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                if (result == 0) {
                    area.setText("Wine not available");
                } else if (result == 50) { // quantita = 50 finiti
                    btnfind.setDisable(true);
                    yesbtn.setDisable(false);
                    nobtn.setDisable(false);

                    String[] view = oggwine.sendtodisplay();
                    area.setText("");
                    area.appendText("Country: " + view[0]);
                    area.appendText("\nTypo: " + view[1]);
                    area.appendText("\nTechnical notes: " + view[2]);
                    area.appendText("\nQuantity: 0");
                    saveresult(0);
                    savewinename(txtwinenam.getText());

                } else { // quantita = n
                    btnfind.setDisable(true);
                    yesbtn.setDisable(false);
                    nobtn.setDisable(false);

                    String[] viewq = oggwine.sendtodisplay();
                    area.setText("");
                    area.appendText("Country: " + viewq[0]);
                    area.appendText("\nTypo: " + viewq[1]);
                    area.appendText("\nTechnical notes: " + viewq[2]);
                    area.appendText("\nQuantity: " + result);
                    saveresult(result);
                }
            }
        });

        nobtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                yesbtn.setDisable(true);
                nobtn.setDisable(true);
                btnfind.setDisable(false);
            }
        });

        yesbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                yesbtn.setDisable(true);
                nobtn.setDisable(true);

                if (resultquantity == 0) {
                    area.setText("");
                    area.appendText("Wine not avaialable,sending notification to employer...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    if (!userTextField.getText().equals("")) {
                        oggempl.addnotifytodb(userTextField.getText(), winenamerequested);
                    } else if (!txtemail.getText().equals("")) {
                        oggempl.addnotifytodb(txtemail.getText(), winenamerequested);
                    }

                } else {
                    boolean resultvend = false;
                    resultvend = oggwine.buywine(Integer.parseInt(txtquantity.getText()), resultquantity);
                    if (resultvend) {
                        area.setText("Wine bought successfully!");

                        if (!userTextField.getText().equals("")) {
                            saveclientnamefororder(userTextField.getText());
                        } else {
                            saveclientnamefororder(txtemail.getText());
                        }

                        addorder(clientnam, txtwinenam.getText(), txtquantity.getText());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        area.setText("");
                        area.appendText("Your Quantity exeed maximum of depot, try again");
                    } // Maggiorequantity
                }
                btnfind.setDisable(false);
            }
        });

        btnempllog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = 0; i < 20; i++) {
                    if (!clientnameorder[i].equals("")) {
                        orderdisplay.appendText(
                                "" + clientnameorder[i] + " " + orderarray[i][0] + " " + orderarray[i][1] + "\n");
                    }
                }

                if (oggempl.showemplemail().equals(userTextField.getText())) {
                    if (oggempl.showemplpass().equals(passbox.getText())) {

                        employerstage.show();
                        primaryStage.hide();

                        String[][] showdbpublic = oggempl.shownotifydb();
                        if (showdbpublic[0][0].equals("")) {
                            actiontempl.setText("No request found,have a nice day!");
                            notifydisplay.setText("");
                            btnfill.setDisable(true);
                        }
                        notifydisplay.setText("");

                        for (int i = 0; i < 5; i++) {
                            if (!showdbpublic[i][0].equals("")) {
                                notifydisplay.appendText(
                                        "Email:" + showdbpublic[i][0] + " Wine Requested:" + showdbpublic[i][1] + "\n");
                            } else {
                                break;
                            }
                        }
                    } else {
                        actiontarget.setText("Wrong email or password");
                    }
                } else {
                    actiontarget.setText("Wrong email or password");
                }

            }
        }); // employer access button

        btnfill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int savepos = 0;
                String[][] arrforfill = oggempl.shownotifydb();

                if (!arrforfill[0][0].equals("")) {
                    int txtposinlistint = Integer.parseInt(txtwineposlist.getText());
                    txtposinlistint--;
                    for (int z = 0; z < 5; z++) {
                        if (arrforfill[txtposinlistint][1].equals(oggwine.dbwine[z][0])) {
                            savepos = z;
                        }
                    }
                    oggwine.dbnumberwine[savepos] = Integer.parseInt(txtquantitywineempl.getText());
                    actiontempl.setText("Wine filled succesfully");

                    arrforfill[txtposinlistint][1] = "";
                    arrforfill[txtposinlistint][0] = "";

                    notifydisplay.setText("");
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 2; j++) {
                            notifydisplay.appendText(arrforfill[i][j]);
                        }
                    }

                } else {
                    actiontempl.setText("No request found,have a nice day!");
                    btnfill.setDisable(true);
                }
            }
        });

        btnquit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                area.setText("");
                txtwinenam.setText("");
                txtyearwin.setText("");
                txtquantity.setText("");
                txtusname.setText("");
                txtpasssign.setText("");
                txtsurname.setText("");
                txtemail.setText("");

                actiontarget.setText("Logged out Successfully");

                searchstage.hide();
                primaryStage.show();

                incrcount();
                if (incrcount() > 2) {
                    btnfind.setDisable(true);
                    btnsign.setDisable(true);
                    btnlog.setDisable(true);
                    btnempllog.setDisable(false);
                } else {
                    btnsign.setDisable(false);
                    btnlog.setDisable(false);
                }
                btnsearch.setDisable(true);
                btnfind.setDisable(false);
            }
        });

        btnclose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });

        sendtoserver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            try{
                Socket socket = new Socket ("127.0.0.1", 7777); // Create and connect the socket
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                String sts="";
                
                dOut.writeByte(1);
                for(int d=0;d<20;d++){
                    sts+="Wine name: "+orderarray[d][0]+" Quantity: "+orderarray[d][1]+" Ordered by: "+clientnameorder[d]+"\n";
                }
                dOut.writeUTF(sts);

                dOut.writeByte(-1);
                dOut.flush();
    
                dOut.close();
                socket.close();

                actiontempl.setText("Data successfully sent to Server");
            }catch(Exception e1){//exceptionhandler
                actiontempl.setText("An error during server connection occured");
            }

        }});

}//start
    public static void doonce(){
        for(int f=0;f<20;f++){
            clientnameorder[f]="";}}

    public static int incrcount(){
    countsys++;
    return countsys;}

    public static void incrcountnewus(){
        countnewus++;}

    public static int showcountnewus(){
         return countnewus;}

    public static void saveresult(int result){
        resultquantity=result;}

     public static void savewinename(String wn){
         winenamerequested= wn;}

     public static void addorder(String cno,String winename,String orderedquantity){
        clientnameorder[skiporder]=cno;
        orderarray[skiporder][0]= winename;
        orderarray[skiporder][1]= orderedquantity;
        skiporder++; }

     public static void saveclientnamefororder(String cn){
        clientnam=cn;}

}//class app