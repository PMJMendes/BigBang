package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.casualtyModule.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubCasualtyList extends FilterableList<SubCasualtyStub> implements SubCasualtyDataBrokerClient {

	public static class Entry extends ListEntry<SubCasualtyStub> {

		protected Label numberLabel;
		protected Label policyLabel;
		protected Label valueLabel;
		protected Label insurerProcLabel;
		protected Image openImage;
		protected boolean initialized;

		public Entry(SubCasualtyStub value) {
			super(value);
			setHeight("50px");
		}

		@Override
		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				numberLabel = getFormatedLabel();
				numberLabel.setWordWrap(false);
				numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
				policyLabel = getFormatedLabel();
				policyLabel.getElement().getStyle().setFontSize(10, Unit.PX);
				policyLabel.setHeight("1.2em");
				policyLabel.setWordWrap(true);
				insurerProcLabel = getFormatedLabel();
				insurerProcLabel.getElement().getStyle().setFontSize(10, Unit.PX);
				insurerProcLabel.setHeight("1.2em");
				insurerProcLabel.setWordWrap(true);

				HorizontalPanel leftContainer = new HorizontalPanel();
				leftContainer.setSize("100%", "100%");

				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				container.add(numberLabel);
				container.setCellHeight(numberLabel, "100%");
				container.setCellVerticalAlignment(numberLabel, HasVerticalAlignment.ALIGN_TOP);
				container.add(policyLabel);
				container.add(insurerProcLabel);
				setWidget(container);

				HorizontalPanel leftWidgetContainer = new HorizontalPanel();
				leftWidgetContainer.setSize("100%", "100%");

				VerticalPanel rightWrapper = new VerticalPanel();
				leftWidgetContainer.add(rightWrapper);
				rightWrapper.setSize("100%", "100%");
				rightWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

				openImage = new Image();
				leftWidgetContainer.add(openImage);
				openImage.getElement().getStyle().setMarginLeft(10, Unit.PX);
				leftWidgetContainer.setCellVerticalAlignment(openImage, HasVerticalAlignment.ALIGN_MIDDLE);

				((UIObject) rightWidgetContainer).setSize("100%", "100%");
				setLeftWidget(leftWidgetContainer);
				
				valueLabel = new Label();
				valueLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

				VerticalPanel rightContainer = new VerticalPanel();
				rightContainer.add(valueLabel);
				rightContainer.setSize("100%", "100%");
				rightContainer.setCellVerticalAlignment(valueLabel, HasVerticalAlignment.ALIGN_BOTTOM);
				setRightWidget(rightContainer);
				container.setCellHorizontalAlignment(rightContainer, HasHorizontalAlignment.ALIGN_RIGHT);
				container.setCellVerticalAlignment(rightContainer, HasVerticalAlignment.ALIGN_BOTTOM);
			}

			SubCasualtyStub c = (SubCasualtyStub) info;

			this.numberLabel.setText("#" + (c.number == null ? "" : c.number));
			this.numberLabel.setTitle("Número e tipo de Recibo");

			String referenceType = c.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? "Apólice #" : "Apólice Adesão #";
			
			this.policyLabel.setText(referenceType + c.referenceNumber + " " + c.categoryName + "/" + c.lineName + "/" + c.subLineName);

			this.insurerProcLabel.setText("Proc. Seg. # " + c.insurerProcessNumber);

			Resources r = GWT.create(Resources.class);
			openImage.setResource(c.isOpen ? r.activeCasualtyIcon() : r.inactiveCasualtyIcon());
			
			valueLabel.setText(c.totalDamages != null ?  c.totalDamages + "€" : "-");
			
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
				this.policyLabel.getElement().getStyle().setColor("white");
				this.valueLabel.getElement().getStyle().setColor("white");
				this.insurerProcLabel.getElement().getStyle().setColor("white");
			}else{
				this.policyLabel.getElement().getStyle().setColor("gray");
				this.valueLabel.getElement().getStyle().setColor("black");
				this.insurerProcLabel.getElement().getStyle().setColor("gray");
			}
		}
	}

	protected String ownerId;
	protected SubCasualtyDataBroker broker;
	protected int dataVersion;

	public SubCasualtyList(){
		this.showFilterField(false);
		this.showSearchField(true);
		
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId == null) {
			clear();
		}else{
			broker.getSubCasualties(ownerId, new ResponseHandler<Collection<SubCasualtyStub>>(){

				@Override
				public void onResponse(Collection<SubCasualtyStub> response) {
					clear();
					for(SubCasualtyStub subCasualty : response) {
						addEntry(subCasualty);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null){
			this.broker.unregisterClient(this);
			this.ownerId = null;
		}
	}

	public void addEntry(SubCasualtyStub c){
		this.add(new Entry(c));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.dataVersion = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return dataVersion;
	}

	@Override
	public void addSubCasualty(SubCasualty subCasualty) {
		if(this.ownerId != null && subCasualty != null && subCasualty.casualtyId.equalsIgnoreCase(this.ownerId)){
			addEntry(subCasualty);
		}
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty) {
		if(this.ownerId != null && subCasualty != null && subCasualty.casualtyId.equalsIgnoreCase(this.ownerId)){
			for(ValueSelectable<SubCasualtyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(subCasualty.id)) {
					entry.setValue(subCasualty);
					break;
				}
			}
		}
	}

	@Override
	public void removeSubCasualty(String id) {
		if(this.ownerId != null && id != null){
			for(ValueSelectable<SubCasualtyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(id)) {
					remove(entry);
					break;
				}
			}
		}
	}
}
