package bigBang.definitions.shared;

public class ExpenseStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String number; //Gerado pelo servidor
	public String clientId;
	public String clientNumber;
	public String clientName;
	public String referenceId; //ID da apólice ou sub-apólice activada, não pode ser null
	public String referenceTypeId; //Apólice ou Sub-Apólice, não pode ser null
	public String referenceNumber; //Número da (sub)apólice, dado pelo servidor no round trip
	public String expenseDate;
	public String insuredObjectId; //Lista tipificada dos objectos da (sub)apólice, filtrada pelo ID da respectiva
	public String insuredObjectName;
	public String coverageId; //Lista tipificada das coberturas da (sub)apólice
	public String coverageName;
	public String value; //Em €
	public boolean isOpen;
}
