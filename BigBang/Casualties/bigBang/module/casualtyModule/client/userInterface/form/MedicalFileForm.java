package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFile.MedicalDetail;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.casualtyModule.client.userInterface.NewMedicalDetailItemSection;

public class MedicalFileForm extends FormView<MedicalFile>{

	DatePickerFormField nextAppointment;
	NewMedicalDetailItemSection newItemSection;
	Collection<MedicalDetailItemSection> medicalDetailItemSections;

	public MedicalFileForm() {
		nextAppointment = new DatePickerFormField("Data da próxima consulta");

		addSection("Informação Geral");

		addFormField(nextAppointment);

		newItemSection = new NewMedicalDetailItemSection();
		newItemSection.getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addMedicalDetail(new MedicalFile.MedicalDetail());
			}
		});

		medicalDetailItemSections = new ArrayList<MedicalDetailItemSection>();
		
		addLastFields();
		
		setValue(new MedicalFile());
		
		setValidator(new MedicalFormValidator(this));
	}

	protected void addMedicalDetail(MedicalFile.MedicalDetail medicalDetail) {

		if(!medicalDetail.deleted){
			
			final MedicalDetailItemSection section = new MedicalDetailItemSection(medicalDetail);
			section.setReadOnly(this.isReadOnly());
			this.medicalDetailItemSections.add(section);
			addSection(section);
			section.setItem(medicalDetail);
			
			if(medicalDetail.id == null){
				section.expand();
			}
			
			section.getRemoveButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					removeItemAndSection(section);
				}
			});
			
			addLastFields();
		}
	}

	protected void removeItemAndSection(MedicalDetailItemSection section) {
		section.setVisible(false);
		MedicalDetail detail = section.getItem();
		detail.deleted = true;
		section.setItem(detail);
	}

	private void addLastFields() {
		addSection(newItemSection);
	}

	@Override
	public MedicalFile getInfo() {

		MedicalFile result = value;
		
		if(result != null){
			result.nextDate = nextAppointment.getStringValue();
			result.details = getMedicalDetails();
		}
		
		return result;
		
	}

	@Override
	public void setInfo(MedicalFile info) {

		if(info != null){
			nextAppointment.setValue(info.nextDate);
			setMedicalDetails(info.details);
		}

	}

	private void setMedicalDetails(MedicalFile.MedicalDetail[] details) {
		for(FormViewSection section : medicalDetailItemSections){
			removeSection(section);
		}

		medicalDetailItemSections.clear();

		if(details != null){
			for (MedicalFile.MedicalDetail detail : details){
				addMedicalDetail(detail);
			}
		}
	}

	private MedicalFile.MedicalDetail[] getMedicalDetails(){
		MedicalFile.MedicalDetail[] details = new MedicalFile.MedicalDetail[medicalDetailItemSections.size()];

		int i = 0; 

		for(MedicalDetailItemSection section : medicalDetailItemSections){
			details[i] = section.getItem();
			i++;
		}

		return details;
	}

}
