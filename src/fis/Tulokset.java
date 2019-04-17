package fis;

import java.util.ArrayList;
import java.util.List;

/**
 * Tulokset-luokka
 * Rajattomasti kasvava tietorakenneluokka joka osaa säilöä Tulos-olioita
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public class Tulokset extends TiedostoKasittely {
    private final String tiedostoNimi = "data/tulokset.csv";
    private final String tmpTiedostoNimi = "data/tmpTulokset.csv";
    private final String tiedostoOtsake = "id,fis_code,date,place,discipline,position,points";
    private final int TULOKSIA_INKREMENTTI = 5;
    private int lkm = 0;
    private Tulos[] tulokset = new Tulos[TULOKSIA_INKREMENTTI];
    
    
    /**
     * Palauttaa tulosten esittämiseen käytettävät otsakkeet
     * @return otsakkeet merkkijonotaulukossa
     */
    public String[] getOtsakkeet() {
        return Tulos.getKenttaOtsakkeet();
    }
    
    
    /**
     * @return Alkioiden määrä tietorakenteessa alussa ja paljollako kasvatetaan tarvittaessa kokoa
     */
    public int getTuloksiaInkrementti() {
        return TULOKSIA_INKREMENTTI;
    }
    
    
    /**
     * Palauttaa tuloksien määrän tietorakenteessa.
     * @return tuloksien määrä tietorakenteessa
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulokset tulokset = new Tulokset();
     * tulokset.getLkm() === 0;
     * tulokset.lisaa(new Tulos());
     * tulokset.getLkm() === 1;
     * tulokset.lisaa(new Tulos());
     * tulokset.getLkm() === 2;
     * </pre>
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Palauttaa tuloksien lukumäärän tietyllä fis koodilla ts. urheilijan tulosten määrä
     * @param fisCode fis koodi, uniikki
     * @return tulosten lukumäärä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulokset tulokset = new Tulokset();
     * tulokset.getLkm(1) === 0;
     * Tulos tulos = new Tulos();
     * tulos.setFiscode(1);
     * Tulos tulos2 = new Tulos();
     * tulos2.setFiscode(2);
     * tulokset.lisaa(tulos);
     * tulokset.lisaa(tulos2);
     * tulokset.getLkm(2) === 1;
     * </pre>
     */
    public int getLkm(int fisCode) {
        int loydetyt = 0;
        for (Tulos tulos : tulokset) {
            if (tulos != null && tulos.getFiscode() == fisCode) loydetyt++;
        }
        return loydetyt;
    }
    
    
    /*
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#getList()
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * int y = 5;
     * Tulokset tulokset = new Tulokset();
     * tulokset.getList().size() === 0;
     * for (int i = 0; i < y; i++) {
     *      tulokset.lisaa(new Tulos());
     * }
     * tulokset.getList().size() === y;
     * </pre>
     */
    @Override
    public List<Tulos> getList() {
        List<Tulos> loydetyt = new ArrayList<Tulos>();
        for (Tulos tulos : tulokset) {
            if (tulos != null) loydetyt.add(tulos);
        }
        return loydetyt;
    }
    
    
    /**
     * Palauttaa tulokset, joissa fis koodi esiintyy ts. urheilijan tulokset
     * @param fisCode fis koodi, uniikki
     * @return Lista tuloksista
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulokset tulokset = new Tulokset();
     * tulokset.getList(0).size() === 0;
     * Tulos tulos = new Tulos();
     * tulos.setFiscode(1);
     * Tulos tulos2 = new Tulos();
     * tulos2.setFiscode(2);
     * Tulos tulos3 = new Tulos();
     * tulos3.setFiscode(2);
     * tulokset.lisaa(tulos);
     * tulokset.lisaa(tulos2);
     * tulokset.lisaa(tulos3);
     * tulokset.getList(1).size() === 1;
     * tulokset.getList(2).size() === 2;
     * </pre>
     */
    public List<Tulos> getList(int fisCode) {
        List<Tulos> loydetyt = new ArrayList<Tulos>();
        for (Tulos tulos : tulokset) {
            if (tulos != null && tulos.getFiscode() == fisCode) loydetyt.add(tulos);
        }
        return loydetyt;
    }
    
   
    /**
     * Lisää uuden tuloksen tietorakenteeseen
     * @param tulos Tulos
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulokset tulokset = new Tulokset();
     * int max = tulokset.getTuloksiaInkrementti() + 7;
     * for (int i = 0; i <= max; i++) {
     *     Tulos tulos = new Tulos();
     *     tulos.taytaMalliDatalla();
     *     tulokset.lisaa(tulos);
     * }
     * tulokset.getLkm() > tulokset.getTuloksiaInkrementti() === true;
     * </pre>
     */
    @Override
    public void lisaa(Object tulos) {
        if (lkm >= tulokset.length) {
            Tulos[] tmpTulokset = new Tulos[tulokset.length + TULOKSIA_INKREMENTTI];
            for (int i = 0; i < tulokset.length; i++) {
                tmpTulokset[i] = tulokset[i];
            }
            tulokset = tmpTulokset;
        }
        tulokset[lkm] = (Tulos) tulos;
        lkm++;
        setMuutettu(true);
    }
    
    
    /**
     * Poistaa tietorakenteesta tulosta vastaavat esiintymät
     * @param tulos Tulos
     * @example
     * <pre name="test">
     * int y = 5;
     * Tulos tulos = new Tulos();
     * Tulokset tulokset = new Tulokset();
     * tulokset.getList().size() === 0;
     * tulokset.lisaa(tulos);
     * for (int i = 0; i < y; i++) {
     *      tulokset.lisaa(new Tulos());
     * }
     * tulokset.getLkm() === y+1;
     * tulokset.poista(tulos);
     * tulokset.getLkm() === y;
     * </pre>
     */
    @Override
    public void poista(Object tulos) {
        for (int i = 0; i < tulokset.length; i++) {
            if (tulokset[i] != null && tulos.equals(tulokset[i])) {
                tulokset[i] = null;
                lkm--;
            }
            if (i < tulokset.length - 1 && tulokset[i] == null && tulokset[i+1] != null) {
                tulokset[i] = tulokset[i+1];
                tulokset[i+1] = null;
            }
        }
        setMuutettu(true);
    }

    
    /**
     * Lukee tulokset tiedostosta tietorakenteeseen.
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import fis.Tulos;
     * String tiedosto = "testiTulokset.csv";
     * String otsake = "comma,separated,value";
     * Tulokset tulokset = new Tulokset();
     * VertaaTiedosto.kirjoitaTiedosto(tiedosto,
     *      otsake + "\n" +
     *      "1,120097,01.01.2019,Levi,GS,1,21.65\n" +
     *      "2,120097,02.01.2019,Pyhä,GS,14,28.11\n" +
     *      "3,123456,07.01.2019,Tahko,SL,26,39.12\n");
     * tulokset.getLkm() === 0;
     * tulokset.lueTiedostosta(tulokset, Tulos.class, tiedosto, otsake);
     * tulokset.getLkm() === 3;
     * tulokset.getLkm(123456) === 1;
     * VertaaTiedosto.tuhoaTiedosto(tiedosto);
     * </pre>
     */
    public void lueTiedostosta() {
        lueTiedostosta(this, Tulos.class, tiedostoNimi, tiedostoOtsake);
    }
    
    
    /**
     * Tallentaa tulokset tietorakenteesta tiedostoon
     * @throws SailoException Tallennus ei onnistunut
     * @example
     * <pre name="test">
     * #THROWS IOException, SailoException
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import fis.Tulos;
     * #import java.util.List;
     * String tiedosto = "testiTulokset.csv";
     * String tiedosto2 = "testiTuloksetTallennettu.csv";
     * String otsake = "comma,separated,value";
     * Tulokset tulokset = new Tulokset();
     * VertaaTiedosto.kirjoitaTiedosto(tiedosto,
     *      otsake + "\n" +
     *      "1,120097,01.01.2019,Levi,GS,1,21.65\n" +
     *      "2,120097,02.01.2019,Pyhä,GS,14,28.11\n" +
     *      "3,123456,07.01.2019,Tahko,SL,26,39.12\n");
     * tulokset.getLkm() === 0;
     * tulokset.lueTiedostosta(tulokset, Tulos.class, tiedosto, otsake);
     * tulokset.getLkm() === 3;
     * List<Tulos> lista = tulokset.getList(123456);
     * lista.size() === 1;
     * tulokset.poista(lista.get(0));
     * tulokset.getLkm() === 2;
     * tulokset.tallennaTiedostoon(tulokset, tiedosto2, "testiTuloksetTemp.csv", otsake);
     * String tulos =
     *      otsake + "\n" +
     *      "1,120097,01.01.2019,Levi,GS,1,21.65\n" +
     *      "2,120097,02.01.2019,Pyhä,GS,14,28.11\n";
     * VertaaTiedosto.vertaaFileString(tiedosto2, tulos) === null;
     * VertaaTiedosto.tuhoaTiedosto(tiedosto);
     * VertaaTiedosto.tuhoaTiedosto(tiedosto2);
     * </pre>
     */
    public void tallennaTiedostoon() throws SailoException {
        tallennaTiedostoon(this, tiedostoNimi, tmpTiedostoNimi, tiedostoOtsake);
    }
}
