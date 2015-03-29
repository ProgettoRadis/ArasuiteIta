package tico.imageGallery.dataBase;

public class TIGConcept {

	String word;
	String noaccent;
	String language;
	String type;
	
	
	public TIGConcept(String word, String noaccent, String language,
			String type) {
		super();
		this.word = word;
		this.noaccent = noaccent;
		this.language = language;
		this.type = type;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public String getNoaccent() {
		return noaccent;
	}


	public void setNoaccent(String noaccent) {
		this.noaccent = noaccent;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
}
