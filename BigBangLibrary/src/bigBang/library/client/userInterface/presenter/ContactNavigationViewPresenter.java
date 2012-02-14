package bigBang.library.client.userInterface.presenter;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;
import bigBang.library.client.userInterface.view.ContactView.ContactEntry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactNavigationViewPresenter implements ViewPresenter{

	private Display view;
	private boolean bound;
	private HasParameters parameterHolder;

	public ContactNavigationViewPresenter(Display view){

		setView((UIObject)view);

	}

	public interface Display{

		Widget asWidget();
		void setHomeWidget(UIObject view);
		HasWidgets getNextContainer();
		void navigateTo(Widget newChildContainer);

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

	private void bind() {

	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		this.parameterHolder = parameterHolder;
		final ContactViewPresenter presenter = (ContactViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("SINGLE_CONTACT");
		HasWidgets container = view.getNextContainer();
		presenter.go(container);
		view.setHomeWidget((UIObject) container);
		presenter.setParameters(parameterHolder);
		presenter.registerActionHandler(new ActionInvokedEventHandler<ContactViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch (action.getAction()){
				case REMOVE_OK:{
					if(!((NavigationPanel)view).navigateBack()){
						NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
						navig.removeParameter("id");
						navig.removeParameter("show");
						navig.removeParameter("contactid");
						navig.removeParameter("ownertypeid");
						navig.removeParameter("editpermission");
						NavigationHistoryManager.getInstance().go(navig);
					}
					break;
				}

				case SHOW_CHILD_CONTACTS:{
					showChildContacts(presenter.getView().getContact().id, presenter.getView().getContact().ownerId);
					break;
				}

				case CREATE_CHILD_CONTACT:{
					createChildContact(presenter.getView().getContact().id);
					break;
				}

				}

			}
		});

	}

	protected void createChildContact(String ownerId) {

		HasParameters newChildParameters = new HasParameters();
		newChildParameters.setParameter("id", ownerId);
		newChildParameters.setParameter("ownertypeid", BigBangConstants.EntityIds.CONTACT);
		newChildParameters.setParameter("editpermission", "1");
		final ContactViewPresenter newChildPresenter = (ContactViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("SINGLE_CONTACT");
		HasWidgets newChildContainer = view.getNextContainer();
		newChildPresenter.go(newChildContainer);
		view.navigateTo((Widget) newChildContainer);
		newChildPresenter.setParameters(newChildParameters);
		newChildPresenter.registerActionHandler(new ActionInvokedEventHandler<ContactViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch (action.getAction()){
				case REMOVE_OK:{
					((NavigationPanel)view).navigateBack();
					break;
				}

				case SHOW_CHILD_CONTACTS:{
					showChildContacts(newChildPresenter.getView().getContact().id, newChildPresenter.getView().getContact().ownerId);
					break;
				}

				case CREATE_CHILD_CONTACT:{
					createChildContact(newChildPresenter.getView().getContact().id);
					break;
				}

				}

			}
		});
	}

	protected void showChildContacts(String contactId, String ownerId) {

		HasParameters parameters = new HasParameters();
		parameters.setParameter("contactid", contactId);
		parameters.setParameter("ownerid", ownerId);
		HasWidgets contactListContainer = view.getNextContainer();
		final SubContactListViewPresenter contactListPresenter = (SubContactListViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("SUB_CONTACT_LIST");
		contactListPresenter.go(contactListContainer);
		view.navigateTo((Widget) contactListContainer);
		contactListPresenter.setParameters(parameters);
		
		contactListPresenter.registerActionHandler(new ActionInvokedEventHandler<ContactViewPresenter.Action>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case CHILD_SELECTED:{
					System.out.println("O ESCOLHIDO FOI O:" + ((ListEntry<Contact>) contactListPresenter.getView().getList().getSelected().toArray()[0]).getValue().name);
				}
				}
				
			}
		});
	}

}
