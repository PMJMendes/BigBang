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
	}
	
	public void navigateTo(Widget w){
		while(iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}
		this.navigatables.add(w);
		this.slide.slideInto(w, Direction.LEFT);
	}
	
	public boolean navigateForward(){
		if(!iterator.hasNext())
			return false;
		this.slide.slideInto(iterator.next(), Direction.LEFT);
		return true;
	}
	
	public boolean navigateBack(){
		if(!iterator.hasPrevious())
			return false;
		this.slide.slideInto(iterator.previous(), Direction.RIGHT);
		return true;
	}
	
	public void navigateToFirst(){
		if(iterator.hasPrevious()){
			while(iterator.previous() != null);
			this.slide.slideInto(iterator.previous(), Direction.RIGHT);
		}
	}
	
	public void navigateToLast(){
		if(iterator.hasNext()){
			while(iterator.next() != null);
			this.slide.slideInto(iterator.next(), Direction.LEFT);
		}
	}
}
