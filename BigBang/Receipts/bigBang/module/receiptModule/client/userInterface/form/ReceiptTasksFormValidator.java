package bigBang.module.receiptModule.client.userInterface.form;

public class ReceiptTasksFormValidator extends ReceiptFormValidator {

	public ReceiptTasksFormValidator(ReceiptTasksForm form) {
		super(form);
		
	}
	
	@Override
	public boolean validateDescription(){
		return validateString(form.description, 1, 250, false);	
	}

}
