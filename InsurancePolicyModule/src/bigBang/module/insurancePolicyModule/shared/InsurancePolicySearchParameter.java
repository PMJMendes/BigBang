package bigBang.module.insurancePolicyModule.shared;

import java.io.Serializable;

import bigBang.definitions.client.dataAccess.SearchParameter;

public class InsurancePolicySearchParameter extends SearchParameter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String ownerId;

}
