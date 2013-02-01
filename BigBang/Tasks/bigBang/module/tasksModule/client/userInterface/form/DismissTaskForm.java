package bigBang.module.tasksModule.client.userInterface.form;

import bigBang.definitions.shared.Task;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class DismissTaskForm extends FormView<Task> {

	private TextBoxFormField dateField;
	private TextAreaFormField descriptionField;
	
	public DismissTaskForm(){
		dateField = new TextBoxFormField("Data da Notificação");
		descriptionField = new TextAreaFormField();
		descriptionField.setFieldHeight("300px");
		descriptionField.setFieldWidth("100%");
		descriptionField.setWidth("100%");
		descriptionField.setHeight("100%");
		
		addSection("Informação da Notificação");
		addFormField(dateField);
		addSection("Descrição");
		addFormField(descriptionField);
		
		setReadOnly(true);
	}
	
	@Override
	public Task getInfo() {
		return getValue();
	}

	@Override
	public void setInfo(Task info) {
		dateField.setValue(info.timeStamp);
		descriptionField.setValue(info.longDesc);
	}

}
