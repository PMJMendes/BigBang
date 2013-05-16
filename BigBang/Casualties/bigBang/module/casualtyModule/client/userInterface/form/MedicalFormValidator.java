package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;
import bigBang.module.casualtyModule.client.userInterface.AppointmentForm;

public class MedicalFormValidator extends FormValidator<MedicalFileForm> {

	public MedicalFormValidator(MedicalFileForm form) {
		super(form);
	}

	@Override
	protected Result validateImpl() {
		boolean valid = true;

		valid &= validateDateNextAppointment();
		valid &= validateString(form.notes, 0, 250, true);
		valid &= validateDetails();
		valid &= validateAppointments();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateDetails() {
		
		boolean valid = true;
		for(MedicalDetailForm detailForm : form.medicalDetailForms){
			if(detailForm.getNonScrollableContent().isVisible()){
				valid &= detailForm.validate();
			}
		}
		
		return valid;
	}
	
	private boolean validateAppointments(){
		
		boolean valid = true;
		for(AppointmentForm assForm : form.appointmentForms){
			if(assForm.getNonScrollableContent().isVisible()){
				valid &= assForm.validate();
			}
		}
		
		return valid;
		
	}

	private boolean validateDateNextAppointment() {
		return validateDate(form.nextAppointment, true);
	}
}
