package es.ucm.innova.docentia.TutorialesInteractivos.view;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Muestra un FileChooser para seleccionar el path del compilador
 * 
 * @author Carlos, Rafa
 *
 */
public class PathChooser 
{
	private String path;
	
	public PathChooser(Stage stage, String lenguaje){
		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(stage);
		if (f != null) {
			this.path = f.getAbsolutePath();
		}
	}
	
	public PathChooser(Stage stage){
		DirectoryChooser chooser = new DirectoryChooser();
		File f = chooser.showDialog(stage);
		if (f != null) {
			this.path = f.getAbsolutePath();
		}
	}

	
	public String getPath(){
		return this.path;
	}
}
