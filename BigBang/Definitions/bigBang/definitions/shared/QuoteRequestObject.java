package bigBang.definitions.shared;

public class QuoteRequestObject
	extends QuoteRequestObjectStub
{
	private static final long serialVersionUID = 1L;
	
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

	public QuoteRequestObject()
	{
	}

	public QuoteRequestObject(QuoteRequestObject orig)
	{
		super(orig);

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
	}

	public QuoteRequestObject(QuoteRequestObject data, QuoteRequestObject struct)
	{
		super(data, struct);

		this.taxNumberPerson = data.taxNumberPerson;
		this.genderId = data.genderId;
		this.birthDate = data.birthDate;
		this.clientNumberPerson = data.clientNumberPerson;
		this.insuranceCompanyInternalIdPerson = data.insuranceCompanyInternalIdPerson;
		this.taxNumberCompany = data.taxNumberCompany;
		this.caeId = data.caeId;
		this.grievousCaeId = data.grievousCaeId;
		this.activityNotes = data.activityNotes;
		this.productNotes = data.productNotes;
		this.businessVolumeId = data.businessVolumeId;
		this.europeanUnionEntity = data.europeanUnionEntity;
		this.clientNumberGroup = data.clientNumberGroup;
		this.makeAndModel = data.makeAndModel;
		this.equipmentDescription = data.equipmentDescription;
		this.firstRegistryDate = data.firstRegistryDate;
		this.manufactureYear = data.manufactureYear;
		this.clientInternalId = data.clientInternalId;
		this.insuranceCompanyInternalIdVehicle = data.insuranceCompanyInternalIdVehicle;
		this.siteDescription = data.siteDescription;
		this.species = data.species;
		this.race = data.race;
		this.birthYear = data.birthYear;
		this.cityRegistryNumber = data.cityRegistryNumber;
		this.electronicIdTag = data.electronicIdTag;
	}
}
