package bigBang.definitions.shared;

public class InsuredObject
	extends InsuredObjectStub
{
	private static final long serialVersionUID = 1L;

	public boolean deleteThis;

	public String taxNumberPerson;
	public String genderId;
	public String birthDate;
	public String clientNumberPerson; // Only if the insured person is also a first level client
	public String insuranceCompanyInternalIdPerson;

	public String taxNumberCompany;
	public String caeId;
	public String grievousCaeId;
	public String activityNotes;
	public String productNotes;
	public String businessVolumeId;
	public String europeanUnionEntity;
	public String clientNumberGroup; // Only if the insured company is also a first level client

	public String makeAndModel;
	public String equipmentDescription;
	public String firstRegistryDate;
	public String manufactureYear;
	public String clientInternalId;
	public String insuranceCompanyInternalIdVehicle;

	public String siteDescription;

	public String species;
	public String race;
	public String birthYear;
	public String cityRegistryNumber;
	public String electronicIdTag;

	public InsuredObject()
	{
		deleteThis = false;

		headerFields = new HeaderField[0];
		columnFields = new ColumnField[0];
		extraFields = new ExtraField[0];
		exerciseData = new ExerciseData[0];
	}
}
