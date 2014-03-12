package org.xbmc.api.object;

import java.io.Serializable;

import org.xbmc.api.object.INamedResource;

public class Addon implements Serializable,INamedResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 765918331060391838L;
	/**
	 * 
	 */
	public String name; 
	public String type ; 
	
	public Addon(String name, String type) {
		this.name = name ; 
		this.type = type ;
	}
	
	public String toString(){
		return name;
	}

	public String getShortName() {
		return "";
	}

	
	
}
