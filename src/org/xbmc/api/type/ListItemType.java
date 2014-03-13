package org.xbmc.api.type;

import java.io.Serializable;

public class ListItemType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -408608511442224768L;

	private String name; 
	private int id ;
	
	public ListItemType(String name, int id) {
		this.setName(name) ;
		this.setId(id) ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
