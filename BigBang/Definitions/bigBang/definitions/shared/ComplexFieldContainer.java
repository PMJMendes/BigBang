package bigBang.definitions.shared;

public class ComplexFieldContainer
	extends FieldContainer
{
	private static final long serialVersionUID = 1L;

	public static class ExerciseData
		extends FieldContainer
	{
		private static final long serialVersionUID = 1L;

		public String label;
		public String startDate;
		public String endDate;
	}
	
	public ExerciseData[] exerciseData;
}
