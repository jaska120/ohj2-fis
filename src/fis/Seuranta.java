package fis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rajapinnat.AlkioInterface;

/**
 * Seuranta-luokka
 * Luokka hoitaa seurannan hallinnan ohjelmassa.
 * Osaa lisätä ja poistaa uniikkeja fis koodeja seurannasta.
 * Osaa tallentaa ja lukea seurannan tiedostoista.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 * Seuranta-luokka
 */
public class Seuranta extends TiedostoKasittely implements AlkioInterface {
    private final String tiedosto = "data/seuranta.csv";
    private final String tmpTiedosto = "data/tmpSeuranta.csv";
    private final String otsake = "fis_code";
    private List<Integer> seuratut = new LinkedList<Integer>();
    
    
    /*
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#lisaa(java.lang.Object)
     * @example
     * <pre name="test">
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * int y = 5;
     * for (int i=0; i<y; i++) {
     *      seuranta.lisaa(new Integer(0));
     * }
     * seuranta.getList().size() === y;
     * </pre>
     */
    @Override
    public void lisaa(Object object) {
        seuratut.add((Integer) object);
        setMuutettu(true);
    }
    

    /*
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#poista(java.lang.Object)
     * @example
     * <pre name="test">
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * int y = 5;
     * for (int i=0; i<y; i++) {
     *      seuranta.lisaa(new Integer(0));
     * }
     * seuranta.getList().size() === y;
     * seuranta.poista(new Integer(1));
     * seuranta.getList().size() === y;
     * seuranta.poista(new Integer(0));
     * seuranta.getList().size() === 0;
     * </pre>
     */
    @Override
    public void poista(Object object) {
        for (Iterator<Integer> iterator = seuratut.iterator(); iterator.hasNext();) {
            Integer integer = iterator.next();
            if (integer.intValue() == ((Integer) object).intValue())
                iterator.remove();
        }
        setMuutettu(true);
    }
    

    /*
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#getList()
     * @example
     * <pre name="test">
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * int y = 5;
     * for (int i=0; i<y; i++) {
     *      seuranta.lisaa(new Integer(0));
     * }
     * seuranta.getList().size() === y;
     * </pre>
     */
    @Override
    public List<Integer> getList() {
        return seuratut;
    }

    /**
     * Asettaa olion attribuutit merkkijonosta
     * @param line merkkijono
     * @throws SailoException virhe
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * seuranta.parse("1");
     * seuranta.parse("3");
     * seuranta.getList().size() === 2;
     * </pre>
     */
    @SuppressWarnings("deprecation")
    @Override
    public void parse(String line) throws SailoException {
        String[] values = line.split(",");
        for (int i = 0; i < values.length; i++) {
            lisaa(new Integer(values[i]));
        }
    }
    
    
    /**
     * Totuusarvo kuuluuko urheilija seurantaan eli löytyykö
     * kyseisen urheilijan fis koodi seurannan tietorakenteesta
     * @param fisCode Fis koodi
     * @return onko urheilija seurannassa?
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Seuranta seuranta = new Seuranta();
     * seuranta.seurannassa(1234) == false === true;
     * seuranta.parse("1234");
     * seuranta.seurannassa(1234) == false === false;
     * </pre>
     */
    public boolean seurannassa(int fisCode) {
        for (Integer integer : seuratut) {
            if (integer.intValue() == fisCode)
                return true;
        }
        return false;
    }
    
    
    /**
     * Lukee seurannan fis koodit tiedostosta tietorakenteeseen
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import fis.Tulos;
     * String tiedosto = "testiSeuranta.csv";
     * String otsake = "comma,separated,value";
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * VertaaTiedosto.kirjoitaTiedosto(tiedosto,
     *      otsake + "\n" +
     *      "1\n" +
     *      "2\n" +
     *      "3");
     * seuranta.lueTiedostosta(seuranta, tiedosto, otsake);
     * seuranta.getList().size() === 3;
     * VertaaTiedosto.tuhoaTiedosto(tiedosto);
     * </pre>
     */
    public void lueTiedostosta() {
        lueTiedostosta(this, tiedosto, otsake);
    }
    
    
    /**
     * Tallentaa seurannan eli fis koodit tietorakenteesta tiedostoon
     * @throws SailoException Tallennus ei onnistunut.
     * @example
     * <pre name="test">
     * #THROWS IOException, SailoException
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import fis.Tulos;
     * String tiedosto = "testiSeuranta.csv";
     * String otsake = "comma,separated,value";
     * Seuranta seuranta = new Seuranta();
     * seuranta.getList().size() === 0;
     * VertaaTiedosto.kirjoitaTiedosto(tiedosto,
     *      otsake + "\n" +
     *      "1\n" +
     *      "2\n" +
     *      "3");
     * seuranta.lueTiedostosta(seuranta, tiedosto, otsake);
     * seuranta.getList().size() === 3;
     * seuranta.poista(new Integer(2));
     * seuranta.tallennaTiedostoon(seuranta, tiedosto, "tmpTestiSeuranta.csv", otsake);
     * String tulos =
     *      otsake + "\n" +
     *      "1\n" +
     *      "3\n";
     * VertaaTiedosto.vertaaFileString(tiedosto, tulos) === null;
     * VertaaTiedosto.tuhoaTiedosto(tiedosto);
     * </pre>
     */
    public void tallennaTiedostoon() throws SailoException {
        tallennaTiedostoon(this, tiedosto, tmpTiedosto, otsake);
    }
}
