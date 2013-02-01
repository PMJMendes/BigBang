package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;
import bigBang.library.client.userInterface.form.DocumentForm.DocumentDetailEntry;

public class DocumentFormValidator extends FormValidator<DocumentForm> {

	public DocumentFormValidator(DocumentForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateType();
		valid &= validateInfoList();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateType() {
		return validateGuid(form.docType, false);
	}

	private boolean validateInfoList() {
		boolean valid = true;
		for(int i = 0; i< form.details.size()-1; i++) {//O ÚLTIMO É SEMPRE O BOTÃO DE ADICIONAR
			DocumentDetailEntry documentEntry = (DocumentDetailEntry) form.details.get(i);
			valid &= validateString(documentEntry.info, 1, 250, false);
			valid &= validateString(documentEntry.infoValue, 1, 250, false);
		}
		return valid;
	}


}
