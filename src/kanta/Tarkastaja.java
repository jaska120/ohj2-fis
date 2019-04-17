package kanta;

/**
 * Tarkastaja-luokka
 * Luokalla on tarkoitus hoitaa oikeellisuustarkistuksia.
 * Tällä hetkellä implementoituna vain regex tarkastaja.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
public abstract class Tarkastaja {
    /**
     * @param tarkistettava Merkkijono joka tarkistetaan
     * @param regex Regular expression jota vastaan merkkijono tarkistetaan
     * @return vastaako regexiä
     */
    public static boolean tarkista(String tarkistettava, String regex) {
        return tarkistettava.matches(regex);
    }
}
