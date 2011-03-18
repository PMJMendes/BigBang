package bigBang.module.generalSystemModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class User_Array_Rank_1_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer.deserialize(reader, (bigBang.module.generalSystemModule.shared.User[])object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.User_Array_Rank_1_FieldSerializer.serialize(writer, (bigBang.module.generalSystemModule.shared.User[])object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.generalSystemModule.shared.User[].class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.generalSystemModule.shared.User[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static bigBang.module.generalSystemModule.shared.User[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new bigBang.module.generalSystemModule.shared.User[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.generalSystemModule.shared.User[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
}
