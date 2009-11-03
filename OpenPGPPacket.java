import java.util.List;
public class OpenPGPPacket
{
   private byte tag;
   private List<Byte> data;
   public OpenPGPPacket(byte tag, List<Byte> data)
   {
      this.tag = tag;
      this.data = data;
   }

   public byte getTag()
   {
      return tag;
   }

   public List<Byte> getData()
   {
      return data;
   }
   public String toString()
   {
      return "Tag : " + tag;
   }
}

