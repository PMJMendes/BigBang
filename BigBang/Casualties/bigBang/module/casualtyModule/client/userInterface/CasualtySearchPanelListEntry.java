package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.CasualtyStub;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.casualtyModule.client.resources.Resources;

public class CasualtySearchPanelListEntry extends ListEntry<CasualtyStub> {

	protected Label numberLabel;
	protected Label clientLabel;
	protected Label dateLabel;
	protected Image openImage;
	protected boolean initialized;

	public CasualtySearchPanelListEntry(CasualtyStub value) {
		super(value);
		setHeight("50px");
	}

	@Override
	public <I extends Object> void setInfo(I info) {
		if(!initialized){
			numberLabel = getFormatedLabel();
			numberLabel.setWordWrap(false);
			numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
			dateLabel = getFormatedLabel();
			clientLabel = getFormatedLabel();
			this.clientLabel.getElement().getStyle().setFontSize(10, Unit.PX);

			VerticalPanel container = new VerticalPanel();
			container.setSize("100%", "100%");
			container.add(numberLabel);
			container.setCellHeight(numberLabel, "100%");
			container.setCellVerticalAlignment(numberLabel, HasVerticalAlignment.ALIGN_TOP);
			container.add(clientLabel);
			setWidget(container);

			VerticalPanel rightContainer = new VerticalPanel();
			rightContainer.setSize("100%", "100%");
			rightContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			dateLabel = getFormatedLabel();
			rightContainer.add(dateLabel);
			rightContainer.setCellVerticalAlignment(dateLabel, HasVerticalAlignment.ALIGN_TOP);

			openImage = new Image();
			rightContainer.add(openImage);
			rightContainer.setCellVerticalAlignment(openImage, HasVerticalAlignment.ALIGN_MIDDLE);

			((UIObject) rightWidgetContainer).setSize("100%", "100%");
			setRightWidget(rightContainer);
		}

		CasualtyStub c = (CasualtyStub) info;

		this.numberLabel.setText("#" + (c.processNumber == null ? "" : c.processNumber));
		this.numberLabel.setTitle("Número e tipo de Recibo");

		this.clientLabel.setText((c.clientNumber == null ? "" : "Cliente #" + c.clientNumber + " - ") +
				(c.clientName == null ? "" : c.clientName));
		this.clientLabel.setTitle("Cliente");

		Resources r = GWT.create(Resources.class);
		
		openImage.setResource(c.isOpen ? r.activeCasualtyIcon() : r.inactiveCasualtyIcon());
		
		this.dateLabel.setText(c.casualtyDate == null ? "" : c.casualtyDate);
		this.dateLabel.setTitle("Data de Vigência");
		initialized = true;
		setSelected(this.isSelected(), false);

	};

	@Override
	public void setSelected(boolean selected, boolean b) {
		super.setSelected(selected, b);
		if(!initialized) {
			return;
		}
		if(selected){
			this.dateLabel.getElement().getStyle().setColor("white");
			this.clientLabel.getElement().getStyle().setColor("white");
		}else{
			this.dateLabel.getElement().getStyle().setColor("#0066FF");
			this.clientLabel.getElement().getStyle().setColor("#0066FF");
		}
	}

}
