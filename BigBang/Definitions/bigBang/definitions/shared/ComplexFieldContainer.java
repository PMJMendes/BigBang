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

		public ExerciseData()
		{
		}

		public ExerciseData(ExerciseData orig)
		{
			super(orig);
			this.label = orig.label;
			this.startDate = orig.startDate;
			this.endDate = orig.endDate;
		}
	}

	public boolean hasExercises;

	public ExerciseData[] exerciseData;

	public ComplexFieldContainer()
	{
	}

	public ComplexFieldContainer(ComplexFieldContainer orig)
	{
		super(orig);

		int i;

		this.hasExercises = orig.hasExercises;

		if ( orig.exerciseData == null )
			this.exerciseData = null;
		else
		{
			this.exerciseData = new ExerciseData[orig.exerciseData.length];
			for ( i = 0; i < this.exerciseData.length; i++ )
				this.exerciseData[i] = (orig.exerciseData[i] == null ? null : new ExerciseData(orig.exerciseData[i]));
		}
	}
}
