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
}
