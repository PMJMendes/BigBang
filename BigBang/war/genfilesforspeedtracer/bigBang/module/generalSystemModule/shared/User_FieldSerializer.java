package bigBang.module.generalSystemModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class User_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.User_FieldSerializer.deserialize(reader, (bigBang.module.generalSystemModule.shared.User)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.generalSystemModule.shared.User_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.User_FieldSerializer.serialize(writer, (bigBang.module.generalSystemModule.shared.User)object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.generalSystemModule.shared.User.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.generalSystemModule.shared.User instance) throws SerializationException {
    instance.email = streamReader.readString();
    instance.id = streamReader.readString();
    instance.name = streamReader.readString();
    instance.role = (bigBang.module.generalSystemModule.shared.UserRole) streamReader.readObject();
    instance.username = streamReader.readString();
    
  }
  
  public static bigBang.module.generalSystemModule.shared.User instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new bigBang.module.generalSystemModule.shared.User();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.generalSystemModule.shared.User instance) throws SerializationException {
    streamWriter.writeString(instance.email);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.name);
    streamWriter.writeObject(instance.role);
    streamWriter.writeString(instance.username);
    
  }
  
}
