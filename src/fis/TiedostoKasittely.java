package fis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import rajapinnat.AlkioInterface;
import rajapinnat.SailoInterface;
import rajapinnat.TiedostoInterface;

/**
 * TiedostoKasittely-luokka
 * Abstrakti luokka joka osaa lukea ja tallentaa tiedostoihin abstraktisti
 * tietorakenteiden alkioita. Pitää kirjaa onko tietorakenne muuttunut tiedoston
 * lukemisen ja kirjoittamisen välillä eli pitääkö tiedosto kirjoittaa uudelleen.
 * @author jaakkomustalahti
 * @email jaakko.jm.mustalahti@student.jyu.fi
 * @version 4.4.2019
 */
abstract class TiedostoKasittely implements TiedostoInterface, SailoInterface {
    private boolean muutettu = false; // Onko tietorakennetta muutettu tiedoston luvun ja tallentamisen välillä eli pitää tallentaa uudestaan
    
    
    /*
     * (non-Javadoc)
     * @see rajapinnat.TiedostoInterface#lueTiedostosta(java.lang.Object, java.lang.Class, java.lang.String, java.lang.String)
     */
    @Override
    public <T extends AlkioInterface> void lueTiedostosta(Object sailoObjekti, Class<T> alkioLuokka, String tiedosto, String otsake) {
        File file = new File(tiedosto);
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    if (!line.equals(otsake))
                        throw new SailoException("Otsakerivi tiedostossa " + tiedosto + "ei ole muodossa: \"" + otsake + "\"");
                    i++;
                    continue;
                }
                Object alkio = alkioLuokka.getDeclaredConstructor().newInstance();
                try {
                    ((AlkioInterface) alkio).parse(line);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ((SailoInterface) sailoObjekti).lisaa(alkio);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException | SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void lueTiedostosta(Object objekti, String tiedosto, String otsake) {
        File file = new File(tiedosto);
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    if (!line.equals(otsake))
                        throw new SailoException("Otsakerivi tiedostossa " + tiedosto + "ei ole muodossa: \"" + otsake + "\"");
                    i++;
                    continue;
                }
                try {
                    ((AlkioInterface) objekti).parse(line);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException | SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    @Override
    public void tallennaTiedostoon(Object sailoObjekti, String tiedosto, String tmpTiedosto,
            String otsake) throws SailoException {
        if (!getMuutettu()) return;
        File tmp = new File(tmpTiedosto);
        File tuloksetFile = new File(tiedosto);
        try ( PrintWriter fo = new PrintWriter(new FileWriter(tmp.getCanonicalPath())) ) {
            fo.println(otsake);
            for (Object objekti : ((SailoInterface) sailoObjekti).getList()) {
                if (objekti != null)
                    fo.println(objekti.toString());
            }
            tuloksetFile.delete();
            tmp.renameTo(tuloksetFile);
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + tmp.getName() + " ei aukea", e);
        } catch ( IOException e ) {
            throw new SailoException("Tiedoston " + tmp.getName() + " kirjoittamisessa ongelmia", e);
        }
        setMuutettu(true);
    }


    @Override
    public void setMuutettu(boolean muutettu) {
        this.muutettu = muutettu;
    }


    @Override
    public boolean getMuutettu() {
        return muutettu;
    }
}
