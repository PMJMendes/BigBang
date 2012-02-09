package bigBang.client.tests;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.HasParameters;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter.Action;
import bigBang.module.clientModule.client.userInterface.view.ClientSectionView;

public class TestsView extends View implements TestsViewPresenter.Display {

	private VerticalPanel wrapper = new VerticalPanel();
	private VerticalPanel container = new VerticalPanel();

	private PopupPanel popupPanel;

	private ActionInvokedEventHandler<Action> actionHandler;

	public TestsView(){

		initWidget(wrapper);

	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setTypifiedTexts(String tag) {

	}

	@Override
	public HasWidgets getContainer() {
		return container;
	}

	@Override
	public void show() {
		this.popupPanel = new PopupPanel(){
			@Override
			protected void onDetach() {
				super.onDetach();
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ON_OVERLAY_CLOSED));
				TestsView.this.popupPanel = null;
			}
		};
		this.popupPanel.add((Widget)this.container);
		this.popupPanel.center();
	}

}
