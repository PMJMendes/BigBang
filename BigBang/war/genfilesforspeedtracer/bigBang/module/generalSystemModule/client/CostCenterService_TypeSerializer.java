package bigBang.module.generalSystemModule.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.rpc.impl.TypeHandler;
import java.util.HashMap;
import java.util.Map;

public class CostCenterService_TypeSerializer extends com.google.gwt.user.client.rpc.impl.SerializerBase {
  private static final Map<String, TypeHandler> methodMapJava;
  private static final MethodMap methodMapNative;
  private static final Map<Class<?>, String> signatureMapJava;
  private static final JsArrayString signatureMapNative;
  
  static {
    if (GWT.isScript()) {
      methodMapJava = null;
      methodMapNative = loadMethodsNative();
      signatureMapJava = null;
      signatureMapNative = loadSignaturesNative();
    } else {
      methodMapJava = loadMethodsJava();
      methodMapNative = null;
      signatureMapJava = loadSignaturesJava();
      signatureMapNative = null;
    }
  }
  
  @SuppressWarnings("deprecation")
  private static Map<String, TypeHandler> loadMethodsJava() {
    Map<String, TypeHandler> result = new HashMap<String, TypeHandler>();
    result.put("bigBang.module.generalSystemModule.shared.CostCenter/3484916152", new bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer.Handler());
    result.put("[LbigBang.module.generalSystemModule.shared.CostCenter;/1106432322", new bigBang.module.generalSystemModule.shared.CostCenter_Array_Rank_1_FieldSerializer.Handler());
    result.put("bigBang.module.generalSystemModule.shared.User/1409815280", new bigBang.module.generalSystemModule.shared.User_FieldSerializer.Handler());
    result.put("bigBang.module.generalSystemModule.shared.UserRole/2061322005", new bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer.Handler());
    result.put("[LbigBang.module.generalSystemModule.shared.User;/413860029", new bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer.Handler());
    result.put("com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533", new com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer.Handler());
    result.put("java.lang.String/2004016611", new com.google.gwt.user.client.rpc.core.java.lang.String_FieldSerializer.Handler());
    result.put("[Ljava.lang.String;/2600011424", new com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer.Handler());
    return result;
  }
  
  @SuppressWarnings("deprecation")
  private static native MethodMap loadMethodsNative() /*-{
    var result = {};
    result["bigBang.module.generalSystemModule.shared.CostCenter/3484916152"] = [
        @bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;LbigBang/module/generalSystemModule/shared/CostCenter;),
        @bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;LbigBang/module/generalSystemModule/shared/CostCenter;)
      ];
    
    result["[LbigBang.module.generalSystemModule.shared.CostCenter;/1106432322"] = [
        @bigBang.module.generalSystemModule.shared.CostCenter_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @bigBang.module.generalSystemModule.shared.CostCenter_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[LbigBang/module/generalSystemModule/shared/CostCenter;),
      ];
    
    result["bigBang.module.generalSystemModule.shared.User/1409815280"] = [
        @bigBang.module.generalSystemModule.shared.User_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @bigBang.module.generalSystemModule.shared.User_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;LbigBang/module/generalSystemModule/shared/User;),
        @bigBang.module.generalSystemModule.shared.User_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;LbigBang/module/generalSystemModule/shared/User;)
      ];
    
    result["bigBang.module.generalSystemModule.shared.UserRole/2061322005"] = [
        @bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;LbigBang/module/generalSystemModule/shared/UserRole;),
        @bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;LbigBang/module/generalSystemModule/shared/UserRole;)
      ];
    
    result["[LbigBang.module.generalSystemModule.shared.User;/413860029"] = [
        @bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[LbigBang/module/generalSystemModule/shared/User;),
        @bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[LbigBang/module/generalSystemModule/shared/User;)
      ];
    
    result["com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533"] = [
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;)
      ];
    
    result["java.lang.String/2004016611"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/String;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/String;)
      ];
    
    result["[Ljava.lang.String;/2600011424"] = [
        ,
        ,
        @com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Ljava/lang/String;)
      ];
    
    return result;
  }-*/;
  
  @SuppressWarnings("deprecation")
  private static Map<Class<?>, String> loadSignaturesJava() {
    Map<Class<?>, String> result = new HashMap<Class<?>, String>();
    result.put(bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer.concreteType(), "bigBang.module.generalSystemModule.shared.CostCenter/3484916152");
    result.put(bigBang.module.generalSystemModule.shared.CostCenter_Array_Rank_1_FieldSerializer.concreteType(), "[LbigBang.module.generalSystemModule.shared.CostCenter;/1106432322");
    result.put(bigBang.module.generalSystemModule.shared.User_FieldSerializer.concreteType(), "bigBang.module.generalSystemModule.shared.User/1409815280");
    result.put(bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer.concreteType(), "bigBang.module.generalSystemModule.shared.UserRole/2061322005");
    result.put(bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer.concreteType(), "[LbigBang.module.generalSystemModule.shared.User;/413860029");
    result.put(com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer.concreteType(), "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533");
    result.put(com.google.gwt.user.client.rpc.core.java.lang.String_FieldSerializer.concreteType(), "java.lang.String/2004016611");
    result.put(com.google.gwt.user.client.rpc.core.java.lang.String_Array_Rank_1_FieldSerializer.concreteType(), "[Ljava.lang.String;/2600011424");
    return result;
  }
  
  @SuppressWarnings("deprecation")
  private static native JsArrayString loadSignaturesNative() /*-{
    var result = [];
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@bigBang.module.generalSystemModule.shared.CostCenter::class)] = "bigBang.module.generalSystemModule.shared.CostCenter/3484916152";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@bigBang.module.generalSystemModule.shared.CostCenter[]::class)] = "[LbigBang.module.generalSystemModule.shared.CostCenter;/1106432322";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@bigBang.module.generalSystemModule.shared.User::class)] = "bigBang.module.generalSystemModule.shared.User/1409815280";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@bigBang.module.generalSystemModule.shared.UserRole::class)] = "bigBang.module.generalSystemModule.shared.UserRole/2061322005";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@bigBang.module.generalSystemModule.shared.User[]::class)] = "[LbigBang.module.generalSystemModule.shared.User;/413860029";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException::class)] = "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.lang.String::class)] = "java.lang.String/2004016611";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.lang.String[]::class)] = "[Ljava.lang.String;/2600011424";
    return result;
  }-*/;
  
  public CostCenterService_TypeSerializer() {
    super(methodMapJava, methodMapNative, signatureMapJava, signatureMapNative);
  }
  
}
