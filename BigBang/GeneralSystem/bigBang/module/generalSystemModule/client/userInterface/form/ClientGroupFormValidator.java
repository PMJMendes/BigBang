package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ClientGroupFormValidator extends FormValidator<ClientGroupForm> {

	public ClientGroupFormValidator(ClientGroupForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateParentGroup();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateParentGroup(){
		return validateGuid(form.parentGroup, true);
	}

}
