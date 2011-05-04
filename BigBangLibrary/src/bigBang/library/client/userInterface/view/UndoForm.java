package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.shared.ProcessUndoItem;

public class UndoForm extends FormView<ProcessUndoItem> {

	private Label beforeDescription, afterDescription;
	private Button undoButton;
	
	public UndoForm(){
		super();
		beforeDescription = new Label();
		afterDescription = new Label();
		
		beforeDescription.setWordWrap(true);
		afterDescription.setWordWrap(false);
		
		addSection("Informação da Operação");
		addWidget(beforeDescription);
		addSection("Desfazer");
		addWidget(afterDescription);
		
		undoButton = new Button("Desfazer operação");
		addWidget(undoButton);
		
		clearInfo();
	}
	
	@Override
	public ProcessUndoItem getInfo() {
		return null;
	}

	@Override
	public void setInfo(ProcessUndoItem info) {
		if(info == null){
			clearInfo();
			return;
		}
		beforeDescription.getElement().setInnerHTML(info.description);
		afterDescription.getElement().setInnerHTML(info.undoDescription);
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		beforeDescription.setText("-");
		afterDescription.setText("-");
	}
	
	public Button getUndoButton(){
		return this.undoButton;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		this.undoButton.setEnabled(!readOnly);
	}

}
