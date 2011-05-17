package bigBang.module.clientModule.shared;

import bigBang.library.shared.Address;

public class Client
	extends ClientStub
{
	private static final long serialVersionUID = 1L;

	public static enum ClientType
	{
		INDIVIDUAL,
		COMPANY,
		CONDOMINIUM
	}

	public String taxNumber;
	public Address address;
	public ClientGroupStub group;
	public String NIB;
	public ClientType type;
	public String mediatorId;
	public String managerId;
	public String operationalProfileId;
	public String email;
	public String caeId;
	public String activityNotes;
	public String revenueId;
	public String birthDate;
	public String genderId;
	public String maritalStatusId;
	public String professionId;
	public String notes;
}
