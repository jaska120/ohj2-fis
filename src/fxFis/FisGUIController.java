package fxFis;

import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import fis.Fis;
import fis.SailoException;
import fis.Tulos;
import fis.Urheilija;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jaakkomustalahti
 * @version 12.1.2019
 *
 */
public class FisGUIController implements Initializable {
    @FXML private StringGrid<Tulos> gridTulokset;
    @FXML private ListView<Urheilija> listUrheilijat;
    @FXML private TextField textSearch;
    @FXML private TextArea areaUrheilijaTiedot;
    @FXML private ImageView imageUrheilija;
    @FXML private ProgressIndicator imageProgress;
    @FXML private Button buttonLisaaTulos, buttonPoistaTulos, buttonLisaaSeurantaan;
        
    
    /**
     * @param url ei kaytossa
     * @param bundle ei kaytossa
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            alusta();
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    @FXML private void handleShowTracking() {
        ModalController.<Fis>showModal(
                SeurantaController.class.getResource("SeurantaView.fxml"), 
                "Seuranta", null, this.fis);
    }
    
    
    @FXML private void handleShowHelp() {
        ModalController.showModal(ApuaController.class.getResource("ApuaView.fxml"), "Apua", null, "");
    }
    
    
    @FXML private void handleAddTracking() {
        Urheilija urheilija = listUrheilijat.getSelectionModel().getSelectedItem();
        if (urheilija != null) {
            boolean seurannassa = fis.vaihdaSeuranta(urheilija);
            Dialogs.showMessageDialog(seurannassa ? "Urheilija lisätty seurantaan." : "Urheilija poistettu seurannasta.");
            naytaUrheilijaSeurannassa(urheilija);
        }
    }
    
    
    @FXML private void handleAddResult() {
        Tulos tulos = ModalController.showModal(TulosDialogController.class.getResource("TulosDialogView.fxml"), "Tulos", null, null);
        if (tulos != null) lisaaTulos(tulos);
    }
    
    
    @FXML private void handleDeleteResult() {
        boolean vastaus = Dialogs.showQuestionDialog("Tuloksen poistaminen", "Haluatko varmasti poistaa tuloksen?", "Poista", "Peruuta");
        if (vastaus) {
            poistaTulos(gridTulokset.getObject());
        }
    }
    
    
  //===========================================================================================   
 // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia 
    
    private Fis fis;

    
    /**
     * Alustaa näytön piirtämällä siihen urheilijalistan
     * @throws SailoException Poikkeus alustuksessa
     */
    protected void alusta() throws SailoException {
        buttonLisaaTulos.setDisable(true);
        buttonPoistaTulos.setDisable(true);
        buttonLisaaSeurantaan.setDisable(true);
        imageProgress.setVisible(false);
        
        this.fis = new Fis();
        fis.paivitaUrheilijaKanta();
        fis.lueTiedostoista();
        
        
        List<Urheilija> urheilijat;
        if ((urheilijat = fis.getUrheilijat()) != null) {
            ObservableList<Urheilija> observableUrheilijat = FXCollections.observableArrayList();
            observableUrheilijat.addAll(urheilijat);
            FilteredList<Urheilija> filteredUrheilijat = new FilteredList<Urheilija>(observableUrheilijat, u -> true);
            
            textSearch.textProperty().addListener((observable, vanha, uusi) -> { // asetetaan kuuntelija urheilijoiden suodattamiseksi
                filteredUrheilijat.setPredicate(urheilija -> {
                    if (uusi == null || uusi.isEmpty()) {
                        return true;
                    }
                    
                    String uusiSuodin = uusi.toLowerCase();
                    
                    if (urheilija.toString().toLowerCase().contains(uusiSuodin)) {
                        return true; // Hakuarvo vastaa nimeä
                    } else if (Integer.toString(urheilija.getFiscode()).contains(uusiSuodin)) {
                        return true; // Hakuarvo vastaa fis koodia
                    }
                    return false;
                });
            });
            listUrheilijat.setItems(filteredUrheilijat);
            listUrheilijat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Urheilija>() {

                @Override
                public void changed(ObservableValue<? extends Urheilija> observable,
                        Urheilija vanha, Urheilija uusi) {
                    if (uusi != null) naytaUrheilija(uusi);
                }
            });
        } else {
            throw new SailoException("Tietokannasta löytyi 0 urheilijaa!");
        }
        
        gridTulokset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gridTulokset.initTable(fis.getTulosOtsakkeet());
        gridTulokset.setOnCellString( (g, tulos, defValue, r, c) -> tulos.getKenttaOtsakkeetArvo(c) );
        gridTulokset.setOnMouseClicked(e -> {
            Tulos tulos;
            if ((tulos = gridTulokset.getObject()) != null) {
                if (e.getClickCount() == 2)
                    muokkaaTulos(tulos);
                buttonPoistaTulos.setDisable(false);
            }
        });
    }
    
    
    private void tallenna() {
        try {
            fis.tallenna();
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    /**
     * @return Voiko sulkea eli onko tallennettu
     */
    public boolean voikoSulkea() {
        if (fis.onkoMuutettu()) {
            boolean tallennetaanko = Dialogs.showQuestionDialog("Tallennus", "Tallennetaanko tekemäsi muutokset?", "Kyllä", "Ei");
            if (tallennetaanko) tallenna();
        }
        return true;
    }


    private void naytaUrheilija(Urheilija urheilija) {
        buttonLisaaTulos.setDisable(false);
        buttonLisaaSeurantaan.setDisable(false);
        naytaUrheilijaTiedot(urheilija);
        naytaUrheilijaTulokset(urheilija);
        naytaUrheilijaSeurannassa(urheilija);
    }


    private void naytaUrheilijaTiedot(Urheilija urheilija) {
        areaUrheilijaTiedot.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaUrheilijaTiedot)) {
            urheilija.tulosta(os);
        }
        naytaUrheilijaKuva(urheilija);
    }
    
    
    private void naytaUrheilijaKuva(Urheilija urheilija) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                imageUrheilija.setImage(null); // poistetaan kuva
                imageProgress.setVisible(true); // indikaattori näkyviin
                Image image = new Image("https://data.fis-ski.com/general/load-competitor-picture/" + urheilija.getCompetitorid() + ".html");
                imageUrheilija.setImage(image);
                imageProgress.setVisible(false); // indikaattori pois
                return null;
            }
        };
        new Thread(task).start();
    }


    private void naytaUrheilijaTulokset(Urheilija urheilija) {
        buttonPoistaTulos.setDisable(true);
        gridTulokset.clear();
        List<Tulos> tulokset = fis.getTulokset(urheilija);
        for (Tulos tulos : tulokset) {
            gridTulokset.add(tulos);
        }
    }
    
    
    private void naytaUrheilijaSeurannassa(Urheilija urheilija) {
        buttonLisaaSeurantaan.setText(fis.seurannassa(urheilija) ? "Poista seurannasta" : "Lisää seurantaan");
    }
    
    
    private void muokkaaTulos(Tulos tulos) {
        Tulos muokattuTulos = ModalController.<Tulos, TulosDialogController>showModal(
                TulosDialogController.class.getResource("TulosDialogView.fxml"), 
                "Tulos", null, tulos, ctrl -> ctrl.setButtonText("Muokkaa"));
        if (muokattuTulos != null)
            naytaUrheilijaTulokset(fis.getUrheilija(muokattuTulos));
    }
    
    
    private void lisaaTulos(Tulos tulos) {
        Urheilija urheilija;
        if ((urheilija = listUrheilijat.getSelectionModel().getSelectedItem()) != null) {
            fis.lisaa(urheilija, tulos);
            naytaUrheilijaTulokset(urheilija);
        }
    }
    
    
    private void poistaTulos(Tulos tulos) {
        fis.poista(tulos);
        naytaUrheilijaTulokset(fis.getUrheilija(tulos));
    }
}