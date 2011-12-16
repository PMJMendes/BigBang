package bigBang.definitions.shared;

import java.io.Serializable;

import bigBang.definitions.shared.InsurancePolicy.FieldType;

public class Exercise
	extends ExerciseStub
{
	private static final long serialVersionUID = 1L;

	public static class InsuredObject
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id;
		public String tempObjectId;
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
			public int columnIndex; // Informacional. Se -1, não está na tabela. Se 0 ou mais, na página identificada por tempExerciseId

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

				public int objectIndex; // Isto indexa o array objects
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

	public InsuredObject[] objects;
	public HeaderData headerData;
	public CoverageData[] coverageData;
}
