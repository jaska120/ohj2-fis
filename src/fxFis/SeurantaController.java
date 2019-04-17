package fxFis;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.StringGrid;
import fis.Fis;
import fis.Tulos;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * @author jaakkomustalahti
 * @version 11.2.2019
 * Controller -luokka Seuranta dialogin hallintaan
 */
public class SeurantaController implements ModalControllerInterface<Fis> {
    @FXML private StringGrid<Tulos> gridTulos;
    private Fis fis;
    
    
    @FXML
    private void initialize() {
        gridTulos.setOnCellString( (g, tulos, defValue, r, c) -> 
            c == 0 ? fis.getUrheilija(tulos).toString() : tulos.getKenttaOtsakkeetArvo(c-1)
        );
    }
    
    
    @FXML
    private void handleRefresh() {
        Dialogs.showMessageDialog("Ei vielä implementoitu.");
    }
    
    
    @FXML
    private void handleRemoveTracking() {
        Tulos tulos;
        if ((tulos = gridTulos.getObject()) != null) {
            fis.vaihdaSeuranta(tulos);
            Dialogs.showMessageDialog("Urheilija poistettu seurannasta.");
            lisaaTulokset();
        } else {
            Dialogs.showMessageDialog("Valitse ensin urheilija listalta, jonka haluat poistaa.");
        }
    }
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        gridTulos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        String[] tmpOtsakkeet = fis.getTulosOtsakkeet();
        String[] otsakkeet = new String[tmpOtsakkeet.length + 1];
        otsakkeet[0] = "Urheilija";
        for (int i = 0; i < tmpOtsakkeet.length; i++) {
            otsakkeet[i+1] = tmpOtsakkeet[i];
        }
        gridTulos.initTable(otsakkeet);
        lisaaTulokset();
    }


    @Override
    public Fis getResult() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void setDefault(Fis oletus) {
        this.fis = oletus;
    }
    
    
    private void lisaaTulokset() {
        gridTulos.clear();
        for (Tulos tulos : fis.getSeurantaTulokset()) {
            gridTulos.add(tulos);
        }
    }
}
