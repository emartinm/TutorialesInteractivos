package es.ucm.innova.docentia.TutorialesInteractivos.view;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
		this.path = fc.showOpenDialog(stage).getAbsolutePath();
	}
	
	public PathChooser(Stage stage){
		DirectoryChooser chooser = new DirectoryChooser();
		this.path = chooser.showDialog(stage).getAbsolutePath();
	}
	
	public String getPath(){
		return this.path;
	}
}
