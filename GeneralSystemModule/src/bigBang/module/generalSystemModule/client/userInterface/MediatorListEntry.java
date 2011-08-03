package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.types.Mediator;
import bigBang.library.client.userInterface.ListEntry;

public class MediatorListEntry extends ListEntry<Mediator> {

	private Label comissioningProfileLabel;
	
	public MediatorListEntry(Mediator value) {
		super(value);
		if(comissioningProfileLabel == null)
			comissioningProfileLabel = new Label();
		comissioningProfileLabel.getElement().getStyle().setProperty("whiteSpace", "nowrap");
		HorizontalPanel labelWrapper = new HorizontalPanel();
		labelWrapper.setHeight("100%");
		labelWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		labelWrapper.add(comissioningProfileLabel);
		setRightWidget(labelWrapper);
		setHeight("40px");
		setInfo(value);
	}
	
	@Override
	public void setValue(Mediator value, boolean fireEvents) {
		super.setValue(value, fireEvents);
	}
	
	@Override
	public <I extends Object> void setInfo(I info){
		Mediator mediator = (Mediator) info;
		if(mediator.id == null){
			setTitle("Novo Mediador");
			return;
		}
			
		setTitle(mediator.name);
		setText(mediator.taxNumber);
		if(comissioningProfileLabel == null)
			comissioningProfileLabel = new Label();
		comissioningProfileLabel.setText(mediator.comissionProfile.value);
	}
	
}
