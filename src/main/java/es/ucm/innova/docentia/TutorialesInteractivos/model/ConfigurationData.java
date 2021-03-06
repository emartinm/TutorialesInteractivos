/* Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;

/**
 * Created by kike on 3/02/17.
 */

/*
* Clase para almacenar la Configuración básica de la aplicación: directorio de temas, compiladores, etc.
* Es un mapa String -> String
*/
public class ConfigurationData {
    private final String DIR_TEMAS = "DIR_TEMAS";
    Map<String, String> prefs;

    public ConfigurationData() {
        prefs = new HashMap<String, String>();
    }

    public ConfigurationData(Map<String, String> p ) {
        this.prefs = p;
    }

    public void save() {
        Preferences storedPrefs = Preferences.userNodeForPackage(this.getClass());
        try {
            storedPrefs.clear(); // Borra entradas obsoletas
        } catch (BackingStoreException e) {
            Controller.log.info( "Unable to clear stored configuration data: " + e);
        }
        Controller.log.info( "Storing configuration data using Java Preference API");
        for (String k : prefs.keySet() ) {
            storedPrefs.put(k, prefs.get(k) );
        }
    }

    public void load() {
        Controller.log.info( "Loading configuration data using Java Preference API");
        Preferences storedPrefs = Preferences.userNodeForPackage(this.getClass());
        try {
            for (String k : storedPrefs.keys()) {
                set(k, storedPrefs.get(k, null));
            }
        } catch (BackingStoreException e) {
            Controller.log.info( "Unable to load configuration data: " + e);
        }
    }

    public String get(String clave) {
        return prefs.getOrDefault(clave, null);
    }

    public void set(String clave, String valor) {
        Controller.log.info( "Storing:  " + clave + " -> " + valor);
        prefs.put(clave, valor);
    }

    public void setDirTemas(String path) {
        this.set(DIR_TEMAS, path);
    }

    public String getDirTemas() {
        return this.get(DIR_TEMAS);
    }

    public boolean isDirTemas() {
        return (this.get(DIR_TEMAS) != null);
    }

    public void delete(String clave) {
        prefs.remove(clave);
    }

    public Set<String> keys() {
        return prefs.keySet();
    }

    public void clear() {
        prefs.clear();
    }
}
