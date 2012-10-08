package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ClientFormValidator extends FormValidator<ClientForm> {

	public ClientFormValidator(ClientForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateTaxNumber();
		valid &= validateAddress();
		valid &= validateGroup();
		valid &= validateNib();
		valid &= validateMediator();
		valid &= validateManager();
		valid &= validateProfile();
		valid &= validateCae();
		valid &= validateActivityNotes();
		valid &= validateNumberOfWorkers();
		valid &= validateRevenue();
		valid &= validateBirthDate();
		valid &= validateGender();
		valid &= validateMaritalStatus();
		valid &= validateProfession();
		valid &= validateNotes();
		valid &= validateOtherClientType();
		valid &= validateClientType();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateTaxNumber() {
		return validateString(form.taxNumber, 0, 250, true);
	}

	private boolean validateAddress() {
		return validateAddress(form.address, true);
	}

	private boolean validateGroup() {
		return validateGuid(form.group, true);
	}

	private boolean validateNib() {
		return validateString(form.NIB, 0, 250, true);
	}

	private boolean validateMediator() {
		return validateGuid(form.mediator, false);
	}

	private boolean validateManager() {
		return validateGuid(form.clientManager, false);
	}

	private boolean validateProfile() {
		return validateGuid(form.profile, false);
	}

	private boolean validateCae() {
		return validateGuid(form.CAE, true);
	}

	private boolean validateActivityNotes() {
		return validateString(form.activityObservations, 0, 250, true);
	}

	private boolean validateNumberOfWorkers() {
		return validateGuid(form.numberOfWorkers, true);
	}

	private boolean validateRevenue() {
		return validateGuid(form.revenue, true);
	}

	private boolean validateBirthDate() {
		return validateDate(form.birthDate, true);
	}

	private boolean validateGender() {
		return validateGuid(form.gender, true);
	}

	private boolean validateMaritalStatus() {
		return validateGuid(form.maritalStatus, true);
	}

	private boolean validateProfession() {
		return validateGuid(form.profession, true);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateOtherClientType() {
		return validateGuid(form.otherClientType, true);
	}

	private boolean validateClientType() {
		return validateString(form.clientType, 1, 250, false);
	}

}
