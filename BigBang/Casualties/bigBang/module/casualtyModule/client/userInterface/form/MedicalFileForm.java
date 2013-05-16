package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFile.Appointment;
import bigBang.definitions.shared.MedicalFile.MedicalDetail;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.casualtyModule.client.userInterface.AppointmentForm;
import bigBang.module.casualtyModule.client.userInterface.PaymentGridPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MedicalFileForm extends FormView<MedicalFile>{

	DatePickerFormField nextAppointment;
	Button newMedicalDetailButton;
	Button newAppointmentButton;
	CollapsibleFormViewSection medicalDetails;
	CollapsibleFormViewSection appointments;
	Collection<MedicalDetailForm> medicalDetailForms;
	Collection<AppointmentForm> appointmentForms;
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

		nextAppointment = new DatePickerFormField("Próximo contacto");

		addSection("Informação Geral");

		HorizontalPanel panel = new HorizontalPanel();

		panel.add(nextAppointment);
		panel.add(notes);
		panel.add(showAllPayments);

		panel.setWidth("95%");

		panel.setCellHorizontalAlignment(showAllPayments, HasHorizontalAlignment.ALIGN_RIGHT);

		addWidget(panel);

		registerFormField(nextAppointment);
		registerFormField(notes);

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

		medicalDetails = new CollapsibleFormViewSection("Detalhes");
		appointments = new CollapsibleFormViewSection("Consultas");

		newMedicalDetailButton = new Button("Adicionar Detalhe");
		newMedicalDetailButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addMedicalDetail(new MedicalFile.MedicalDetail());
			}
		});

		newAppointmentButton = new Button("Adicionar Consulta");
		newAppointmentButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAppointment(new MedicalFile.Appointment());
			}
		});

		medicalDetailForms = new ArrayList<MedicalDetailForm>();
		appointmentForms = new ArrayList<AppointmentForm>();

		addLastFields();

		setValue(new MedicalFile());

		addSection(medicalDetails);
		medicalDetails.setSize("100%", "100%");
		addSection(appointments);
		appointments.setSize("100%", "100%");

		setValidator(new MedicalFormValidator(this));
	}

	protected void addAppointment(Appointment appointment) {
		if(!appointment.deleted){

			final AppointmentForm app = new AppointmentForm();
			app.setReadOnly(this.isReadOnly());
			this.appointmentForms.add(app);
			app.setSize("100%","100%");
			appointments.addWidget(app.getNonScrollableContent());
			app.setValue(appointment);

			app.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeItemAndsection(app);
				}
			});

			addLastFields();
		}
	}

	protected void removeItemAndsection(AppointmentForm section) {
		section.getNonScrollableContent().setVisible(false);
		section.removeFromParent();
		Appointment detail = section.getInfo();
		detail.deleted = true;
		section.setValue(detail);		

	}

	protected void addMedicalDetail(MedicalFile.MedicalDetail medicalDetail) {

		if(!medicalDetail.deleted){

			final MedicalDetailForm section = new MedicalDetailForm();
			section.setReadOnly(this.isReadOnly());
			section.setSize("100%","100%");
			this.medicalDetailForms.add(section);
			medicalDetails.addWidget(section.getNonScrollableContent());
			section.setValue(medicalDetail);

			section.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeItemAndSection(section);
				}
			});

			addLastFields();
		}
	}

	protected void removeItemAndSection(MedicalDetailForm section) {
		section.getNonScrollableContent().setVisible(false);
		MedicalDetail detail = section.getInfo();
		detail.deleted = true;
		section.setValue(detail);
	}

	private void addLastFields() {
		medicalDetails.addWidget(newMedicalDetailButton);
		appointments.addWidget(newAppointmentButton);
	}

	@Override
	public MedicalFile getInfo() {

		MedicalFile result = value;

		if(result != null){
			result.nextDate = nextAppointment.getStringValue();
			result.details = getMedicalDetails();
			result.appointments = getAppointments();
			result.notes = notes.getValue();
		}

		return result;
	}

	private Appointment[] getAppointments() {
		Appointment[] appointments = new Appointment[appointmentForms.size()];

		int i = 0;

		for(AppointmentForm app : appointmentForms){
			appointments[i] = app.getInfo();
			i++;
		}

		return appointments;
	}

	@Override
	public void setInfo(MedicalFile info) {

		if(info != null){
			nextAppointment.setValue(info.nextDate);
			notes.setValue(info.notes);
			setMedicalDetails(info.details);
			setAppointments(info.appointments);
		}

	}

	private void setAppointments(Appointment[] appointments) {
		for(AppointmentForm form : appointmentForms){
			form.removeFromParent();
			form.getNonScrollableContent().setVisible(false);
		}

		appointmentForms.clear();

		if(appointments!= null){
			for(Appointment app : appointments){
				addAppointment(app);
			}
		}
	}

	private void setMedicalDetails(MedicalFile.MedicalDetail[] details) {
		for(FormView<MedicalDetail> section : medicalDetailForms){
			section.getNonScrollableContent().removeFromParent();
		}

		medicalDetailForms.clear();

		if(details != null){
			for (MedicalFile.MedicalDetail detail : details){
				addMedicalDetail(detail);
			}
		}
	}

	private MedicalFile.MedicalDetail[] getMedicalDetails(){
		MedicalFile.MedicalDetail[] details = new MedicalFile.MedicalDetail[medicalDetailForms.size()];

		int i = 0; 

		for(MedicalDetailForm section : medicalDetailForms){
			details[i] = section.getInfo();
			i++;
		}

		return details;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(showAllPayments != null){
			showAllPayments.setEnabled(readOnly);
			for(MedicalDetailForm med : medicalDetailForms){
				med.setReadOnly(readOnly);
			}
			for(AppointmentForm sec : appointmentForms){
				sec.setReadOnly(readOnly);
			}
			newMedicalDetailButton.setVisible(!readOnly);
			newAppointmentButton.setVisible(!readOnly);
		}
		super.setReadOnly(readOnly);
	}
}
