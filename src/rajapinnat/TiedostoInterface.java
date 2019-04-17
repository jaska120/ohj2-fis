package rajapinnat;

import fis.SailoException;

/**
 * TiedostoInterface-rajapinta
 * Määrittää mitkä metodit Tiedoston lukemisessa ja kirjoittamisessa pitäisi toteuttaa.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@gmail.com
 * @version 4.4.2019
 */
public interface TiedostoInterface {
    /**
     * @param sailoObjekti Objekti, jonka tietorakenteeseen säilötään
     * @param alkioLuokka Alkioluokka, jonka instanssia säilötään tietorakenteeseen pitää implementoida AlkioInterface
     * @param tiedosto Tiedoston nimi
     * @param otsake Tiedoston otsakerivi
     */
    <T extends AlkioInterface>void lueTiedostosta(Object sailoObjekti, Class<T> alkioLuokka,
            String tiedosto, String otsake);
    
    
    /**
     * @param objekti Objekti, jonka tietorakenteeseen säilötään ja josta luodaan alkioita
     * @param tiedosto Tiedoston nimi
     * @param otsake Tiedoston otsakerivi
     */
    void lueTiedostosta(Object objekti, String tiedosto, String otsake);
    
    
    /**
     * @param sailoObjekti Objekti, jonka tietorakenteesta tallennetaan
     * @param tiedosto Tiedostonimi, johon tallennetaan
     * @param tmpTiedosto Väliaikaisen tiedoston nimi, johon tallennetaan ensin
     * @param otsake Csv-tiedostoon asetettava otsakerivi
     * @throws SailoException Tiedoston tallennus ei onnistunut
     */
    void tallennaTiedostoon(Object sailoObjekti, String tiedosto, 
            String tmpTiedosto, String otsake) throws SailoException;
    
    
    /**
     * @param muutettu Onko tietorakenne muuttunut
     */
    void setMuutettu(boolean muutettu);
    
    
    /**
     * @return Onko tietorakenne muuttunut
     */
    boolean getMuutettu();
}
