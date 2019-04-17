package fis;

/**
 * Poikkeusluokka tietorakenteiden poikkeuksille ja vähän muillekin poikkeuksille :)
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public class SailoException extends Exception {
    
    /**
     * Luokan versionumero
     */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Poikkeuksen muodostaja viestillä
     * @param viesti viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
    
    
    /**
     * Poikkeuksen muodostaja viestillä ja poikkeuksella
     * @param viesti viesti
     * @param e poikkeus
     */
    public SailoException(String viesti, Throwable e) {
        super(viesti, e);
    }
}