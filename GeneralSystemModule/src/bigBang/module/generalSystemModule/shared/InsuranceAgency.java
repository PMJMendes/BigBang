package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

import bigBang.library.shared.Address;

public class InsuranceAgency implements Serializable {

	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String acronym; 
	public String ISPNumber;
	public String ownMediatorCode;
	public String taxNumber;
	public String NIB;
	public Address address; //includes zip code
	
}
