package rajapinnat;

import fis.SailoException;

/**
 * AlkioInterface-rajapinta
 * Määrittää mitkä metodit ohjelman tietorakenteissa säilöttyjen metodien
 * tulisi toteuttaa.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public interface AlkioInterface {
    /**
     * Käsittelee pilkulla erotetun merkkijonon olion attribuuteiksi
     * @param line Pilkku eroteltu merkkijonorivi
     * @throws SailoException Attribuuteiksi asettamisessa virhe eli data ei ole oikeassa muodossa
     */
    public void parse(String line) throws SailoException;
}
