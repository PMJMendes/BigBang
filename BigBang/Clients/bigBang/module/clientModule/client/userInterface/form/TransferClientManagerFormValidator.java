package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class TransferClientManagerFormValidator extends
		FormValidator<TransferClientManagerForm> {

	public TransferClientManagerFormValidator(TransferClientManagerForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateManager();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateManager(){
		return validateGuid(form.manager, false);
	}

}
