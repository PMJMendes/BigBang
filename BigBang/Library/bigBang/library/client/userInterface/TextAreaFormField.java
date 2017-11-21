package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;

/**
 * This class was needed to allow all classes already using the old TextAreaFormField's class
 * to maintain their behavior, without no change at all. It pretty much extends the 
 * UnlimitedTextAreaFormField's class by adding it a 250 characters' maximum. 
 */
public class TextAreaFormField extends UnlimitedTextAreaFormField {

	public TextAreaFormField(){
		
		super();
		
		setMaxCharacters(250, null);
	}

	public TextAreaFormField(String label) {
		super(label);
	}
	
	public TextAreaFormField(String label,FieldValidator<String> validator){
		super(label, validator);
	}
	
	public TextAreaFormField(FieldValidator<String> validator) {
		super(validator);
	}
}
