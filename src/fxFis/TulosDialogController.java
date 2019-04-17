package fxFis;


import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fis.SailoException;
import fis.Tulos;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import kanta.Tarkastaja;

/**
 * @author jaakkomustalahti
 * @version 4.3.2019
 * Controller-luokka tulos dialogin hallintaan
 */
public class TulosDialogController implements ModalControllerInterface<Tulos> {
    @FXML private GridPane gridTulos;
    @FXML private Button buttonOK;
    @FXML private List<TextField> kentat = new ArrayList<TextField>();
    @FXML private List<Label> ohjeet = new ArrayList<Label>();
    private Tulos tulos;
    

    @FXML
    private void initialize() {
        String[] otsakkeet = Tulos.getKenttaOtsakkeet();
        for (int i = 0; i < otsakkeet.length; i++) {
            Label otsake = new Label(otsakkeet[i]);
            String ohje = Tulos.getKenttaRegex()[i][1];
            
            Label tarkastus = new Label(ohje);
            tarkastus.setStyle("visibility: hidden");
            ohjeet.add(tarkastus);

            TextField kentta = new TextField();
            kentat.add(kentta);
            kentta.setOnKeyReleased(e -> validoi());
            gridTulos.add(otsake, 0, i);
            gridTulos.add(kentta, 1, i);
            gridTulos.add(tarkastus, 2, i);
        }
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(1/3);
        gridTulos.getColumnConstraints().addAll(column);
        gridTulos.setVgap(10);
        gridTulos.setHgap(10);
    }
    

    @FXML
    private void handleOK() {
        if (this.tulos == null) {
            this.tulos = new Tulos();
            this.tulos.setId();
        }
        int i = 0;
        for (Node node : gridTulos.getChildren()) {
            if ( node != null && node instanceof TextField ) {
                try {
                    this.tulos.set(Tulos.getKenttaArvot()[i], ((TextField) node).getText());
                } catch (SailoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                i++;
            }
        }
        ModalController.closeStage(gridTulos);
    }
    
    
    @FXML
    private void handleCancel() {
        ModalController.closeStage(gridTulos);
    }


    @Override
    public Tulos getResult() {
        return tulos;
    }

    @Override
    public void handleShown() {
        for (Node element : gridTulos.getChildren()) {
            if (element instanceof TextField) {
                element.requestFocus();
                break;
            }
        }
    }

    
    @Override
    public void setDefault(Tulos tulos) {
        this.tulos = tulos;
        if (this.tulos != null) {
            int i = 0;
            for (Node element : gridTulos.getChildren()) {
                if (element instanceof TextField) {
                    ((TextField) element).textProperty().setValue(this.tulos.getKenttaOtsakkeetArvo(i));
                    i++;
                }
            }
        }
        validoi();
    }
    
    
    /**
     * Asettaa OK painikkeeseen tekstin
     * @param text teksti
     */
    public void setButtonText(String text) {
        this.buttonOK.textProperty().setValue(text);
    }
    
    
    /**
     * @return onko tulos validi eli voidaanko tallentaa
     */
    public boolean validoi() {
        boolean validi = true;
        for (int i = 0; i < kentat.size(); i++) {
            if (!Tarkastaja.tarkista(kentat.get(i).getText(), Tulos.getKenttaRegex()[i][0])) {
                ohjeet.get(i).setStyle("visibility: visible");
                kentat.get(i).setStyle("-fx-control-inner-background: red;");
                validi = false;
            } else {
                ohjeet.get(i).setStyle("visibility: hidden");
                kentat.get(i).setStyle("-fx-control-inner-background: white;");
            }
        }
        if (validi)
            buttonOK.setDisable(false);
        else
            buttonOK.setDisable(true); 
        return validi;
    }
}
