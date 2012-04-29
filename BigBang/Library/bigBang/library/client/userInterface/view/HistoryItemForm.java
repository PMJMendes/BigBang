package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.HistoryItem;

public class HistoryItemForm extends FormView<HistoryItem> {

	private Label beforeDescription, afterDescription;
	
	public HistoryItemForm(){
		super();
		beforeDescription = new Label();
		afterDescription = new Label();
		
		beforeDescription.setWordWrap(true);
		afterDescription.setWordWrap(false);
		
		addSection("Informação da Operação");
		addWidget(beforeDescription);
		addSection("Desfazer");
		addWidget(afterDescription);
		
		clearInfo();
	}
	
	@Override
	protected void initializeView() {
		return;
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

}
