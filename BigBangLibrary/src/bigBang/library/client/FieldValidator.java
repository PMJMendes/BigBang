package bigBang.library.client;

public interface FieldValidator {
		
	public <T> boolean isValid(T value);
	
	public String getErrorMessage();
	
	public boolean isMandatory();

}
