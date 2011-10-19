package bigBang.definitions.shared;

public class InsurancePolicy extends InsurancePolicyStub {

	private static final long serialVersionUID = 1L;

	public String startDate;
	public String durationId;
	public String fractioningId;
	public boolean postponeable;
	public int maturityDay;
	public int maturityMonth; //1 to 12
	public boolean renewable;
	public String expirationDate;

}
