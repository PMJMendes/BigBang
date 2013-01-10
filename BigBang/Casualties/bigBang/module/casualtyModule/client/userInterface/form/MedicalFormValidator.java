package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class MedicalFormValidator extends FormValidator<MedicalFileForm> {

	public MedicalFormValidator(MedicalFileForm form) {
		super(form);
	}

	@Override
	protected Result validateImpl() {
		boolean valid = true;
		
		valid &= validateDateNextAppointment();
		valid &= validateDetails();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateDateNextAppointment() {
		return validateDate(form.nextAppointment, true);
	}

	private boolean validateDetails() {
		boolean valid = true;
		
		for(MedicalDetailItemSection section : form.medicalDetailItemSections){
			valid &= validateDetailSection(section);
		}
		
		return valid;
	}

	private boolean validateDetailSection(MedicalDetailItemSection section) {
		boolean valid = true;
		
		valid &= validateDate(section.startDate, false);
		valid &= validateGuid(section.disabilityType, false);
		valid &= validateString(section.disabilityLocation, 0, 250, true);
		valid &= validateNumber(section.disabilityPercent, true);
		valid &= validateDate(section.endDate, true);
		valid &= validateNumber(section.benefits,false);
		
		return valid;
	}

}
