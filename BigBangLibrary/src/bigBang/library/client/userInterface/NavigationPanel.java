package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.ListIterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.event.NavigationEvent;
import bigBang.library.client.event.NavigationEventHandler;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.view.View;

public class NavigationPanel extends View {

	protected NavigationToolbar navBar;
	protected SlidePanel slide;

	protected ArrayList<Widget> navigatables;
	protected ListIterator<Widget> iterator;
	
	protected boolean showPreviousButton, showNextButton;

	public NavigationPanel() {
		this("");
	}
	
	public NavigationPanel(String navBarTitle){
		showPreviousButton = true;
		showNextButton = false;
		
		navigatables = new ArrayList<Widget>();
		iterator = navigatables.listIterator();

		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		navBar = new NavigationToolbar(); 
		navBar.setText(navBarTitle);
		wrapper.add(navBar);

		navBar.addNavigationEventHandler(new NavigationEventHandler() {

			@Override
			public void onNavigationEvent(NavigationEvent event) {
				switch(event.getNavigationCommand()) {
				case NEXT:
					navigateForward();
					break;
				case PREVIOUS:
					navigateBack();
					break;
				default:
					break;
				}
			}
		});

		slide = new SlidePanel();
		slide.setSize("100%", "100%");

		wrapper.add(slide);
		wrapper.setCellHeight(slide, "100%");

		checkToolbarItems();
		initWidget(wrapper);
	}

	public void showNavigationBar(boolean show){
		this.navBar.setVisible(show);
	}

	public void showPreviousButton(boolean show) {
		this.showPreviousButton = show;
		this.checkToolbarItems();
	}
	
	public void showNextButton(boolean show) {
		this.showNextButton = show;
		this.checkToolbarItems();
	}
	
	/**
	 * Clears the navigation data and places the widget
	 * in the argument as the first navigation item
	 */
	public void setHomeWidget(Widget w) {
		navigatables.clear();
		this.slide.clear();
		this.slide.add(w);
		iterator = navigatables.listIterator();
		iterator.add(w);
		checkToolbarItems();
	}

	public boolean hasHomeWidget(){
		return this.navigatables.size() > 0;
	}

	public void navigateTo(Widget w){
		//Removes all the next items
		while(iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}

		//if(this.navigatables.contains(w))
		//	throw new RuntimeException("The navigation item is already inserted in the flow.");

		iterator.add(w);

		disableToolbarItems(true);
		this.slide.slideInto(w, Direction.LEFT, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("The animation has failed");
			}

			@Override
			public void onSuccess(Void result) {
				disableToolbarItems(false);
				checkToolbarItems();
			}
		});
	}

	public boolean navigateForward(){
		if(!iterator.hasNext()){
			checkToolbarItems();
			return false;
		}
		disableToolbarItems(true);
		this.slide.slideInto(iterator.next(), Direction.LEFT, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("The animation has failed");
			}

			@Override
			public void onSuccess(Void result) {
				disableToolbarItems(false);
				checkToolbarItems();
			}
		});
		return true;
	}

	public boolean navigateBack(){
		if(iterator.hasPrevious()) {
			iterator.previous();
			if(!iterator.hasPrevious()){
				iterator.next();
				checkToolbarItems();
				return false;
			}else{
				disableToolbarItems(true);
				this.slide.slideInto(iterator.previous(), Direction.RIGHT, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("The animation has failed");
					}

					@Override
					public void onSuccess(Void result) {
						disableToolbarItems(false);
						checkToolbarItems();
					}
				});
				iterator.next();
			}
		}else
			throw new RuntimeException("Cannot navigate back : iterator is on an illegal index");
		return true;
	}

	public void navigateToFirst(){
		while(iterator.hasPrevious())
			iterator.previous();
		disableToolbarItems(true);
		this.slide.slideInto(iterator.next(), Direction.RIGHT, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("The animation has failed");
			}

			@Override
			public void onSuccess(Void result) {
				disableToolbarItems(false);
				checkToolbarItems();
			}
		});
	}

	public void navigateToLast(){
		while(iterator.hasNext())
			iterator.next();
		iterator.previous();
		disableToolbarItems(true);
		this.slide.slideInto(iterator.next(), Direction.LEFT, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("The animation has failed");
			}

			@Override
			public void onSuccess(Void result) {
				disableToolbarItems(false);
				checkToolbarItems();
			}
		});
		checkToolbarItems();
	}

	private void checkToolbarItems(){
		boolean hasPrevious = false;
		if(iterator.hasPrevious()){
			iterator.previous();
			if(iterator.hasPrevious())
				hasPrevious = true;
			iterator.next();
		}
		navBar.prevButton.setVisible(hasPrevious && showPreviousButton);
		navBar.nextButton.setVisible(iterator.hasNext() && showNextButton);
	}

	private void disableToolbarItems(boolean disable) {
		navBar.nextButton.setEnabled(!disable);
		navBar.prevButton.setEnabled(!disable);
	}
}
