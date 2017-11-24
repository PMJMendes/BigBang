package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.Message.Kind;
import bigBang.library.client.FormValidator;

public class SendMessageFormValidator extends
FormValidator<SendMessageForm> {

	public SendMessageFormValidator(SendMessageForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;

		valid &= validateConversationInfo();
		valid &= validateMessageInfo();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateConversationInfo() {
		boolean valid = validateGuid(form.requestType, false);
		boolean validConversationSubject = form.subject.getValue() != null && form.subject.getValue().length()>0;
		valid &= validConversationSubject;
		form.subject.setInvalid(!validConversationSubject);
		boolean validExpectsResponse = form.expectsResponse.getValue()!=null && form.expectsResponse.getValue().length()>0;
		valid &= validExpectsResponse;
		form.expectsResponse.setInvalid(!validExpectsResponse);
		if (form.expectsResponse.getValue()!=null && form.expectsResponse.getValue().equals("YES")) {
			valid = validateNumber(form.replyLimit, false);
		}
		return valid;
	}

	private boolean validateMessageInfo() {
		boolean valid = (form.emailOrNote.getValue() != null && form.emailOrNote.getValue().length()>0);
		form.emailOrNote.setInvalid(!valid);
		if (valid && form.emailOrNote.getValue().equals(Kind.EMAIL.toString())) {
			boolean validAddresses = (form.toAddresses.getValue() != null && form.toAddresses.getValue().length()>0) ||
					(form.ccAddresses.getValue() != null && form.ccAddresses.getValue().length()>0) ||
					(form.bccAddresses.getValue() != null && form.bccAddresses.getValue().length()>0);
			valid &= validAddresses;
			form.toAddresses.setInvalid(!validAddresses);
			form.ccAddresses.setInvalid(!validAddresses);
			form.bccAddresses.setInvalid(!validAddresses);
			boolean mailSubjectSet = form.emailSubject.getValue() != null && form.emailSubject.getValue().length()>0;
			valid &= mailSubjectSet;
			form.emailSubject.setInvalid(!mailSubjectSet);
		}
		return valid;
	}

}
