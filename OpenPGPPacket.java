import java.io.*; //TODO: remove
import java.util.List;
public class OpenPGPPacket
{
   private byte tag;
   private PacketSpecificInterface packetInfo;
   
   public OpenPGPPacket(byte tag, List<Byte> data)
   {
      this.tag = tag;
      switch(tag)
      {
         case OpenPGP.LITERAL_DATA_PACKET_TAG:
            packetInfo = new LiteralDataPacket(data);
            break;
//         case OpenPGP.
      }
   }

   public byte getTag()
   {
      return tag;
   }

   /*public PacketSpecificInterface getPacket()
   {
   }*/

   public String toString() 
   {
      return "Tag : " + tag;
   }
}

