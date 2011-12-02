package bigBang.definitions.shared;

import java.io.Serializable;

import bigBang.definitions.shared.InsurancePolicy.FieldType;

public class InsuredObject
	extends SearchResult
{
	private static final long serialVersionUID = 1L;

	public static class Exercise
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		String id;
		String tempExerciseId;
		String label;
	}

	public static class CoverageData
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
			extends FixedField
		{
			private static final long serialVersionUID = 1L;

			public String fieldId;
			public String fieldName;
			public FieldType type;
			public String unitsLabel;
			public String refersToId;
			public int columnIndex; // Informacional. Se 0 ou mais, na página identificada por tempObjectId e tempExerciseId

			public String[] data; // Vêm pela mesma ordem que os exercícios no array exercises abaixo
		}

		String coverageId;
		FixedField[] fixedFields;
		VariableField[] variableFields;
	}

	public String unitIdentification;
	public Address address;
	public String inclusionDate;
	public String exclusionDate;

	Exercise[] exercises;
	CoverageData[] coverageData;

	public String tempObjectId; // Temporary ID for an insured object in a policy scratch pad

	public InsuredObject()
	{
		tempObjectId = null;
	}
}
