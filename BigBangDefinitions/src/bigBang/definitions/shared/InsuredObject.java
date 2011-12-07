package bigBang.definitions.shared;

import java.io.Serializable;

import bigBang.definitions.shared.InsurancePolicy.FieldType;

public class InsuredObject
	extends InsuredObjectStub
{
	private static final long serialVersionUID = 1L;

	public static class Exercise
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id;
		public String tempExerciseId;
		public String label;
	}

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

		public static class VariableField
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public static class VariableValue
				implements Serializable
			{
				private static final long serialVersionUID = 1L;

				public int exerciseIndex; // Isto indexa o array exercises
				public String value;
			}

			public String fieldId;
			public String fieldName;
			public FieldType type;
			public String unitsLabel;
			public String refersToId;
			public int columnIndex; // Informacional. Se 0 ou mais, na página identificada por tempObjectId e tempExerciseId

			public VariableValue[] data;
		}

		public FixedField[] fixedFields;
		public VariableField[] variableFields;
	}

	public static class CoverageData
		extends HeaderData
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
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
	public String businessVolume;
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
	public String age;
	public String cityRegistryNumber;
	public String electronicIdTag;

	public Exercise[] exercises;
	public HeaderData headerData;
	public CoverageData[] coverageData;
}
