package org.gwt.mosaic.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SheetPanel_Resources_ie6_default_StaticClientBundleGenerator implements org.gwt.mosaic.ui.client.SheetPanel.Resources {
  public org.gwt.mosaic.ui.client.SheetPanel.Css sheetPanelCss() {
    return sheetPanelCss;
  }
  private void _init0() {
    sheetPanelCss = new org.gwt.mosaic.ui.client.SheetPanel.Css() {
    private boolean injected;
    public boolean ensureInjected() {
      if (!injected) {
        injected = true;
        com.google.gwt.dom.client.StyleInjector.inject(getText());
        return true;
      }
      return false;
    }
    public String getName() {
      return "sheetPanelCss";
    }
    public String getText() {
      return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".GFOE1RNDEG{border:" + ("1p"+ " " +"solid"+ " " +"#000")  + ";background:" + ("white")  + ";}.GFOE1RNDDG{filter:" + ("\"progid:DXImageTransform.Microsoft.dropShadow(color=#818181, offX=7, offY=7, positive=true)\"")  + ";}")) : ((".GFOE1RNDEG{border:" + ("1p"+ " " +"solid"+ " " +"#000")  + ";background:" + ("white")  + ";}.GFOE1RNDDG{filter:" + ("\"progid:DXImageTransform.Microsoft.dropShadow(color=#818181, offX=7, offY=7, positive=true)\"")  + ";}"));
    }
    public java.lang.String open(){
      return "GFOE1RNDDG";
    }
    public java.lang.String sheet(){
      return "GFOE1RNDEG";
    }
  }
  ;
  }
  
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static org.gwt.mosaic.ui.client.SheetPanel.Css sheetPanelCss;
  
  static {
    new SheetPanel_Resources_ie6_default_StaticClientBundleGenerator()._init0();
  }
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      sheetPanelCss(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("sheetPanelCss", sheetPanelCss());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'sheetPanelCss': return this.@org.gwt.mosaic.ui.client.SheetPanel.Resources::sheetPanelCss()();
    }
    return null;
  }-*/;
}
