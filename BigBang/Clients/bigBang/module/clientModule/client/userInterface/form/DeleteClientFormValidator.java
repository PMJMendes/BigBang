package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class DeleteClientFormValidator extends FormValidator<DeleteClientForm> {

	public DeleteClientFormValidator(DeleteClientForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateMotive();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateMotive() {
		return validateGuid(form.motive, false);
	}

}
