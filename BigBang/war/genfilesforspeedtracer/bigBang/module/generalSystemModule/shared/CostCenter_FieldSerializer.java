package bigBang.module.generalSystemModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class CostCenter_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer.deserialize(reader, (bigBang.module.generalSystemModule.shared.CostCenter)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.generalSystemModule.shared.CostCenter_FieldSerializer.serialize(writer, (bigBang.module.generalSystemModule.shared.CostCenter)object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.generalSystemModule.shared.CostCenter.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.generalSystemModule.shared.CostCenter instance) throws SerializationException {
    instance.code = streamReader.readString();
    instance.id = streamReader.readString();
    instance.managerId = streamReader.readString();
    instance.members = (bigBang.module.generalSystemModule.shared.User[]) streamReader.readObject();
    instance.name = streamReader.readString();
    
  }
  
  public static bigBang.module.generalSystemModule.shared.CostCenter instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new bigBang.module.generalSystemModule.shared.CostCenter();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.generalSystemModule.shared.CostCenter instance) throws SerializationException {
    streamWriter.writeString(instance.code);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.managerId);
    streamWriter.writeObject(instance.members);
    streamWriter.writeString(instance.name);
    
  }
  
}
