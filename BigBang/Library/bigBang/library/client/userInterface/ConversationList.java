package bigBang.library.client.userInterface;

import java.util.Collection;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.ConversationStub.Direction;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.ConversationBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.resources.Resources;

public class ConversationList extends FilterableList<ConversationStub> implements ConversationBrokerClient{


	public static class Entry extends ListEntry<ConversationStub>{

		protected Label subjectLabel;
		protected Label requestType;
		protected Image statusIcon;
		private boolean initialized;


		public Entry(ConversationStub value) {
			super(value);
			setHeight("50px");
		}
		
		public <I extends Object> void setInfo(I info){
			ConversationStub value = (ConversationStub)info;
			
			if(value.id != null){
				if(!initialized){
					subjectLabel = getFormatedLabel();
					subjectLabel.getElement().getStyle().setFontSize(14, Unit.PX);
					subjectLabel.setWordWrap(false);
					subjectLabel.getElement().getStyle().setProperty("whiteSpace", "");
					
					requestType = getFormatedLabel();
					requestType.getElement().getStyle().setFontSize(11, Unit.PX);
					requestType.getElement().getStyle().setProperty("whiteSpace", "");
					requestType.setHeight("1.2em");
					
					statusIcon = new Image();
					
					VerticalPanel container = new VerticalPanel();
					container.add(subjectLabel);
					container.add(statusIcon);
					container.add(requestType);
					
					container.setCellHorizontalAlignment(statusIcon, HasHorizontalAlignment.ALIGN_RIGHT);
					
					container.setSize("100%", "100%");
					
					setWidget(container);
				}
				
				subjectLabel.setText(value.subject);
				requestType.setText(value.requestTypeLabel);
				
				Resources r = GWT.create(Resources.class);
				
				statusIcon.setResource(value.pendingDir != null ? (value.pendingDir.equals(Direction.OUTGOING) ? r.yellowIcon() : r.greenIcon()) : r.greyIcon());
				
				
				initialized = true;
			}			
		}
	}

	protected ConversationBroker broker;
	protected String ownerId;
	protected int dataVersion;

	public ConversationList(){
		this.showFilterField(false);
		this.showSearchField(true);

		broker = (ConversationBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CONVERSATION);

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}



	protected void discardOwner() {
		this.clear();
	}

	public void setOwner(String ownerId2) {
		discardOwner();

		if(ownerId2 != null){
			this.broker.registerClient(this);

			broker.getConversationsForOwner(ownerId2, new ResponseHandler<Collection<ConversationStub>>(){

				@Override
				public void onResponse(Collection<ConversationStub> response) {
					clear();
					for(ConversationStub s : response){
						addEntry(s);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {					
				}

			});
		}

		this.ownerId = ownerId2;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION));
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void updateConversation(Conversation response) {
		for(ValueSelectable<ConversationStub> c : this){
			if(c.getValue().id.equalsIgnoreCase(response.id)){
				c.setValue(response);
				break;
			}
		}
	}

	public void addConversation(Conversation conversation){
		if(this.ownerId != null){
			if(conversation.parentDataObjectId.equalsIgnoreCase(conversation.id)){
				this.addEntry(conversation);
			}
		}
	}


	private void addEntry(ConversationStub conversation) {
		add(new Entry(conversation));
	}

}
