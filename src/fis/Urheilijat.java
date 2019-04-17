package fis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Urheilija-luokka
 * Säilöö Urheilija olioita tietorakenteeseensa ja antaa niitä pyydettäessä.
 * Osaa tarkistaa internetistä uusimman tietokannan version ja lataamaan,
 * ottamaan sen käyttöön ja tallentamaan tarvittaessa.
 * Pitää kirjaa nykyisestä tietokannasta.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 *
 */
public class Urheilijat extends TiedostoKasittely {
    private List<Urheilija> urheilijat = new ArrayList<Urheilija>();
    private int nykyinenKantaVersio = 0;
    private int saatavillaKantaVersio = 0;
    private final String kansionNimi = "data/listat"; // kansion nimi, johon tallennetaan listoja
    private final String tiedostoOtsake = "Listid,Listname,Published,Sectorcode,Status,Competitorid,Fiscode,Lastname,Firstname,Nationcode,Gender,Birthdate,Skiclub,Nationalcode,Competitorname,Birthyear,DHpoints,DHpos,DHSta,SLpoints,SLpos,SLSta,GSpoints,GSpos,GSSta,SGpoints,SGpos,SGSta,ACpoints,ACpos,ACSta";
    private final String fisKantaUrl = "https://data.fis-ski.com/fis_athletes/ajax/fispointslistfunctions/export_fispointslist.html?export_csv=true&sectorcode=AL&listid="; // osoite, josta ladataan pistelistat
    private final String fisRSSUrl = "https://www.fis-ski.com/DB/alpine-skiing/fis-points-lists.html?sectorcode=AL&rss=true"; // rss osoite, josta tarkistetaan uusin voimassa oleva lista
    
    
    /**
     * Muodostaja urheilijat-säilöntäluokalle
     */
    public Urheilijat() {
        tarkistaNykyinenKantaVersio();
    }
    
    
    /*
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#lisaa(java.lang.Object)
     * @example
     * <pre name="test">
     * Urheilijat urheilijat = new Urheilijat();
     * urheilijat.getList().size() === 0;
     * int y = 5;
     * for (int i=0; i<y; i++) {
     *      urheilijat.lisaa(new Urheilija());
     * }
     * urheilijat.getList().size() === y;
     * </pre>
     */
    @Override
    public void lisaa(Object object) {
        urheilijat.add((Urheilija) object);
    }
    
    
    /*
     * Ei implementoitu, koska urheilijatietokannan muokkaus ei ole tämän ohjelman tehtävä.
     * (non-Javadoc)
     * @see rajapinnat.SailoInterface#poista(java.lang.Object)
     */
    @Override
    public void poista(Object object) {
        return;
    }
    
    
    /**
     * Palauttaa fis koodia vastaavan urheilijan
     * @param fisCode fis koodi
     * @return fis koodia vastaava urheilija tai null, jos ei ole
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilijat urheilijat = new Urheilijat();
     * Urheilija urheilija = new Urheilija();
     * String rivi = "278,13th FIS points list 2018/2019,1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,Nolby Alpina,SWE,AABERG Filip,2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * urheilija.parse(rivi);
     * urheilijat.lisaa(urheilija);
     * int y = 5;
     * for (int i=0; i<y; i++) {
     *      urheilijat.lisaa(new Urheilija());
     * }
     * urheilijat.get(502468).equals(urheilija);
     * </pre>
     */
    public Urheilija get(int fisCode) {
        for (Urheilija urheilija : urheilijat) {
            if (urheilija.getFiscode() == fisCode)
                return urheilija;
        }
        return null;
    }
    
    
    /**
     * Metodi palauttaa urheilijat listana ja yrittää lukea urheilijat listaan, jos ei ole vielä luettu
     * @return Urheilijalista
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.List;
     * Urheilijat urheilijat = new Urheilijat();
     * urheilijat.getList().size() === 0;
     * urheilijat.lueTiedostosta();
     * List<Urheilija> listUrheilijat = urheilijat.getList();
     * listUrheilijat.size() > 0 === true;
     * </pre>
     */
    @Override
    public List<Urheilija> getList() {
        return urheilijat;
    }
   
    
    /**
     * Palauttaa urheilijat listan koon
     * @return Urheilijoiden lukumäärä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Urheilijat urheilijat = new Urheilijat();
     * urheilijat.getLkm() === 0;
     * urheilijat.lueTiedostosta();
     * int lkm = urheilijat.getLkm();
     * lkm > 10 === true;
     * </pre>
     */
    public int getLkm() {
        return urheilijat.size();
    }
    
    
    /**
     * @return Käytössä olevan tietokannannimi
     * @throws SailoException poikkeus, jos kantaa ei ole, eikä sitä myöskään pystytä lataamaan
     * @example
     */
    public String getKantaTiedostoNimi() throws SailoException {
        return kansionNimi + "/" + getNykyinenKantaVersio() + ".csv";
    }
    
    
    /**
     * @return Uusin levyltä löytyvän urheilijatietokannan numero tai 0, jos levyltä ei löydy kantoja
     * @throws SailoException kantaa ei onnistuttu päivittämään
     */
    public int getNykyinenKantaVersio() throws SailoException {
        if (nykyinenKantaVersio == 0)
            if (tarkistaNykyinenKantaVersio() == 0) {
                paivitaKanta();
            }
        return nykyinenKantaVersio;
    }


    /**
     * Hakee uusimman version numeron netistä
     * @return Uusin netistä saatavilla olevan urheilijatietokannan numero tai 0, jos kantaa ei löydetty;
     * @throws SailoException poikkeus uutta versiota ei pystytty hakemaan
     */
    public int getSaatavillaKantaVersio() throws SailoException {
        if (saatavillaKantaVersio == 0) {
            return tarkistaSaatavillaKantaVersio();
        }
        return saatavillaKantaVersio;
    }
    
    
    /**
     * Tarkistaa onko tietokanta ajantasalla ja päivittää tarvittaessa.
     * @return Käytöön tulevan tietokannan tiedostonimi tai null jos uutta tietokantaa ei pystytty tarkistamaan tai lataamaan, jolloin käytetään projektin omaa tietokantaa.
     * @throws SailoException poikkeus
     */
    public String paivitaKanta() throws SailoException {
        int uusiTietokanta, nykyinenTietokanta;
        String kannanNimi;
        if ((uusiTietokanta = getSaatavillaKantaVersio()) > (nykyinenTietokanta = getNykyinenKantaVersio()))
            if ((kannanNimi = lataaTietokanta(uusiTietokanta)) != null) {
                nykyinenKantaVersio = saatavillaKantaVersio;
                return kannanNimi;
            }
        kannanNimi = nykyinenTietokanta + ".csv";  
        return kannanNimi;
    }
    
    
    /**
     * Tarkistaa https://www.fis-ski.com/DB/alpine-skiing/fis-points-lists.html?sectorcode=AL&rss=true
     * feedistä uusimman tietokantaversion numeron.
     * @return tietokannan versio tai 0, jos uutta kannan versiota ei pystytty tarkistamaan
     * @throws SailoException poikkeus
     */
    private int tarkistaSaatavillaKantaVersio() throws SailoException {
        String kantaVersioRivi = etsiRSSFeedista(fisRSSUrl, ".*listid=[0-9]*.*");
        Pattern pattern = Pattern.compile("listid=[0-9]*");
        Matcher matcher = pattern.matcher(kantaVersioRivi);
        int uusiVersio = 0;
        if (matcher.find())
        {
            uusiVersio = Integer.parseInt(matcher.group(0).substring(7)); // version numero merkkijonosta listid=vvv
        }
        saatavillaKantaVersio = uusiVersio;
        return uusiVersio;
    }
    
    
    /**
     * Lukee hakemistolistauksesta nykyisen tietokantaversion
     * @return tietokantaversio tai 0, jos tietokantaa ei ole olemassa tai sitä ei pystytä vertaamaan.
     */
    private int tarkistaNykyinenKantaVersio() {
        int versio = 0;
        File kansio = new File(kansionNimi);
        File[] tiedostot = kansio.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("[0-9]*.csv");
            }
        });
        int luettuVersio;
        for (File tiedosto : tiedostot) {
            String nimi;
            if ((luettuVersio = Integer.parseInt((nimi = tiedosto.getName()).substring(0, nimi.lastIndexOf('.')))) > versio)
                versio = luettuVersio;
        }
        nykyinenKantaVersio = versio;
        return versio;
    }
    
    
    /**
     * Etsii ja palauttaa URL osoitteesta ensimmäisen regexiä vastaavan merkkijonon
     * @param url url
     * @param regExp regexp
     * @return merkkijonorivi, joka vastaa rexexpiä tai null jos vastaavuuksia ei löydy
     * @throws SailoException poikkeus
     */
    private String etsiRSSFeedista(String url, String regExp) throws SailoException {
        URL rssUrl;
        @SuppressWarnings("resource")
        BufferedReader br = null;
        try{
            rssUrl = new URL (url);
            br = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
            String palautettava = null;
            String line;
            while((line=br.readLine()) != null){
                if (line.matches(regExp)) {
                    palautettava = line;
                    break;
                }
            }
            return palautettava;
        } catch (MalformedURLException err){
            throw new SailoException("Url on väärin.", err);
        } catch (IOException err){
            throw new SailoException("RSS feediä ei pystytty lukemaan.", err);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    throw new SailoException("Tiedoston käsittelyssä on ongelma.", e);
                }
        }
    }
    
    
    /**
     * Lataa internetistä levylle tietyn urheilijatietokannan
     * @param versio mikä versio ladataan
     * @return Ladatun tietokannan tiedostonimi tai null, jos lataus ei onnistunut
     * @throws SailoException poikkeustilanteet latauksessa ja tiedoston luonnissa
     */
    @SuppressWarnings("resource")
    public String lataaTietokanta(int versio) throws SailoException {
        String kanta = fisKantaUrl + versio;
        String kannanNimi = null;
        String tmpKannanNimi = kansionNimi + "/" + versio + ".csv";
        URL url;
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        try {
            url = new URL(kanta);
            rbc = Channels.newChannel(url.openStream());
            fos = new FileOutputStream(tmpKannanNimi);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            kannanNimi = tmpKannanNimi;
        } catch (MalformedURLException e) {
            throw new SailoException("Muodostettu url on väärin.", e);
        } catch (IOException e) {
            throw new SailoException("Tiedoston käsitellyssä ei onnistu.", e);
        } finally {
            if (rbc != null)
                try {
                    rbc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return kannanNimi;
    }
    
    
    /**
     * Lukee tietokannan tiedostosta Urheilijat tietorakenteeseen.
     * @throws SailoException tietokantaa ei löydy levyltä
     * @example
     * <pre name="test">
     * #THROWS IOException, SailoException
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import fis.Urheilija;
     * #import java.util.List;
     * String tiedosto = "testiUrheilijat.csv";
     * String otsake = "Listid,Listname,Published,Sectorcode,Status,Competitorid,Fiscode,Lastname,Firstname,Nationcode,Gender,Birthdate,Skiclub,Nationalcode,Competitorname,Birthyear,DHpoints,DHpos,DHSta,SLpoints,SLpos,SLSta,GSpoints,GSpos,GSSta,SGpoints,SGpos,SGSta,ACpoints,ACpos,ACSta";
     * String u1 = "278,\"13th FIS points list 2018/2019\",1,AL,O,219656,502468,AABERG,Filip,SWE,M,2001-10-19,\"Nolby Alpina\",SWE,\"AABERG Filip\",2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * String u2 = "278,\"13th FIS points list 2018/2019\",1,AL,O,219656,1234,AABERG,Filip,SWE,M,2001-10-19,\"Nolby Alpina\",SWE,\"AABERG Filip\",2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * String u3 = "278,\"13th FIS points list 2018/2019\",1,AL,O,219656,5678,AABERG,Filip,SWE,M,2001-10-19,\"Nolby Alpina\",SWE,\"AABERG Filip\",2001,191.08,1587,+,66.57,1817,,77.99,2430,,131.07,2036,*,,,";
     * Urheilijat urheilijat = new Urheilijat();
     * VertaaTiedosto.kirjoitaTiedosto(tiedosto,
     *      otsake + "\n" +
     *      u1 + "\n" +
     *      u2 + "\n" +
     *      u3 + "\n");
     * urheilijat.getLkm() === 0;
     * urheilijat.lueTiedostosta(urheilijat, Urheilija.class, tiedosto, otsake);
     * urheilijat.getLkm() === 3;
     * urheilijat.get(5678) == null === false;
     * VertaaTiedosto.tuhoaTiedosto(tiedosto);
     * </pre>
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(this, Urheilija.class, getKantaTiedostoNimi(), tiedostoOtsake);
    }
}
