import java.io.FileOutputStream;
public class SymmetricDataPacket implements PacketSpecificInterface
{
   private byte[] encryptedData;
   public SymmetricDataPacket(byte[] rawData)
   {
      encryptedData = rawData;
   }

   public byte[] getEncryptedData()
   {
      return encryptedData;
   }

   public void write(FileOutputStream output)
   {
   }

   public int getBodyLength()
   {
      return encryptedData.length;
   }
}
