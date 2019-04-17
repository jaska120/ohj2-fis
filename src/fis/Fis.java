package fis;

import java.util.ArrayList;
import java.util.List;

/**
 * Fis-luokka hoitaa Urheilijat-, Tulokset- ja Seuranta-luokkien yhteistyön.
 * Luokka välittää yllälueteltujen luokkien tietorakenteita.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public class Fis {
    private final Urheilijat urheilijat = new Urheilijat();
    private final Tulokset tulokset = new Tulokset();
    private final Seuranta seuranta = new Seuranta();
    
    
    /**
     * Palauttaa urheilijoiden lukumäärän
     * @return urheilijoiden lukumäärä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.getUrheilijoita() === 0;
     * fis.lueUrheilijatTiedostosta();
     * fis.getUrheilijoita() > 0 === true;
     * </pre>
     */
    public int getUrheilijoita() {
        return urheilijat.getLkm();
    }
    
    
    /**
     * Hae urheilijat listaan
     * @return urheilijat
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.getUrheilijat().size() === 0;
     * fis.lueUrheilijatTiedostosta();
     * fis.getUrheilijat().size() > 0 === true;
     * </pre>
     */
    public List<Urheilija> getUrheilijat() {
        return urheilijat.getList();
    }
    
    
    /**
     * @param tulos Tulos
     * @return Urheilija, jolle tulos kuuluu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.List;
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * List<Tulos> seuranta = fis.getSeurantaTulokset();
     * seuranta.get(0) == null === false;
     * fis.getUrheilija(seuranta.get(0)) == null === false;
     * </pre>
     */
    public Urheilija getUrheilija(Tulos tulos) {
        return urheilijat.get(tulos.getFiscode());
    }
    
    
    /**
     * Palauttaa listan urheilijan tuloksista
     * @param urheilija urheilija
     * @return lista tuloksista
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * Urheilija urheilija = new Urheilija();
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * Urheilija urheilija2 = new Urheilija();
     * String rivi2 = "278,13th FIS points list 2018/2019,1,AL,O,219656,123456,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija2.parse(rivi2);
     * int max = 3;
     * for (int i = 0; i < max; i++) {
     *    Tulos tulos = new Tulos();
     *    tulos.taytaMalliDatalla();
     *    fis.lisaa(urheilija, tulos); 
     * }
     * fis.lisaa(urheilija2, new Tulos()); // lisätään 4. tulos
     * fis.getTulokset(urheilija).size() === max;
     * </pre>
     */
    public List<Tulos> getTulokset(Urheilija urheilija) {
        return tulokset.getList(urheilija.getFiscode());
    }
    
    
    /**
     * @return Kaikki seurannassa olevien urheilijoiden tulokset
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.getSeurantaTulokset().size() === 0;
     * fis.lueTiedostoista();
     * fis.getSeurantaTulokset().size() == 0 === false;
     * </pre>
     */
    public List<Tulos> getSeurantaTulokset() {
        List<Tulos> tmpTulokset = new ArrayList<Tulos>();
        for (Integer integer : seuranta.getList()) {
            tmpTulokset.addAll(tulokset.getList(integer.intValue()));
        }
        return tmpTulokset;
    }


    /**
     * Palauttaa tulosten esittämisessä käytetyt otsakkeet
     * @return otsakkeet merkkijonotaulukossa
     * @example
     * <pre name="test">
     * Fis fis = new Fis();
     * fis.getTulosOtsakkeet() == null === false;
     * </pre>
     */
    public String[] getTulosOtsakkeet() {
        return tulokset.getOtsakkeet();
    }
    
    
    /**
     * Lisaa urheilijalle tuloksen tietorakenteeseen
     * @param urheilija Urheilija
     * @param tulos Tulos
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.List;
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * List<Urheilija> urheilijat = fis.getUrheilijat();
     * urheilijat.size() == 0 === false;
     * int i = fis.getTulokset(urheilijat.get(0)).size();
     * fis.lisaa(urheilijat.get(0), new Tulos());
     * fis.getTulokset(urheilijat.get(0)).size() == i+1 === true;
     * </pre>
     */
    public void lisaa(Urheilija urheilija, Tulos tulos) {
        tulos.setFiscode(urheilija.getFiscode());
        tulokset.lisaa(tulos);
    }
    
    
    /**
     * Lisää urheilijan seuranta -tietorakenteeseen
     * @param urheilija Urheilija
     * @example
     * <pre name="test">
     * Fis fis = new Fis();
     * Urheilija urheilija = new Urheilija();
     * fis.seurannassa(urheilija) === false;
     * fis.lisaa(urheilija);
     * fis.seurannassa(urheilija) === true;
     * </pre>
     */
    public void lisaa(Urheilija urheilija) {
        @SuppressWarnings("deprecation")
        Integer inte = new Integer(urheilija.getFiscode());
        seuranta.lisaa(inte);
    }
    
    
    /**
     * Poistaa urheilijan seuranta -tietorakenteesta
     * @param urheilija Urheilija
     * @example
     * <pre name="test">
     * Fis fis = new Fis();
     * Urheilija urheilija = new Urheilija();
     * fis.seurannassa(urheilija) === false;
     * fis.lisaa(urheilija);
     * fis.seurannassa(urheilija) === true;
     * fis.poista(urheilija);
     * fis.seurannassa(urheilija) === false;
     * </pre>
     */
    public void poista(Urheilija urheilija) {
        @SuppressWarnings("deprecation")
        Integer inte = new Integer(urheilija.getFiscode());
        seuranta.poista(inte);
    }
    
    
    /**
     * Poistaa tuloksen tietorakenteesta
     * @param tulos Tulos
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * Urheilija urheilija = fis.getUrheilijat().get(0);
     * urheilija == null === false;
     * int i = fis.getTulokset(urheilija).size();
     * Tulos tulos = new Tulos();
     * fis.lisaa(urheilija, tulos);
     * fis.getTulokset(urheilija).size() === i+1;
     * fis.poista(tulos);
     * fis.getTulokset(urheilija).size() === i;
     * </pre>
     */
    public void poista(Tulos tulos) {
        tulokset.poista(tulos);
    }
    
    
    /**
     * @param urheilija Urheilija
     * @return onko urheilija seurannassa?
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * Urheilija urheilija = fis.getUrheilijat().get(0);
     * boolean seurannassa = fis.seurannassa(urheilija);
     * fis.vaihdaSeuranta(urheilija);
     * fis.seurannassa(urheilija) == seurannassa === false;
     * </pre>
     */
    public boolean seurannassa(Urheilija urheilija) {
        return seuranta.seurannassa(urheilija.getFiscode());
    }
    
    
    /**
     * Vaihtaa urheilijan joko seurantaan tai pois
     * @param urheilija Urheilija
     * @return onko urheilija vaihdon jälkeen seurannassa
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * Urheilija urheilija = fis.getUrheilijat().get(0);
     * boolean seurannassa = fis.seurannassa(urheilija);
     * fis.vaihdaSeuranta(urheilija);
     * fis.seurannassa(urheilija) == seurannassa === false;
     * </pre>
     */
    @SuppressWarnings("deprecation")
    public boolean vaihdaSeuranta(Urheilija urheilija) {
        boolean onkoSeurannassa = this.seurannassa(urheilija);
        if (onkoSeurannassa)
            seuranta.poista(new Integer(urheilija.getFiscode()));
        else
            seuranta.lisaa(new Integer(urheilija.getFiscode()));
        return !onkoSeurannassa;
    }
    
    
    /**
     * Vaihtaa urheilijan joko seurantaan tai pois
     * @param tulos Tulos, josta selviää kenen urheilijan tulos on kyseessä
     * @return onko urheilija vaihdoin jälkeen seurannassa
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.lueTiedostoista();
     * Urheilija urheilija = fis.getUrheilijat().get(0);
     * boolean seurannassa = fis.seurannassa(urheilija);
     * Tulos tulos = new Tulos();
     * fis.lisaa(urheilija, tulos);
     * fis.vaihdaSeuranta(tulos);
     * fis.seurannassa(urheilija) == seurannassa === false;
     * </pre>
     */
    public boolean vaihdaSeuranta(Tulos tulos) {
        return this.vaihdaSeuranta(this.getUrheilija(tulos));
    }
    
    
    /**
     * @return Onko tietorakenteita muutettu eli pitääkö mahdollisesti tallentaa
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.onkoMuutettu() === false;
     * fis.lueTiedostoista();
     * fis.onkoMuutettu() === false;
     * fis.lisaa(fis.getUrheilijat().get(10));
     * fis.onkoMuutettu() === true;
     * </pre>
     */
    public boolean onkoMuutettu() {
        if (urheilijat.getMuutettu() || tulokset.getMuutettu() || seuranta.getMuutettu()) return true;
        return false;
    }
    
    
    /**
     * Lukee urheilijatietokannan tiedostosta Urheilijat olioon
     * @throws SailoException tietokantaa ei löytynyt eikä uutta pystytty lataamaan
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Fis fis = new Fis();
     * fis.getUrheilijat().size() === 0;
     * fis.lueUrheilijatTiedostosta();
     * fis.getUrheilijat().size() > 0 === true;
     * </pre>
     */
    public void lueUrheilijatTiedostosta() throws SailoException {
        urheilijat.lueTiedostosta();
    }
    
    
    /**
     * Päivittää urheilijatietokannan netistä
     * @throws SailoException tietokannan lataus epäonnistui
     */
    public void paivitaUrheilijaKanta() throws SailoException {
        urheilijat.paivitaKanta();
    }
    
    
    /**
     * Tarkistaa uusimman tietokantaversion netistä
     * @return tietokannan versio
     * @throws SailoException tarkistus ei onnistunut
     */
    public int getSaatavillaUrheilijaTietokanta() throws SailoException {
        return urheilijat.getSaatavillaKantaVersio();
    }
    
    
    /**
     * Lukee tulokset tiedostosta tietorakenteeseen
     */
    public void lueTuloksetTiedostosta() {
        tulokset.lueTiedostosta();
    }
    
    
    /**
     * Lukee seurannan tiedostosta tietorakenteeseen
     */
    public void lueSeurantaTiedostosta() {
        seuranta.lueTiedostosta();
    }
    
    
    /**
     * Lukee ohjelman vaadittavat tiedostot ja asettaa datat tietorakenteisiin
     * @throws SailoException Tiedostojen lukeminen ei onnistunut
     */
    public void lueTiedostoista() throws SailoException {
        lueUrheilijatTiedostosta();
        lueTuloksetTiedostosta();
        lueSeurantaTiedostosta();
        urheilijat.setMuutettu(false);
        tulokset.setMuutettu(false);
        seuranta.setMuutettu(false);
    }
    
    
    /**
     * Tallentaa tiedot tiedostoihin.
     * @throws SailoException Tallennus ei onnistunut
     */
    public void tallenna() throws SailoException {
        tulokset.tallennaTiedostoon();
        seuranta.tallennaTiedostoon();
    }
}
