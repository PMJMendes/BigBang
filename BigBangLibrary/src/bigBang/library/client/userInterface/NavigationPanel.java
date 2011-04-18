package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.ListIterator;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.view.View;

public class NavigationPanel extends View {

	protected NavigationToolbar navBar;
	protected SlidePanel slide;

	protected ArrayList<Widget> navigatables;
	protected ListIterator<Widget> iterator;

	public NavigationPanel(){
		navigatables = new ArrayList<Widget>();
		iterator = navigatables.listIterator();

		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		navBar = new NavigationToolbar(); 
		wrapper.add(navBar);

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

	public void navigateTo(Widget w){
		while(iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}
		iterator.add(w);
		navigateToLast();
	}

	public boolean navigateForward(){
		if(!iterator.hasNext()){
			checkToolbarItems();
			return false;
		}
		this.slide.slideInto(iterator.next(), Direction.LEFT);
		checkToolbarItems();
		return true;
	}

	public boolean navigateBack(){
		if(!iterator.hasPrevious()){
			checkToolbarItems();
			return false;
		}
		this.slide.slideInto(iterator.previous(), Direction.RIGHT);
		checkToolbarItems();
		return true;
	}

	public void navigateToFirst(){
		Widget w = null;
		while(iterator.hasPrevious())
			w = iterator.previous();
		this.slide.slideInto(w, Direction.RIGHT);
		checkToolbarItems();
	}

	public void navigateToLast(){
		Widget w = null;
		while(iterator.hasNext())
			w = iterator.next();
		this.slide.slideInto(w, Direction.LEFT);
		checkToolbarItems();
	}

	private void checkToolbarItems(){
		navBar.prevButton.setVisible(iterator.hasPrevious());
		navBar.nextButton.setVisible(iterator.hasNext());
	}
}
