package bigBang.definitions.shared;

public class InsurancePolicy
	extends InsurancePolicyStub
{
	private static final long serialVersionUID = 1L;

	public String managerId;
	public String insuranceAgencyId;
	public String startDate;
	public String durationId;
	public String fractioningId;
	public int maturityDay;
	public int maturityMonth; //1 to 12
	public String expirationDate;
	public String notes;
	public String mediatorId;
	public boolean caseStudy;
	public Contact[] contacts;
	public Document[] documents;

	public InsurancePolicy()
	{
		contacts = new Contact[0];
		documents = new Document[0];
	}
}
