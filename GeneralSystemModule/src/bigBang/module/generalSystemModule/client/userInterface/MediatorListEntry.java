package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorListEntry extends ListEntry<Mediator> {

	private Label comissioningProfileLabel;
	
	public MediatorListEntry(Mediator value) {
		super(value);
		comissioningProfileLabel = new Label();
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
		setTitle(mediator.name);
		setText(mediator.taxNumber);
		//comissioningProfileLabel.setText(mediator.comissionProfile.name);
	}
	
}
