package bigBang.definitions.shared;

public class Exercise
	extends SearchResult
{
	private static final long serialVersionUID = 1L;

	public String label;
	public String startDate;
	public String endDate;

	public String tempExerciseId; // Temporary ID for an exercise in a policy scratch pad

	public Exercise()
	{
		tempExerciseId = null;
	}
}
