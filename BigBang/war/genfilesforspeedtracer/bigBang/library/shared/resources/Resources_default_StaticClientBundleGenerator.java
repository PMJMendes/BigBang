package bigBang.library.shared.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Resources_default_StaticClientBundleGenerator implements bigBang.library.shared.resources.Resources {
  public com.google.gwt.resources.client.ImageResource viewIconMediumBlack() {
    return viewIconMediumBlack;
  }
  public com.google.gwt.resources.client.ImageResource viewIconMediumWhite() {
    return viewIconMediumWhite;
  }
  public com.google.gwt.resources.client.ImageResource viewIconSmallBlack() {
    return viewIconSmallBlack;
  }
  public com.google.gwt.resources.client.ImageResource viewIconSmallWhite() {
    return viewIconSmallWhite;
  }
  private void _init0() {
    viewIconMediumBlack = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "viewIconMediumBlack",
    bundledImage_None,
    32, 0, 32, 32, false, false
  );
    viewIconMediumWhite = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "viewIconMediumWhite",
    bundledImage_None,
    0, 0, 32, 32, false, false
  );
    viewIconSmallBlack = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "viewIconSmallBlack",
    bundledImage_None,
    64, 16, 16, 16, false, false
  );
    viewIconSmallWhite = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "viewIconSmallWhite",
    bundledImage_None,
    64, 0, 16, 16, false, false
  );
  }
  
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String bundledImage_None = GWT.getModuleBaseURL() + "A0979953D039F38138E7C75F90E91F04.cache.png";
  private static com.google.gwt.resources.client.ImageResource viewIconMediumBlack;
  private static com.google.gwt.resources.client.ImageResource viewIconMediumWhite;
  private static com.google.gwt.resources.client.ImageResource viewIconSmallBlack;
  private static com.google.gwt.resources.client.ImageResource viewIconSmallWhite;
  
  static {
    new Resources_default_StaticClientBundleGenerator()._init0();
  }
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      viewIconMediumBlack(), 
      viewIconMediumWhite(), 
      viewIconSmallBlack(), 
      viewIconSmallWhite(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("viewIconMediumBlack", viewIconMediumBlack());
        resourceMap.put("viewIconMediumWhite", viewIconMediumWhite());
        resourceMap.put("viewIconSmallBlack", viewIconSmallBlack());
        resourceMap.put("viewIconSmallWhite", viewIconSmallWhite());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'viewIconMediumBlack': return this.@bigBang.library.shared.resources.Resources::viewIconMediumBlack()();
      case 'viewIconMediumWhite': return this.@bigBang.library.shared.resources.Resources::viewIconMediumWhite()();
      case 'viewIconSmallBlack': return this.@bigBang.library.shared.resources.Resources::viewIconSmallBlack()();
      case 'viewIconSmallWhite': return this.@bigBang.library.shared.resources.Resources::viewIconSmallWhite()();
    }
    return null;
  }-*/;
}
