package bigBang.library.client.userInterface.presenter;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactNavigationViewPresenter implements ViewPresenter{

	private Display view;
	private boolean hasPermissions = false;

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
		container.clear();
		container.add(this.view.asWidget());

	}

	private void bind() {

	}

	@Override
	public void setParameters(final HasParameters parameterHolder) {

		if(parameterHolder.getParameter("editpermission") != null){
			hasPermissions = true;
		}
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

					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("show");
					navig.removeParameter("contactid");
					navig.removeParameter("ownertypeid");
					NavigationHistoryManager.getInstance().go(navig);

					break;
				}

				case CREATE_CHILD_CONTACT:{
					createShowChildContact(null,presenter.getView().getContact().id);
					break;
				}
				case CANCEL:{
					presenter.setParameters(parameterHolder);	
					break;
				}

				case CHILD_SELECTED:{
					@SuppressWarnings("unchecked")
					Contact chosen = ((ListEntry<Contact>) presenter.getView().getSubContactList().getSelected().toArray()[0]).getValue(); 
					createShowChildContact(chosen.id, chosen.ownerId);
					break;
				}

				case ERROR_SHOWING_CONTACT:{
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("show");
					navig.removeParameter("contactid");
					navig.removeParameter("ownertypeid");
					NavigationHistoryManager.getInstance().go(navig);
				}

				}

			}
		});
	}

	protected void createShowChildContact(String id, String ownerId) {

		HasParameters showChildContact = new HasParameters();
		showChildContact.setParameter("contactid", id);
		showChildContact.setParameter("id", ownerId);
		showChildContact.setParameter("ownertypeid", BigBangConstants.EntityIds.CONTACT);
		if(hasPermissions ){
			showChildContact.setParameter("editpermission", "1");
		}
		ContactViewPresenter newChildPresenter = (ContactViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("SINGLE_CONTACT");
		HasWidgets newChildContainer = view.getNextContainer();
		newChildPresenter.go(newChildContainer);
		view.navigateTo((Widget) newChildContainer); 
		newChildPresenter.setParameters(showChildContact);
		newChildPresenter.registerActionHandler(new ActionInvokedEventHandler<ContactViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch (action.getAction()){
				case REMOVE_OK:{
					((NavigationPanel)view).navigateBack();
					break;
				}
				case CREATE_CHILD_CONTACT:{
					createShowChildContact(null, ((ContactViewPresenter) action.getSource()).getView().getContact().id);

					break;
				}
				case CANCEL: {
					if (((ContactViewPresenter) action.getSource()).getView().getContact().id != null){
						((ContactViewPresenter) action.getSource()).setParameters(((ContactViewPresenter) action.getSource()).getParameters());
					}
					else
						((NavigationPanel)view).navigateBack();

					break;
				}
				case CHILD_SELECTED:{
					@SuppressWarnings("unchecked")
					Contact chosen = ((ListEntry<Contact>) ((ContactViewPresenter) action.getSource()).getView().getSubContactList().getSelected().toArray()[0]).getValue(); 
					createShowChildContact(chosen.id, chosen.ownerId);
					break;
				}
				case ERROR_SHOWING_CONTACT:{
					((NavigationPanel)view).navigateBack();
					break;
				}
				}

			}
		});
	}


}
