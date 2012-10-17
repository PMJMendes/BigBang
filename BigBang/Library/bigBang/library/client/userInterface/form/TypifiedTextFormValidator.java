package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class TypifiedTextFormValidator extends FormValidator<TypifiedTextForm> {

	public TypifiedTextFormValidator(TypifiedTextForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateLabel();
		valid &= validateSubject();
		valid &= validateText();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateLabel() {
		return validateString(form.label, 1, 250, false);
	}

	private boolean validateSubject() {
		return validateString(form.subject, 0, 250, true);
	}

	private boolean validateText() {
		return validateString(form.textBody, 0, 250, true);
	}

}
