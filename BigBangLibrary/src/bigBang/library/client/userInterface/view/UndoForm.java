package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.HistoryItem;

public class UndoForm extends FormView<HistoryItem> {

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
		HorizontalPanel buttonWrapper = new HorizontalPanel();
		buttonWrapper.setWidth("100%");
		buttonWrapper.add(undoButton);
		buttonWrapper.setCellHorizontalAlignment(undoButton, HasHorizontalAlignment.ALIGN_RIGHT);
		addWidget(buttonWrapper);
		
		clearInfo();
	}
	
	@Override
	public HistoryItem getInfo() {
		return null;
	}

	@Override
	public void setInfo(HistoryItem info) {
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
