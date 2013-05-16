package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class TransferCasualtyManagerFormValidator extends
		FormValidator<TransferCasualtyManagerForm> {

	public TransferCasualtyManagerFormValidator(TransferCasualtyManagerForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateManager();
		
		return new Result(valid, this.validationMessages);
		
	}

	private boolean validateManager() {
		return validateGuid(form.manager, false);
	}

}
