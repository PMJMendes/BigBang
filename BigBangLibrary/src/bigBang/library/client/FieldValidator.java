package bigBang.library.client;

public interface FieldValidator<T> {
		
	public boolean isValid(T value);
	
	public String getErrorMessage();
	
	public boolean isMandatory();

}
