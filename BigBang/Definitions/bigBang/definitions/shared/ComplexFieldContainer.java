package bigBang.definitions.shared;

public class ComplexFieldContainer
	extends FieldContainer
{
	private static final long serialVersionUID = 1L;

	public static class ExerciseData
		extends FieldContainer
	{
		private static final long serialVersionUID = 1L;

		public boolean isActive;
	
		public String label;
		public String startDate;
		public String endDate;
	}
	
	public boolean hasExercises;

	public ExerciseData[] exerciseData;
}
