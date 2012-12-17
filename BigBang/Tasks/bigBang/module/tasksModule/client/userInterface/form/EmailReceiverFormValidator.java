package bigBang.module.tasksModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class EmailReceiverFormValidator extends
FormValidator<EmailReceiverForm> {

	public EmailReceiverFormValidator(EmailReceiverForm emailReceiverForm) {
		super(emailReceiverForm);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateActiveOrNewSubject();
		valid &= validateReplyLimit();
		valid &= validateType();


		return new Result(valid, validationMessages);
	}

	private boolean validateType() {
		return validateGuid(form.requestType, false);
	}

	@SuppressWarnings("unchecked")
	private boolean validateActiveOrNewSubject() {
		boolean valid = true;

		if(("OLD").equalsIgnoreCase(form.newOrOldSubject.getValue())){
			valid &= validateGuid(form.conversationList, !form.conversationList.isVisible());
		}else{
			valid &= validateGuid(form.referenceType, !form.referenceType.isVisible());

		}
		
		for(int i = 0; i<form.references.length; i++){
			valid &= validateGuid(form.references[i], !form.references[i].isVisible());
		}

		return valid;
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, form.replyLimit.isReadOnly());	}


}
