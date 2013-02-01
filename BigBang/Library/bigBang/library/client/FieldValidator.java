package bigBang.library.client;

public interface FieldValidator<T> {
		
	final String MANDATORY_FIELD_MESSAGE = "Campo obrigatório";
	final String SIZE_EXCEEDED_MESSAGE = "";
	
	public boolean isValid(T value);
	
	public String getErrorMessage();
	
	public boolean isMandatory();

}
