package bigBang.module.generalSystemModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class UserRole_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer.deserialize(reader, (bigBang.module.generalSystemModule.shared.UserRole)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.UserRole_FieldSerializer.serialize(writer, (bigBang.module.generalSystemModule.shared.UserRole)object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.generalSystemModule.shared.UserRole.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.generalSystemModule.shared.UserRole instance) throws SerializationException {
    instance.id = streamReader.readString();
    instance.name = streamReader.readString();
    
  }
  
  public static bigBang.module.generalSystemModule.shared.UserRole instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new bigBang.module.generalSystemModule.shared.UserRole();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.generalSystemModule.shared.UserRole instance) throws SerializationException {
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.name);
    
  }
  
}
