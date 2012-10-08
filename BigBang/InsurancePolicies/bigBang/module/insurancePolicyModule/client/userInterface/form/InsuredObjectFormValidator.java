package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.library.client.FormValidator;

public class InsuredObjectFormValidator extends
		FormValidator<InsuredObjectForm> {

	public InsuredObjectFormValidator(InsuredObjectForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		//common
		valid &= validateIdentification();
		valid &= validateAddress();
		valid &= validateInclusionDate();
		valid &= validateExclusionDate();
		valid &= validateInclusionAndExclusionDate();
		
		//person
		valid &= validatePersonTaxNumber();
		valid &= validatePersonGender();
		valid &= validatePersonBirthDate();
		valid &= validatePersonClientNumber();
		valid &= validatePersonCompanyNumber();
		
		//company
		valid &= validateCompanyTaxNumber();
		valid &= validateCompanyCae();
		valid &= validateCompanyGrievousCae();
		valid &= validateCompanyActivityNotes();
		valid &= validateCompanyProductNotes();
		valid &= validateCompanyBusinessNotes();
		valid &= validateCompanyEUEntity();
		valid &= validateCompanyClientNumber();
		
		//equipment
		valid &= validateEquipmentMakeAndModel();
		valid &= validateEquipmentDescription();
		valid &= validateEquipmentFirstRegistryDate();
		valid &= validateEquipmentManufactureYear();
		valid &= validateEquipmentClientInternalId();
		valid &= validateEquipmentInsuranceCompanyInternalVehicleId();
		
		//location
		valid &= validateLocationDescription();
		
		//animal
		valid &= validateAnimalSpecies();
		valid &= validateAnimalRace();
		valid &= validateAnimalBirthYear();
		valid &= validateAnimalCityRegistryNumber();
		valid &= validateAnimalElectronicTagId();
				
		return new Result(valid, this.validationMessages);
	}

	private boolean validateIdentification() {
		return validateString(form.identification, 1, 250, false);
	}

	private boolean validateAddress() {
		return validateAddress(form.address, true);
	}

	private boolean validateInclusionDate() {
		return validateDate(form.inclusionDate, true);
	}

	private boolean validateExclusionDate() {
		return validateDate(form.exclusionDate, true);
	}
	
	private boolean validateInclusionAndExclusionDate(){
		boolean validDates = validateInclusionDate() && validateExclusionDate();
		if(validDates) {
			Date inclusionDate = form.inclusionDate.getValue();
			Date exclusionDate = form.exclusionDate.getValue();
			if(inclusionDate != null && exclusionDate != null) {
				if(inclusionDate.before(exclusionDate)) {
					return true;
				}else{
					form.inclusionDate.setInvalid(true);
					form.exclusionDate.setInvalid(true);
					return false;
				}
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	private boolean validatePersonTaxNumber() {
		return validateString(form.personTaxNumber, 0, 250, true);
	}

	private boolean validatePersonGender() {
		return validateGuid(form.personGender, true);
	}

	private boolean validatePersonBirthDate() {
		return validateDate(form.personBirthDate, true);
	}

	private boolean validatePersonClientNumber() {
		return validateString(form.personClientNumber, 0, 250, true);
	}

	private boolean validatePersonCompanyNumber() {
		return validateString(form.personCompanyNumber, 0, 250, true);
	}

	private boolean validateCompanyTaxNumber() {
		return validateString(form.companyTaxNumber, 0, 250, true);	
	}

	private boolean validateCompanyCae() {
		return validateGuid(form.companyCAE, true);
	}

	private boolean validateCompanyGrievousCae() {
		return validateGuid(form.companyGrievousCAE, true);
	}

	private boolean validateCompanyActivityNotes() {
		return validateString(form.companyActivityNotes, 0, 250, true);	
	}

	private boolean validateCompanyProductNotes() {
		return validateString(form.companyProductNotes, 0, 250, true);	

	}

	private boolean validateCompanyBusinessNotes() {
		return validateGuid(form.companyBusinessVolume, true);	

	}

	private boolean validateCompanyEUEntity() {
		return validateString(form.companyEuropeanUnionEntity, 0, 250, true);	
	}

	private boolean validateCompanyClientNumber() {
		return validateString(form.companyClientNumber, 0, 250, true);	
	}

	private boolean validateEquipmentMakeAndModel() {
		return validateString(form.equipmentMakeAndModel, 0, 250, true);	
	}

	private boolean validateEquipmentDescription() {
		return validateString(form.equipmentDescription, 0, 250, true);
	}

	private boolean validateEquipmentFirstRegistryDate() {
		return validateDate(form.equipmentFirstRegistryDate, true);
	}

	private boolean validateEquipmentManufactureYear() {
		return validateString(form.equipmentManufactureYear, 0, 4, true);
	}

	private boolean validateEquipmentClientInternalId() {
		return validateString(form.equipmentClientInternalId, 0, 250, true);
	}

	private boolean validateEquipmentInsuranceCompanyInternalVehicleId() {
		return validateString(form.equipmentInsuranceCompanyInternalVehicleId, 0, 250, true);
	}

	private boolean validateLocationDescription() {
		return validateString(form.locationDescription, 0, 250, true);
	}

	private boolean validateAnimalSpecies() {
		return validateString(form.animalSpecies, 0, 250, true);
	}

	private boolean validateAnimalRace() {
		return validateString(form.animalRace, 0, 250, true);
	}

	private boolean validateAnimalBirthYear() {
		return validateString(form.animalBirthYear, 0, 4, true);
	}

	private boolean validateAnimalCityRegistryNumber() {
		return validateString(form.animalCityRegistryNumber, 0, 250, true);
	}

	private boolean validateAnimalElectronicTagId() {
		return validateString(form.animalElectronicTagId, 0, 250, true);
	}

}
