package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.FormValidator;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.form.ContactForm.ContactEntry;

public class ContactFormValidator extends FormValidator<ContactForm> {

	public ContactFormValidator(ContactForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateType();
		valid &= validateAddress();
		valid &= validateInfoList();

		return new Result(valid, this.validationMessages);		
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateType() {
		return validateGuid(form.type, false);
	}

	private boolean validateAddress() {
		return validateAddress(form.address, true);
	}

	private boolean validateInfoList() {
		boolean valid = true;
		for(ListEntry<ContactInfo> entry : form.contactIL) {
			ContactEntry contactEntry = (ContactEntry) entry;
			if(contactEntry.type != null) {
				valid &= validateGuid(contactEntry.type, false);
			}
			if(contactEntry.infoValue != null) {
				valid &= validateString(contactEntry.infoValue, 1, 250, false);
			}
		}
		return valid;
	}

}
