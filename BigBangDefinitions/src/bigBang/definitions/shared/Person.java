package bigBang.definitions.shared;

import java.io.Serializable;

public class Person extends InsuredObject implements Serializable {

	private static final long serialVersionUID = 1L;
 
	public String taxNumber;
	public String genderId;
	public String clientNumber; //NULL if not a client
	public String birthDate;
	public String insuranceAgencyInternalNumber;
	
}
