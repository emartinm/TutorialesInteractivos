package TFG.TutorialesInteractivos.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Language que se está utilizando actualmente
 * 
 * @author Carlos, Rafa
 *
 */

public class Language {
	private final SimpleStringProperty language;
	private final SimpleStringProperty path;

	public Language() {
		this.language = null;
		this.path = null;
	}

	public Language(String language, String path) {
		this.language = new SimpleStringProperty(language);
		this.path = new SimpleStringProperty(path);
	}

	public String getLanguage() {
		return language.get();
	}

	public String getPath() {
		return path.get();
	}

	public void setLanguage(String lang) {
		this.language.set(lang);
	}

	public void setPath(String path) {
		this.path.set(path);
	}

}