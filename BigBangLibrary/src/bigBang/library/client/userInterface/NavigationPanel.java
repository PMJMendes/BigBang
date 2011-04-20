package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.ListIterator;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.userInterface.NavigationToolbar.NavigationEvent.Navigation;
import bigBang.library.client.userInterface.NavigationToolbar.NavigationEventHandler;
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
		
		navBar.addNavigationEventHandler(new NavigationEventHandler() {
			
			@Override
			public void onNavigationEvent(Navigation n) {
				switch(n) {
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
		while(iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}
		iterator.add(w);
		this.slide.slideInto(w, Direction.LEFT);
		checkToolbarItems();
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
		if(iterator.hasPrevious()) {
			iterator.previous();
			if(!iterator.hasPrevious()){
				iterator.next();
				checkToolbarItems();
				return false;
			}else{
				this.slide.slideInto(iterator.previous(), Direction.RIGHT);
				iterator.next();
				checkToolbarItems();
			}
		}
		return true;
	}

	public void navigateToFirst(){
		Widget w = null;
		while(iterator.hasPrevious())
			w = iterator.previous();
		if(w != null)
			this.slide.slideInto(w, Direction.RIGHT);
		iterator.next();
		checkToolbarItems();
	}

	public void navigateToLast(){
		Widget w = null;
		while(iterator.hasNext())
			w = iterator.next();
		if(w != null)
			this.slide.slideInto(w, Direction.LEFT);
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
		navBar.prevButton.setVisible(hasPrevious);
		navBar.nextButton.setVisible(iterator.hasNext());
	}
}
