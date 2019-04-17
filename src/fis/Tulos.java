package fis;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import rajapinnat.AlkioInterface;

/**
 * Tulos-luokka
 * Luokka tuloksille.
 * Osaa lukea itseensä tietoa pilkulla erotetusta merkkijonosta.
 * Osaa määrittää itsensä pilkulla erotettuun merkkijonoon.
 * Tietää itsensä esittämiseen vaadittavat otsakkeet.
 * Tietää missä muodossa oma data pitäisi olla regex:llä.
 * Tietää millä "indekseillä" löytyy otsakkeita vastaava data kysyttäessä.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 11.2.2019
 */
public class Tulos extends Parse implements AlkioInterface {
    private int id;
    private int fisCode, position;
    private Date date;
    private String place, discipline;
    private double points;
    private static final String[] kenttaOtsakkeet = new String[] {"Päivämäärä", "Paikka", "Laji", "Sija", "Pisteet"}; // Mitkä kentät näytetään UI:ssa
    private static final int[] kenttaArvot = new int[] {2, 3, 4, 5, 6}; // Mistä "indekseistä" yllä olevat arvot löytyvät ja kenttaOtsakkeet.length == kenttaArvot.length
    private static final String[][] kenttaRegex = new String[][] { // Kenttien tarkistus regex ja tarkistusta vastaava ohje
            new String[] {
                    "((0?[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))\\.((0?[1-9])|(1[0-2]))\\.[0-9]{4}",
                    "pp.kk.vvvv"
            },
            new String[] {
                    "[A-Za-zåäöÅÄÖ]{2,50}", 
                    "2-50 kirjainta"
            },
            new String[] {
                    "AC|DH|SG|GS|SL",
                    "jokin seuraavista: AC, DH, SG, GS, SL"
            },
            new String[] {
                    "140|100|([1-9][0-9]?)|(1[0-3][0-9])",
                    "välillä 1-140"
            },
            new String[] {
                    "[0-9]{1,3}\\.[0-9]{1,2}",
                    "välillä 0.0-999.99"
            }
        };
    private final String[] malliPaikat = new String[] {"Levi", "Ylläs", "Pyhä", "Tahko", "Luleå", "Kiiruna"}; // Mallidataa varten
    private final String[] malliLajit = new String[] {"SL", "GS", "SG", "DH", "AC"}; // Mallidataa varten
    
    private static int nextId = 1; // Seuraavaksi lisättävän tuloksen id
    
    
    /**
     * @return Otsakekentät tulokselle
     */
    public static String[] getKenttaOtsakkeet() {
        return kenttaOtsakkeet;
    }
    
    
    /**
     * @return Otsakearvojen indeksi tulokselle
     */
    public static int[] getKenttaArvot() {
        return kenttaArvot;
    }
    
    
    /**
     * @return Otsakekenttien oikeellisuuden regex indeksissä 0 ja vastaava ohje indeksissä 1
     */
    public static String[][] getKenttaRegex() {
        return kenttaRegex;
    }
    
    
    /**
     * Palauttaa otsaketta vastaavan arvon Tuloksesta
     * @param i indeksi
     * @return tuloksen kenttä merkkijonona
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulos tulos = new Tulos();
     * tulos.getKenttaOtsakkeetArvo(1) === null;
     * tulos.taytaMalliDatalla();
     * String random = "random";
     * tulos.set(3, random);
     * tulos.getKenttaOtsakkeetArvo(1).equals(random);
     * </pre>
     */
    public String getKenttaOtsakkeetArvo(int i) {
        switch (i) {
        case 0:
            return this.toString(date);
        case 1:
            return place;
        case 2:
            return discipline;
        case 3:
            return Integer.toString(position);
        case 4:
            return Double.toString(points);
        default:
            return null;
        }
    }
    
    
    /**
     * @return Tuloksen id
     * @example
     * <pre name="test">
     * Tulos tulos = new Tulos();
     * tulos.getId() === 0;
     * tulos.setId();
     * tulos.getId() == 0 === false;
     * </pre>
     */
    public int getId() {
        return id;
    }
    
    
    /**
     * Asettaa tulokselle id:n, joka on uniikki
     * sillä nextId kasvaa aina kun uusia tuloksia luodaan.
     * @example
     * <pre name="test">
     * Tulos tulos = new Tulos();
     * tulos.getId() === 0;
     * tulos.setId();
     * tulos.getId() == 0 === false;
     * </pre>
     */
    public void setId() {
        setId(nextId);
    }
    
    
    /**
     * Asettaa tulokselle id:n, joka on uniikki
     * sillä nextId kasvaa aina kun uusia tuloksia luodaan.
     * @example
     * <pre name="test">
     * Tulos tulos = new Tulos();
     * tulos.getId() === 0;
     * tulos.setId();
     * tulos.getId() == 0 === false;
     * </pre>
     */
    private void setId(int id) {
        this.id = id;
        if (id >= nextId) nextId = id + 1;
    }
    
    
    /**
     * Palauttaa tuloksen fis koodin
     * @return fis koodi
     * @example
     * <pre name="test">
     * Tulos tulos = new Tulos();
     * tulos.getFiscode() === 0;
     * tulos.taytaMalliDatalla();
     * tulos.getFiscode() == 0 === false;
     * </pre>
     */
    public int getFiscode() {
        return fisCode;
    }
    
    
    /**
     * Asettaa tulokselle fis koodin
     * @param fisCode fis koodi
     * @example
     * <pre name="test">
     * int fiscode = 123;
     * Tulos tulos = new Tulos();
     * tulos.getFiscode() === 0;
     * tulos.taytaMalliDatalla();
     * fiscode == tulos.getFiscode() === false;
     * tulos.setFiscode(fiscode);
     * tulos.getFiscode() == fiscode === true;
     * </pre>
     */
    public void setFiscode(int fisCode) {
        this.fisCode = fisCode;
    }
    
    
    /**
     * Asettaa attribuutteja indeksin mukaan
     * @param i indeksi
     * @param value merkkijono
     * @throws SailoException Virhe arvon käsittelyssä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulos tulos = new Tulos();
     * tulos.getKenttaOtsakkeetArvo(3).equals("0") === true;
     * tulos.set(5, "10");
     * tulos.getKenttaOtsakkeetArvo(3).equals("10") === true;
     * </pre>
     */
    public void set(int i, String value) throws SailoException {
        switch (i) {
        case 0:
            this.setId(parseInt(value));
            break;
        case 1:
            fisCode = parseInt(value);
            break;
        case 2:
            date = parseDate(value, "dd.MM.yyyy");
            break;
        case 3:
            place = value;
            break;
        case 4:
            discipline = value;
            break;
        case 5:
            position = parseInt(value);
            break;
        case 6:
            points = parseDouble(value);
            break;
        default:
            break;
        }
    }
    
    
    /**
     * Tarkistaa onko olio sama kuin toinen eli onko olioilla sama id, koska id on uniikki
     * @param object olio
     * @return totuusarvo
     * @example
     * <pre name="test">
     * Tulos tulos = new Tulos();
     * tulos.setId();
     * Tulos tulos2 = new Tulos();
     * tulos2.setId();
     * Tulos tulos3 = new Tulos();
     * tulos3.setId();
     * tulos.equals(tulos2) === false;
     * tulos.equals(tulos3) === false;
     * tulos2.equals(tulos3) === false;
     * tulos.equals(tulos) === true;
     * </pre>
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Tulos)) return false;
        return this.getId() == ((Tulos) object).getId();
    }
    
    
    /**
     * Tayttaa olion attribuutit randomoidulla mallidatalla. Vain testikäyttöön.
     */
    public void taytaMalliDatalla() {
        this.setId(nextId);
        this.fisCode = 120097;
        //this.fisCode = ThreadLocalRandom.current().nextInt(1, 9999999);
        this.position = ThreadLocalRandom.current().nextInt(1, 100);
        this.date = new Date(ThreadLocalRandom.current().nextLong(1520166622076L, 1551702622076L));
        this.points = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
        this.place = malliPaikat[ThreadLocalRandom.current().nextInt(0, malliPaikat.length)];
        this.discipline = malliLajit[ThreadLocalRandom.current().nextInt(0, malliLajit.length)];
    }
    
    
    /**
     * Asettaa olion attribuutit merkkijonosta
     * @param line merkkijono
     * @throws SailoException virhe arvojen muuttamisessa merkkijonoista tyyppeihin
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulos tulos = new Tulos();
     * tulos.getFiscode() == 0 === true;
     * tulos.parse("1,120097,01.01.2019,Levi,GS,1,21.65");
     * tulos.getFiscode() == 0 === false;
     * tulos.getFiscode() == 120097 === true;
     * tulos.parse("ak,12,--,2"); #THROWS SailoException
     * </pre>
     */
    @Override
    public void parse(String line) throws SailoException {
        String[] values = line.split(",");
        for (int i = 0; i < values.length; i++) {
            set(i, values[i]);
        }
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     * Tulostaa olion tiedot pilkulla erotettuun merkkijonoon
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tulos tulos = new Tulos();
     * String line2 = "2,120097,02.01.2019,Pyhä,GS,14,28.11";
     * tulos.parse(line2);
     * tulos.toString().equals(line2);
     * String line = "1,120097,01.01.2019,Levi,GS,1,21.65";
     * tulos.parse(line);
     * tulos.toString().equals(line);
     * </pre>
     */
    @Override
    public String toString() {
        return "" +
                getId() + "," +
                fisCode + "," +
                toString(date) + "," +
                place + "," +
                discipline + "," +
                position + "," +
                points;
                
    }


    @Override
    public int hashCode() {
        return this.getId();
    }
}
