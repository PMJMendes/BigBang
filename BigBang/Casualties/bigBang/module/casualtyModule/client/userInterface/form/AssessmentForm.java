package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.Date;

import bigBang.definitions.shared.Assessment;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public class AssessmentForm extends FormView<Assessment>{

	DatePickerFormField scheduleDate;
	DatePickerFormField effectiveDate;
	RadioButtonFormField result;
	CheckBoxFormField loss;
	TextAreaFormField notes;
	DateTimeFormat format;

	public AssessmentForm() {
		
		this.format = DateTimeFormat.getFormat("yyyy-MM-dd");
		
		addSection("Detalhes da Peritagem ou Averiguação");
		
		scheduleDate = new DatePickerFormField("Data Agendada");
		effectiveDate = new DatePickerFormField("Data da Peritagem ou Averiguação");
		result = new RadioButtonFormField("Resultado");
		result.setMandatory(true);
		result.addOption("CONDITIONAL", "Condicional");
		result.addOption("FINAL", "Definitivo");
		loss = new CheckBoxFormField("Perda Total");
		notes = new TextAreaFormField("Notas");

		addFormField(scheduleDate);
		addFormField(effectiveDate);
		addFormField(result);
		addFormField(loss);
		addFormField(notes);

		scheduleDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				effectiveDate.setEditable(event.getValue() != null);
				effectiveDate.setReadOnly(isReadOnly());
				result.setEditable(effectiveDate.getValue() != null);
				result.setReadOnly(isReadOnly());
				loss.setEditable(effectiveDate.getValue() != null);
				loss.setReadOnly(isReadOnly());
			}
		});

		effectiveDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				result.setEditable(event.getValue() != null);
				result.setReadOnly(isReadOnly());
				loss.setEditable(event.getValue() != null);
				loss.setReadOnly(isReadOnly());
				if(event.getValue() != null){
					validate();
				}
			}
		});


		
		effectiveDate.setEditable(false);
		result.setEditable(false);
		loss.setEditable(false);
		setValidator(new AssessmentFormValidator(this));
	}

	@Override
	public Assessment getInfo() {
		
		Assessment ass = value;
		ass.scheduledDate = scheduleDate.getStringValue();
		ass.effectiveDate = effectiveDate.getStringValue();
		if(result.getValue() != null){
			ass.isConditional = "CONDITIONAL".equalsIgnoreCase(result.getValue());
		}
		ass.isTotalLoss = loss.getValue();
		ass.notes = notes.getValue();
		
		return ass;
			
	}

	@Override
	public void setInfo(Assessment info) {	
		scheduleDate.setValue(info.scheduledDate);
		effectiveDate.setValue(info.effectiveDate);
		if(info.isConditional != null){
			result.setValue(info.isConditional ? "CONDITIONAL" : "FINAL");
		}
		loss.setValue(info.isTotalLoss);
		notes.setValue(info.notes);
	}

}
