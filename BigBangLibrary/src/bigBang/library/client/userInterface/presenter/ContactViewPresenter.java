package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.view.ContactView;
import bigBang.library.client.userInterface.view.ContactView.ContactEntry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ContactViewPresenter implements ViewPresenter{

	private Contact contact;
	private boolean bound = false;
	private Display view;


	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		CREATE_CHILD_CONTACT,
		SHOW_CHILD_CONTACTS,
		ADD_NEW_DETAIL,
		DELETE_DETAIL
	}


	public interface Display{


		public void setContact(Contact contacto);
		public void addContactInfo(ContactInfo info);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		public void setEditable(boolean b);
		public List<ContactInfo> getContactInfoList();
		public ContactEntry initializeContactEntry();
		public Contact getContact();
		public void setSaveMode(boolean b);
		public void deleteDetail();
		public void registerDeleteHandler(
				DeleteRequestEventHandler deleteRequestEventHandler);
	}

	public void setContact(Contact contact){

		this.contact = contact;
		view.setContact(contact);

		for(int i = 0; i<contact.info.length; i++){


			view.addContactInfo(contact.info[i]);

		}
		view.addContactInfo(null);
	}

	@Override
	public void setView(UIObject view) {

		this.view = (Display)view;

	}

	@Override
	public void go(HasWidgets container) {
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());

	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}

	public void bind() {
		if(bound){
			return;
		}

		view.registerDeleteHandler(new DeleteRequestEventHandler(){

			@Override
			public void onDeleteRequest(Object object) {
				
				//TODO APAGAR DA BD
				List<ContactInfo> list = view.getContactInfoList();
				
				for(ValueSelectable<ContactInfo> cont: list){
					
					if(cont.getValue() == object) {
						list.remove(cont);
						break;
					}
					
				}

				
			}
			
			
		});
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){

				case ADD_NEW_DETAIL: 
					addNewDetail();
					break;

				case CREATE_CHILD_CONTACT: System.out.println("TENTEI CRIAR UM FILHO NOVO!");
				break;

				case CANCEL:
					setContact(contact);
					view.setEditable(false);
					break;

				case EDIT: 
					view.setEditable(true);
					break;

				case SAVE: 
					contact = view.getContact();
					setContact(contact);
					view.setSaveMode(false);
					//TODO SUBMIT TO DATABASE
					view.setEditable(false);
				break;
				
				case DELETE_DETAIL:
					view.deleteDetail();
					break;

				case SHOW_CHILD_CONTACTS: System.out.println("MOSTRA OS FILHOTES");
				break;
				}

			}

		});

	}

	public void addNewDetail() {
		
		ContactEntry temp = view.initializeContactEntry();
		temp.setHeight("40px");
		view.getContactInfoList().remove(view.getContactInfoList().size()-1);
		temp.setEditable(true);
		view.getContactInfoList().add(temp);
		view.addContactInfo(null);
		
	}

	public ContactView getView() {
		return (ContactView)view;
	} 
	

}
