package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.List;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CostCenterMemberList extends List<User> {

	private Button addButton;
	private Button removeButton;
	
	public CostCenterMemberList(){
		super();
		
		HorizontalPanel toolbar = new HorizontalPanel();
		toolbar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolbar.setHeight("30px");
		
		addButton = new Button("Adicionar");
		removeButton = new Button("Remover");
		
		toolbar.add(addButton);
		toolbar.add(removeButton);
		removeButton.setEnabled(false);
		
		setHeaderWidget(toolbar);
		updateFooterLabel();
		setMultipleSelectEnabled(true);
	}
	
	@Override
	public void setValue(User value, boolean fireEvents) {
		super.setValue(value, fireEvents);
		this.removeButton.setEnabled(getSelectedEntry() != null);
	}
	
	@Override
	protected void onSizeChanged(){
		updateFooterLabel();
		this.removeButton.setEnabled(getSelectedEntry() != null);
	}
	
	private void updateFooterLabel(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem membros associados";
			break;
		case 1:
			text = "1 membro associado";
			break;
		default:
			text = size + " membros associados";
			break;
		}
		
		setFooterText(text);
	}

	public HasClickHandlers getAddButton() {
		return this.addButton;
	}
	
	public HasClickHandlers getRemoveButton() {
		return this.removeButton;
	}

}
