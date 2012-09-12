package bigBang.definitions.shared;

public class InsuredObject
	extends InsuredObjectStub
{
	private static final long serialVersionUID = 1L;

	public static enum Change
	{
		CREATED,
		MODIFIED,
		DELETED
	}
	
	public String categoryName;
	public String lineName;
	public String subLineName;

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
	
	public Change change;

	public InsuredObject()
	{
		headerFields = new HeaderField[0];
		columnFields = new ColumnField[0];
		extraFields = new ExtraField[0];
		exerciseData = new ExerciseData[0];
	}

	public InsuredObject(InsuredObject orig)
	{
		super(orig);

		this.categoryName = orig.categoryName;
		this.lineName = orig.lineName;
		this.subLineName = orig.subLineName;
		this.taxNumberPerson = orig.taxNumberPerson;
		this.genderId = orig.genderId;
		this.birthDate = orig.birthDate;
		this.clientNumberPerson = orig.clientNumberPerson;
		this.insuranceCompanyInternalIdPerson = orig.insuranceCompanyInternalIdPerson;
		this.taxNumberCompany = orig.taxNumberCompany;
		this.caeId = orig.caeId;
		this.grievousCaeId = orig.grievousCaeId;
		this.activityNotes = orig.activityNotes;
		this.productNotes = orig.productNotes;
		this.businessVolumeId = orig.businessVolumeId;
		this.europeanUnionEntity = orig.europeanUnionEntity;
		this.clientNumberGroup = orig.clientNumberGroup;
		this.makeAndModel = orig.makeAndModel;
		this.equipmentDescription = orig.equipmentDescription;
		this.firstRegistryDate = orig.firstRegistryDate;
		this.manufactureYear = orig.manufactureYear;
		this.clientInternalId = orig.clientInternalId;
		this.insuranceCompanyInternalIdVehicle = orig.insuranceCompanyInternalIdVehicle;
		this.siteDescription = orig.siteDescription;
		this.species = orig.species;
		this.race = orig.race;
		this.birthYear = orig.birthYear;
		this.cityRegistryNumber = orig.cityRegistryNumber;
		this.electronicIdTag = orig.electronicIdTag;
		this.change = orig.change;
	}
}
