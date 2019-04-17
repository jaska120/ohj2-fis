package fxFis;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author jaakkomustalahti
 * @version 12.1.2019
 *
 */
public class FisMain extends Application {
    
    /**
     * HostServices -palvelut
     * Saatavilla staattisesti, jotta controllerit voivat avata esim. selainikkunan.
     */
    public static HostServices hostServices;
    @Override
    public void start(Stage primaryStage) {
        try {
            FisMain.hostServices = getHostServices();
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("FisGUIView.fxml"));
            final Pane root = ldr.load();
            final FisGUIController fisCtrl = (FisGUIController) ldr.getController();
            
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("fis.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Fis");
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !fisCtrl.voikoSulkea() ) event.consume();
            });
            
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}