package bigBang.module.receiptModule.client.userInterface.form;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.FormValidator;

public class ReceiptFormValidator extends FormValidator<ReceiptForm> {

	public ReceiptFormValidator(ReceiptForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateNumber();
		valid &= validateType();
		valid &= validateTotalPremium();
		valid &= validateSalesPremium();
		valid &= validateCommission();
		valid &= validateRetro();
		valid &= validateFat();
		valid &= validateIssueDate();
		valid &= validateCoverageStart();
		valid &= validateCoverageEnd();
		valid &= validateStartAndEndDate();
		valid &= validateDueDate();
		valid &= validateDescription();
		valid &= validateNotes();
		valid &= validateBonusMalusOption();
		valid &= validateBonusMalusValue();
		valid &= validateValues();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateNumber() {
		return validateString(form.number, 1, 250, false);
	}

	private boolean validateType() {
		return validateGuid(form.type, false);
	}

	private boolean validateTotalPremium() {
		return validateNumber(form.totalPremium, false);
	}

	private boolean validateSalesPremium() {
		return validateNumber(form.salesPremium, form.bonusMalusValue.getValue() == null);
	}

	private boolean validateCommission() {
		return validateNumber(form.commission, true);
	}

	private boolean validateRetro() {
		return validateNumber(form.retro, true);
	}

	private boolean validateFat() {
		return validateNumber(form.fat, true);
	}

	private boolean validateIssueDate() {
		return validateDate(form.issueDate, false);
	}

	private boolean validateCoverageStart() {
		if ( !validateDate(form.coverageStart, !
				((BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.CONTINUING.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.NEW.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.REVERSAL.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.ADJUSTMENT.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.OTHER.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.SUPLEMENTAL.equals(form.type.getValue()))) ) )
			return false;

		if ( (form.coverageStart.getValue() != null) && (form.getValue() != null) && (form.getValue().inheritEndDate != null) ) {
			Date polDate = DateTimeFormat.getFormat("yyyy-MM-dd").parse(form.getValue().inheritEndDate);
			Date recDate = form.coverageStart.getValue();

			if (polDate.before(recDate)) {
				form.coverageStart.setInvalid(true);
				return false;
			}
		}

		return true;
	}

	private boolean validateCoverageEnd() {
		return validateDate(form.coverageEnd, form.coverageStart.getValue() == null);
	}

	private boolean validateStartAndEndDate() {
		boolean validDates = validateCoverageStart() && validateCoverageEnd();

		if(validDates){
			Date startDate = form.coverageStart.getValue();
			Date endDate = form.coverageEnd.getValue();

			if(startDate != null && endDate != null){
				if(startDate.before(endDate)){
					return true;
				}
				else{
					form.coverageStart.setInvalid(true);
					form.coverageEnd.setInvalid(true);
					return false;
				}

			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	private boolean validateDueDate() {
		return validateDate(form.dueDate, !
				((BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.CONTINUING.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.NEW.equals(form.type.getValue())) ||
				(BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.ADJUSTMENT.equals(form.type.getValue()))) );
	}

	private boolean validateDescription() {
		return validateString(form.description, 0, 250, true);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateBonusMalusOption() {
		return true;
	}

	private boolean validateBonusMalusValue() {
		if("Bonus".equalsIgnoreCase(form.bonusMalusOption.getValue())) {
			return validateNumber(form.bonusMalusValue, false);
		}else if("Malus".equalsIgnoreCase(form.bonusMalusOption.getValue())) {
			return validateNumber(form.bonusMalusValue, false);
		}else{
			form.bonusMalusValue.setWarning(form.bonusMalusValue.getValue() != null);
			return true;
		}
	}

	private boolean validateValues() {
		boolean validValues = validateTotalPremium() && validateSalesPremium() && validateBonusMalusValue();

		if(validValues){
			Double totalPremium = form.totalPremium.getValue();
			Double salesPremium = form.salesPremium.getValue();
			Double bonusMalusValue = form.bonusMalusValue.getValue();

			if ( bonusMalusValue == null )
				bonusMalusValue = 0.0;
			bonusMalusValue = Math.abs(bonusMalusValue);
			if ( "Bonus".equalsIgnoreCase(form.bonusMalusOption.getValue()) )
				bonusMalusValue = -bonusMalusValue;

			if(totalPremium != null && salesPremium != null){
				if(totalPremium >= salesPremium + bonusMalusValue){
					return true;
				}
				else{
					form.totalPremium.setInvalid(true);
					form.salesPremium.setInvalid(true);
					if ( form.bonusMalusValue.getValue() != null )
						form.bonusMalusValue.setInvalid(true);
					return false;
				}

			}else{
				return true;
			}
		}else{
			return true;
		}
	}

}
