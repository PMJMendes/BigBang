package bigBang.definitions.shared;

public class QuoteRequestStub
	extends GlobalFieldContainer
{
	private static final long serialVersionUID = 1L;
	
	public String processNumber;
	public String clientId;
	public String clientNumber;
	public String clientName;
	public boolean caseStudy;
	public boolean isOpen;

	public QuoteRequestStub()
	{
	}

	public QuoteRequestStub(QuoteRequestStub orig)
	{
		super(orig);

		this.processNumber = orig.processNumber;
		this.clientId = orig.clientId;
		this.clientNumber = orig.clientNumber;
		this.clientName = orig.clientName;
		this.caseStudy = orig.caseStudy;
		this.isOpen = orig.isOpen;
	}
}
