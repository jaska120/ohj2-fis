package rajapinnat;

import java.util.List;

/**
 * SailoInterface-rajapinta
 * Määrittää mitkä metodit ohjelman tietorakenneluokkien tulisi toteuttaa.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public interface SailoInterface {
    
    /**
     * Lisää tietorakenteeseen olion.
     * @param object tietorakenteeseen lisättävä olio
     */
    void lisaa(Object object);
    
    
    /**
     * Poistaa tietorakenteesta olion.
     * @param object tietorakenteesta poistettava olio
     */
    void poista(Object object);
    
    
    /**
     * @return tietorakenteeseen säilötyt alkiot listana
     */
    List<?> getList();
}
