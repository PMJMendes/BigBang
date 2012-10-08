package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.DocInfo;
import bigBang.library.client.FormValidator;
import bigBang.library.client.userInterface.ListEntry;
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
		for(ListEntry<DocInfo> entry : form.details) {
			DocumentDetailEntry documentEntry = (DocumentDetailEntry) entry;
			valid &= validateGuid(documentEntry.info, false);
			valid &= validateString(documentEntry.infoValue, 1, 250, false);
		}
		return valid;
	}

	
}
