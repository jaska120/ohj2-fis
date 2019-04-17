package fis;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import rajapinnat.AlkioInterface;

/**
 * Urheilija-luokka
 * Osaa asettaa itseensä tietoa pilkulla erotetusta merkkijonosta.
 * Osaa asettaa tietonsa pilkulla erotettuun merkkijonoon.
 * Osaa tulostaa tietojansa pyydettyyn tulostukseen.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@gmail.com
 * @version 4.4.2019
 */
public class Urheilija extends Parse implements AlkioInterface {
    private int Listid;
    private String Listname;
    private int Published;
    private String Sectorcode;
    private String Status;
    private int Competitorid;
    private int Fiscode;
    private String Lastname;
    private String Firstname;
    private String Nationcode;
    private String Gender;
    private Date Birthdate;
    private String Skiclub;
    private String Nationalcode;
    private String Competitorname;
    private int Birthyear;
    private double DHpoints;
    private int DHpos;
    private String DHSta;
    private double SLpoints;
    private int SLpos;
    private String SLSta;
    private double GSpoints;
    private int GSpos;
    private String GSSta;
    private double SGpoints;
    private int SGpos;
    private String SGSta;
    private double ACpoints;
    private int ACpos;
    private String ACSta;
    private final String[] otsakkeet = {"Nimi", "Fis koodi", "Syntymäaika", "SL", "GS", "SG", "DH", "AC"};
    private final int[] otsakkeetIndeksit = {14, 6, 11, 19, 22, 25, 16, 28}; // Taulukon pituus pitäisi olla sama kuin otsakkeet -taulukon pituus


    /**
     * Asettaa urheilijan atribuutit indeksin mukaan
     * @param i indeksi
     * @param value merkkijono
     * @throws SailoException päivämäärä on väärässä muodossa
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getFiscode() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getFiscode() === 502468;
     * urheilija.toString() === "AABERG Filip";
     * urheilija.get(0) === "278";
     * </pre>
     */
    private void set(int i, String value) throws SailoException {
        switch (i) {
        case 0:
            Listid = parseInt(value);
            break;
        case 1:
            Listname = value;
            break;
        case 2:
            Published = parseInt(value);
            break;
        case 3:
            Sectorcode = value;
            break;
        case 4:
            Status = value;
            break;
        case 5:
            Competitorid = parseInt(value);
            break;
        case 6:
            Fiscode = parseInt(value);
            break;
        case 7:
            Lastname = value;
            break;
        case 8:
            Firstname = value;
            break;
        case 9:
            Nationcode = value;
            break;
        case 10:
            Gender = value;
            break;
        case 11:
            Birthdate = parseDate(value, "yyyy-MM-dd");
            break;
        case 12:
            Skiclub = value;
            break;
        case 13:
            Nationalcode = value;
            break;
        case 14:
            Competitorname = value;
            break;
        case 15:
            Birthyear = parseInt(value);
            break;
        case 16:
            DHpoints = parseDouble(value);
            break;
        case 17:
            DHpos = parseInt(value);
            break;
        case 18:
            DHSta = value;
            break;
        case 19:
            SLpoints = parseDouble(value);
            break;
        case 20:
            SLpos = parseInt(value);
            break;
        case 21:
            SLSta = value;
            break;
        case 22:
            GSpoints = parseDouble(value);
            break;
        case 23:
            GSpos = parseInt(value);
            break;
        case 24:
            GSSta = value;
            break;
        case 25:
            SGpoints = parseDouble(value);
            break;
        case 26:
            SGpos = parseInt(value);
            break;
        case 27:
            SGSta = value;
            break;
        case 28:
            ACpoints = parseDouble(value);
            break;
        case 29:
            ACpos = parseInt(value);
            break;
        case 30:
            ACSta = value;
            break;
        default:
            break;
        }
    }
    
    
    /**
     * Antaa urheilijat attribuutin indeksin mukaan
     * @param i indeksi
     * @return atribuutin arvo merkkijonona
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.get(0) === "0";
     * urheilija.get(1) === null;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.get(0) === "278";
     * urheilija.get(1) === "13th FIS points list 2018/2019";
     * urheilija.get(100) === null;
     * </pre>
     */
    public String get(int i) {
        switch (i) {
        case 0:
            return Integer.toString(Listid);
        case 1:
            return Listname;
        case 2:
            return Integer.toString(Published);
        case 3:
            return Sectorcode;
        case 4:
            return Status;
        case 5:
            return Integer.toString(Competitorid);
        case 6:
            return Integer.toString(Fiscode);
        case 7:
            return Lastname;
        case 8:
            return Firstname;
        case 9:
            return Nationcode;
        case 10:
            return Gender;
        case 11:
            return this.toString(Birthdate);
        case 12:
            return Skiclub;
        case 13:
            return Nationalcode;
        case 14:
            return Competitorname;
        case 15:
            return Integer.toString(Birthyear);
        case 16:
            return Double.toString(DHpoints);
        case 17:
            return Integer.toString(DHpos);
        case 18:
            return DHSta;
        case 19:
            return Double.toString(SLpoints);
        case 20:
            return Integer.toString(SLpos);
        case 21:
            return SLSta;
        case 22:
            return Double.toString(GSpoints);
        case 23:
            return Integer.toString(GSpos);
        case 24:
            return GSSta;
        case 25:
            return Double.toString(SGpoints);
        case 26:
            return Integer.toString(SGpos);
        case 27:
            return SGSta;
        case 28:
            return Double.toString(ACpoints);
        case 29:
            return Integer.toString(ACpos);
        case 30:
            return ACSta;
        default:
            return null;
        }
    }
    
    
    /**
     * Määrittää olion attribuutit merkkijonosta
     * @param rivi merkkijono
     * @throws SailoException virhe
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getFiscode() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getFiscode() === 502468;
     * urheilija.toString() === "AABERG Filip";
     * </pre>
     */
    @Override
    public void parse(String rivi) throws SailoException {
        String[] values = rivi.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (values.length != 31) throw new SailoException("Väärä pituus luettavassa tiedostossa.");
        for (int i = 0; i < values.length; i++) {
            set(i, values[i].replaceAll("^\"|\"$", ""));
        }
    }
    
    
    /**
     * @return fis koodi
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getFiscode() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getFiscode() === 502468;
     * urheilija.toString() === "AABERG Filip";
     * </pre>
     */
    public int getFiscode() {
        return Fiscode;
    }
    
    
    /**
     * @return urheilijan id
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getCompetitorid() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getCompetitorid() === 219656;
     * urheilija.toString() === "AABERG Filip";
     * </pre>
     */
    public int getCompetitorid() {
        return Competitorid;
    }
    
    
    /**
     * @return syntymäpäivä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.Date;
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getFiscode() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getFiscode() === 502468;
     * urheilija.toString() === "AABERG Filip";
     * Date date = urheilija.getBirthDate();
     * Date date2 = new Date(101, 9, 19);
     * date.equals(date2) === true;
     * </pre>
     */
    public Date getBirthDate() {
        return Birthdate;
    }
    
    
    /**
     * Tulostetaa urheilijat tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        for (int i = 0; i < otsakkeet.length; i++) {
            out.println(otsakkeet[i] + ":" + "  " + get(otsakkeetIndeksit[i]));
        }
    }
    
    
    /**
     * Tulostaa urheilijan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
    /**
     * Palauttaa urheilijan nimen merkkijonoona
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilija urheilija = new Urheilija();
     * urheilija.toString() === null;
     * urheilija.getFiscode() === 0;
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilija.getFiscode() === 502468;
     * urheilija.toString() === "AABERG Filip";
     * </pre>
     */
    @Override
    public String toString() {
        return Competitorname;
    }
    
    
    /**
     * Tarkistaa onko olio sama kuin toinen eli onko olioilla sama fis koodi, koska fis koodi on uniikki
     * @param object olio
     * @return totuusarvo
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * int code = 123;
     * int code2 = 456;
     * int code3 = 9948;
     * Urheilija urheilija = new Urheilija();
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,"+code+",AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * Urheilija urheilija2 = new Urheilija();
     * rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,"+code2+",AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija2.parse(rivi);
     * Urheilija urheilija3 = new Urheilija();
     * rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,"+code3+",AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija3.parse(rivi);
     * urheilija.equals(urheilija2) === false;
     * urheilija.equals(urheilija3) === false;
     * urheilija2.equals(urheilija3) === false;
     * urheilija.equals(urheilija) === true;
     * </pre>
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Urheilija)) return false;
        return this.getFiscode() == ((Urheilija) object).getFiscode();
    }


    @Override
    public int hashCode() {
        return this.getFiscode();
    }
}
