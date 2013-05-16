package bigBang.module.receiptModule.client.userInterface.form;

public class ReceiptTasksForm extends ReceiptForm{

	@Override
	public void setDefaultValidator(){
		setValidator(new ReceiptTasksFormValidator(this));
	}

}
