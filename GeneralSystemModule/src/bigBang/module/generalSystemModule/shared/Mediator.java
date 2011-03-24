package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

import bigBang.library.shared.Address;

public class Mediator implements Serializable {

	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String ISPNumber;
	public String taxNumber;
	public String NIB;
	public ComissionProfile comissionProfile;
	public Address address; //includes zip code
	
}
