package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

/**
 * Created by kike on 24/02/17.
 */
public class JavaBridge {
    private Controller c;

    public JavaBridge(Controller c) {
        this.c = c;
    }

    public void hello() {
        System.out.println("hello");
    }

    public void show(String s){
        System.out.println(s);
    }

    public void openWebPage(String url) {
        c.log.info("Abriendo página <" + url + "> en navegador");
        c.openWebPage(url);
    }
}