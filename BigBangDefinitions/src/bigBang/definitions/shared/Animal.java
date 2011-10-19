package bigBang.definitions.shared;

import java.io.Serializable;

public class Animal extends InsuredObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String species;
	public String race;
	public int age;
	public String registrationNumber;

}
