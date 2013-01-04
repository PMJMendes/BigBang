package bigBang.library.client.userInterface;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.ConversationBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.resources.Resources;

public class MessagesList extends FilterableList<Message> implements ConversationBrokerClient{

	public static class Entry extends ListEntry<Message>{

		protected Image statusIcon;

		public Entry(Message value){
			super(value);
			setHeight("35px");

			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			textLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			titleLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		}

		public <I extends Object> void setInfo(I info){

			Message m = (Message) info;

			if(value.id != null){
				if(statusIcon == null){
					statusIcon = new Image();
					setRightWidget(statusIcon);
				}
				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(Conversation.Direction.OUTGOING.equals(m.direction) ? resources.arrowLeftIcon() : resources.arrowRightIcon());
				this.setTitle(m.subject);
				this.setText(m.date);
			}

		}

	}

	protected String ownerId;
	protected ConversationBroker broker;

	public MessagesList(){

		this.showFilterField(false);
		this.showSearchField(false);

		ListHeader header = new ListHeader("Mensagens");
		header.setHeight("40px");

		this.setHeaderWidget(header);

		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
	}

	public void setOwner(final String ownerId){
		discardOwner();
		if(ownerId != null && !ownerId.isEmpty()){
			this.broker.getConversation(ownerId, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					addEntrys(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					GWT.log("Could not get messages for owner " + ownerId);
				}
			});
		}
	}

	protected void addEntrys(Conversation response) {
		broker.registerClient(this);
		for(int i = 0; i<response.messages.length; i++){
			Message m = response.messages[i];
			addEntry(m);
		}		
		if(response.messages.length > 0){
			this.get(0).setSelected(true);
		}
	}

	protected void addEntry(Message c) {
		this.add(new Entry(c));
	}

	private void discardOwner() {
		this.clear();		
		broker.unregisterClient(this);
	}

	@Override
	public void updateConversation(Conversation response) {
		this.clear();
		addEntrys(response);		
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}

}
