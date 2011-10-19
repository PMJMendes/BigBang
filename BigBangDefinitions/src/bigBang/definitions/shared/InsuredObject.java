package bigBang.definitions.shared;

import java.io.Serializable;

public class InsuredObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public String unitIdentification;
	public Address address;
	public String inclusionDate;
	public String exclusionDate;
	
}
