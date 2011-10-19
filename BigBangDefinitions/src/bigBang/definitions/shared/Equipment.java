package bigBang.definitions.shared;

import java.io.Serializable;

public class Equipment extends InsuredObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String id;
	public String brand;
	public String model;
	public String description;
	public String dateOfFirstRegistration;
	public String dateOfManufacture;
	public String clientNumber;
	public String insuranceAgencyNumber;

}
