package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFile.MedicalDetail;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.casualtyModule.client.userInterface.NewMedicalDetailItemSection;
import bigBang.module.casualtyModule.client.userInterface.PaymentGridPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MedicalFileForm extends FormView<MedicalFile>{

	DatePickerFormField nextAppointment;
	NewMedicalDetailItemSection newItemSection;
	Collection<MedicalDetailItemSection> medicalDetailItemSections;
	Button showAllPayments = new Button("Resumo de Pagamentos");
	TextBoxFormField notes;
	private PopupPanel popup;
	private PaymentGridPanel payments;

	public MedicalFileForm() {

		popup = new PopupPanel();
		payments = new PaymentGridPanel();
		notes = new TextBoxFormField("Notas");
		notes.setFieldWidth("250px");
		
		popup.add(payments);

		nextAppointment = new DatePickerFormField("Data do próximo contacto");

		addSection("Informação Geral");

		HorizontalPanel panel = new HorizontalPanel();

		panel.add(nextAppointment);
		panel.add(showAllPayments);

		panel.setWidth("95%");

		panel.setCellHorizontalAlignment(showAllPayments, HasHorizontalAlignment.ALIGN_RIGHT);

		addWidget(panel);

		registerFormField(nextAppointment);

		addFormField(notes);
		
		showAllPayments.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(getValue().details == null || getValue().details.length == 0){
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não há pagamentos para apresentar"), TYPE.ALERT_NOTIFICATION));
				}
				else{
					payments.setValue(MedicalFileForm.this.getValue().details);
					popup.center();
				}
			}
		});

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
			result.notes = notes.getValue();
		}

		return result;

	}

	@Override
	public void setInfo(MedicalFile info) {

		if(info != null){
			nextAppointment.setValue(info.nextDate);
			notes.setValue(info.notes);
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

	@Override
	public void setReadOnly(boolean readOnly) {
		if(showAllPayments != null){
			showAllPayments.setEnabled(readOnly);
		}
		super.setReadOnly(readOnly);
	}
}
