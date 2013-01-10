package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.FormValidator;
import bigBang.module.clientModule.shared.ModuleConstants;

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
		form.taxNumber.setWarning((form.clientType.getValue() == null) || (form.taxNumber.getValue() == null) || form.taxNumber.getValue().isEmpty() ||
				!specialValidateTaxNumberFormat());
		return validateString(form.taxNumber, 0, 250, true);
	}

	private boolean validateAddress() {
		form.address.setWarning((form.address.getValue() == null) || (form.address.getValue().zipCode == null) ||
				(form.address.getValue().zipCode.code == null) || form.address.getValue().zipCode.code.isEmpty());
		return validateAddress(form.address, false);
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
		return validateGuid(form.otherClientType,
				!ModuleConstants.ClientTypeIDs.Other.equalsIgnoreCase(form.clientType.getValue()));
	}

	private boolean validateClientType() {
		return validateString(form.clientType, 1, 250, false);
	}

	private boolean specialValidateTaxNumberFormat() {
		String nif;
		String type;
		String subType;
		char c;
		int checkDigit;
		int i;

		nif = form.taxNumber.getValue();
		type = form.clientType.getValue();
		subType = form.otherClientType.getValue();

		if (nif != null)
		{
			if (nif.matches("([0-9]*)") && nif.length() == 9)
			{
				c = nif.charAt(0);
				if ( ((c == '1' || c == '2') && ModuleConstants.ClientTypeIDs.Person.equalsIgnoreCase(type)) ||
						((c == '5' || c == '6') && !ModuleConstants.ClientTypeIDs.Person.equalsIgnoreCase(type)) ||
						((c == '9') && ModuleConstants.ClientTypeIDs.Other.equalsIgnoreCase(type) &&
								ModuleConstants.ClientSubTypeIDs.Condo.equalsIgnoreCase(subType)) ) 
				{
					checkDigit = Character.digit(c, 10) * 9;
					for ( i = 2; i <= 8; i++ )
						checkDigit += (Character.digit(nif.charAt(i - 1), 10) * (10 - i));
					checkDigit = 11 - (checkDigit % 11);
					if ( checkDigit >= 10 )
						checkDigit = 0;
					if ( checkDigit == Character.digit(nif.charAt(8), 10) )
						return true;
				}
			}
		}

		return false;
	}

}
