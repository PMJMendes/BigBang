package bigBang.definitions.shared;

public class SubCasualtyStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String number; //Gerado pelo servidor
	public String casualtyId;
	public String referenceId; //ID da apólice ou sub-apólice activada, pode ser null se ainda não estiver identificada
	public String referenceTypeId; //Apólice ou Sub-Apólice, pode ser null, mas só se o referenceId também fôr null
	public String referenceNumber; //Número da (sub)apólice, dado pelo servidor no round trip
	public String categoryName;
	public String lineName;
	public String subLineName;
	public String insurerProcessNumber; //Pode ser null, enquanto não se souber o número
	public boolean isOpen;
	public String totalDamages; //Em €
}
