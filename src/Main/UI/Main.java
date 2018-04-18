package Main.UI;

import Main.objects.Item;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInterface.fxml"));

        BorderPane root = loader.load();
        controller = loader.getController();

        stage.getIcons().add(new Image("Main/UI/FileIcon.png"));

        stage.setTitle("Maintenance");
        stage.minWidthProperty().setValue(625);
        stage.minHeightProperty().setValue(450);
        stage.setScene(new Scene(root, 625, 500));
        stage.setOnCloseRequest(this :: onClose);
        controller.setItemList(readItemsFile());
        stage.show();
    }

    private ArrayList<Item> readItemsFile() {
        ArrayList<Item> items = new ArrayList<>();
        try (FileInputStream in = new FileInputStream("Items.xml")){
            XMLDecoder decoder = new XMLDecoder(in);
            items = (ArrayList<Item>) decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException e){
            System.out.println("Items.xml file not found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return items;
        }
    }

    private void onClose(WindowEvent windowEvent) {

        try(FileOutputStream out = new FileOutputStream("Items.xml")){
            XMLEncoder encoder = new XMLEncoder(out);
            encoder.writeObject(controller.getSerializableItemList());
            encoder.close();
        } catch(Exception e){
            System.out.println("e");
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}



