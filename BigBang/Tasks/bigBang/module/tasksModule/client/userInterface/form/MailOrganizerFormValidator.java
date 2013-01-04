package bigBang.module.tasksModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormValidator;
import bigBang.module.tasksModule.client.userInterface.form.MailOrganizerForm.DocumentDetailEntry;

public class MailOrganizerFormValidator extends
		FormValidator<MailOrganizerForm> {

	public MailOrganizerFormValidator(MailOrganizerForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateType();
		valid &= validateInfoList();
		valid &= validateReferenceType();
		valid &= validateReference();
		
		return new Result(valid, this.validationMessages);
	}
	
	@SuppressWarnings("unchecked")
	private boolean validateReference() {

		if(form.referenceType.getValue() == null){
			return true;
		}
		
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_AGENCY)){
			return validateGuid(form.references[0], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.MEDIATOR)){
			return validateGuid(form.references[1], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			return validateGuid(form.references[2], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			return validateGuid(form.references[3], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			return validateGuid(form.references[4], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
			return validateGuid(form.references[5], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
			return validateGuid(form.references[6], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			return validateGuid(form.references[7], false);
		}
		if(form.referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
			return validateGuid(form.references[8], false);
		}
		
		return true;
	
	}

	private boolean validateReferenceType() {
		return validateGuid(form.referenceType, false);
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
