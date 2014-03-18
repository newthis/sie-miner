package sie.db.entity;

import java.util.Set;

/**
 * Classe che rappresenta la generica risorsa modificata in un commit
 * */

public class ChangedResource {

	public ChangedResource() {
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Se la risorsa modificata è un file sorgente restituisce la firma del
	 * metodo che è stato modificato.
	 * 
	 * @return la firma del metodo.
	 * */
	public Set<String> getModifiedMethods() {
		return modifiedMethods;
	}

	/**
	 * Se la risorsa modificata è un file sorgente, salva la firma del metodo che è stato modificato.
	 * @param la firma del metodo.
	 * */
	public void setModifiedMethods(Set<String> methods) {
		this.modifiedMethods = methods;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		String mets = "";
		for(String met : modifiedMethods)
			mets += met + " : ";
		return fileName + " -> " + mets;
	}

	private int id;
	private String fileName;
	private Set<String> modifiedMethods;
}
