package fis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parse-luokka
 * Helpottaa merkkijonojen muuttamista esimerkiksi luvuiksi ja päivämääriksi
 * Luokka heittää virheen, jos muuttaminen ei onnistu.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public class Parse {
    
    /**
     * Käsittelee luvuksi merkkijonosta
     * @param strLuku luku merkkijonona
     * @return luku tai -1, jos merkkijonon pituus = 0, tai tyhjä tai ei pystytä käsittelemään
     * @example
     * <pre name="test">
     * parseDouble(null) ~~~ -1;
     * parseDouble("") ~~~ -1;
     * parseDouble("j238") ~~~ -1;
     * parseDouble("0.0") ~~~ 0.0;
     * parseDouble("56.67") ~~~ 56.67;
     * </pre>
     */
    public static double parseDouble(String strLuku) {
        if (strLuku != null && strLuku.length() > 0) {
            try {
               return Double.parseDouble(strLuku);
            } catch(Exception e) {
               return -1;
            }
        }
        return -1;
    }
    
    
    /**
     * Käsittelee luvuksi merkkijonosta
     * @param strLuku luku merkkijonona
     * @return luku tai -1, jos merkkijonon pituus <= 0, tai tyhjä tai ei pystytä käsittelemään
     * @example
     * <pre name="test">
     * parseInt(null) === -1;
     * parseInt("") === -1;
     * parseInt("mi6") === -1;
     * parseInt("0") === 0;
     * parseInt("-747") === -747;
     * </pre>
     */
    public static int parseInt(String strLuku) {
        if (strLuku != null && strLuku.length() > 0) {
            try {
               return Integer.parseInt(strLuku);
            } catch(Exception e) {
               return -1;
            }
        }
        return -1;
    }
    
    
    /**
     * @param strDate päivämäärä merkkijonona
     * @param strFormat format muoto merkkijonona, esimerkiksi "yyyy-mm-dd"
     * @return päivämäärä olio tai null
     * @throws SailoException merkkijonoa ei pystytty formatoimaan annettuun muotoon
     * @example
     * <pre name="test">
     * #THROWS fis.SailoException
     * #import java.util.Date;
     * Date date = parseDate("2001-01-16", "yyyy-MM-dd");
     * Date date2 = new Date(101, 0, 16);
     * date.equals(date2) === true;
     * parseDate("2001-01-16", "yyyy.MM.dd"); #THROWS fis.SailoException
     * Date date3 = parseDate("51-01-16", "yyyy-MM-dd");
     * Date date4 = new Date(-1849, 0, 16);
     * date3.equals(date4) === true;
     * </pre>
     */
    public static Date parseDate(String strDate, String strFormat) throws SailoException {
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        Date date2;
        try {
            date2 = format.parse(strDate);
        } catch (ParseException e) {
            throw new SailoException("Päivämäärä väärässä muodossa", e);
        }
        return date2;
    }
    
    
    /**
     * Palauttaa päivämäärän formatoituna merkkijonona dd.MM.yyyy
     * @param date Päivämäärä
     * @return päivämäärä formatoituna merkkijonona
     */
    public String toString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(date);
    }
}
