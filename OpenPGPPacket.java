import java.io.*; //TODO: remove
import java.util.List;
public class OpenPGPPacket
{
   private byte tag;
   private PacketSpecificInterface packetInfo;
   
   public OpenPGPPacket(byte tag, byte[] data) 
   {
      this.tag = tag;
      switch(tag)
      {
         case OpenPGP.LITERAL_DATA_PACKET_TAG:
            packetInfo = new LiteralDataPacket(data);
            break;
         case OpenPGP.PK_SESSION_KEY_TAG:
            packetInfo = new EncryptedSessionKeyPacket(data);
            break;
         case OpenPGP.SYMMETRIC_DATA_TAG:
            packetInfo = new SymmetricDataPacket(data);
            break;
         case OpenPGP.PUBLIC_KEY_PACKET_TAG:
            packetInfo = new RSAPublicKey(data);
         case OpenPGP.PRIVATE_KEY_PACKET_TAG:
            packetInfo = new RSAPrivateKey(data);
         default:
            System.err.println("Tag " + tag + " is not a supported tag");
      }
   }

   public byte getTag()
   {
      return tag;
   }

   public PacketSpecificInterface getPacket()
   {
      return packetInfo;
   }

   public String toString() 
   {
      //XOR removes the 2 1-bits that are required to be at start of tag
      return "Tag : " + (tag ^ OpenPGP.NEW_TAG_MASK);
   }
}

