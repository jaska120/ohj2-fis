package fxFis;

import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * ApuaController-kontrolleri
 * Hoitaa Apua ikkunan näytön ja dokumentin avaamisen selaimeen
 * @author jaakkomustalahti
 * @version 5.4.2019
 *
 */
public class ApuaController implements ModalControllerInterface<String> {
    
    
    /**
     * Avaa ohjelman dokumentin selaimeen
     */
    @FXML
    public void showDocument() {
        FisMain.hostServices.showDocument("https://www.mit.jyu.fi/demowww/ohj2/ht19/fis/trunk/");
    }
    
    
    @Override
    public String getResult() {
        return null;
    }

    @Override
    public void handleShown() {
        return;
    }

    @Override
    public void setDefault(String oletus) {
        return;
    }

}
