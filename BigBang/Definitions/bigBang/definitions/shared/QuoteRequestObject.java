package bigBang.definitions.shared;

import java.io.Serializable;

import bigBang.definitions.shared.InsurancePolicy.FieldType;

public class QuoteRequestObject
	extends QuoteRequestObjectStub
{
	private static final long serialVersionUID = 1L;

	public static class HeaderData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static class FixedField
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public String fieldId;
			public String fieldName;
			public FieldType type;
			public String unitsLabel;
			public String refersToId;
			public int columnIndex; // Informacional. Se -1, não está na tabela. Se 0 ou mais, na página identificada por tempObjectId

			public String value;
		}

		public FixedField[] fixedFields;
	}

	public static class CoverageData
		extends HeaderData
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
		public String coverageLabel;
	}

	public static class SubLineData
	{
		public String subLineId;
		public String headerText;
		public HeaderData headerData;
		public CoverageData[] coverageData;
	}

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

	public SubLineData[] objectData;
}
