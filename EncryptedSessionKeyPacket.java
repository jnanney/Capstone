public class EncryptedSessionKeyPacket implements PacketSpecificInterface
{
   private static final byte VERSION = 3;
   private byte[] encryptedKey;
   private byte[] keyID;
   private static final int KEY_ID_SIZE = 8;
   public EncryptedSessionKeyPacket(byte[] data)
   {
      int i = 0;
      if(data[i++] != VERSION)
      {
         System.err.println("Version " + data[0] + " is not supported");
      }
      for(int j = 0; j < KEY_ID_SIZE; i++, j++)
      {
         keyID[j] = data[i];
      }
      if(data[i++] != RSA_CONSTANT)
      {
         System.err.println("RSA is the only public key algorithm that is supported");
      }
      int length = (data[i++] << 8) | data[i++];
      encryptedKey = new byte[length / Byte.SIZE];
      for(int j = 0; j < encryptedKey.length && i < data.length; i++, j++)
      {
         encryptedKey[j] = data[i];
      }
   }

   public byte[] getKeyID()
   {
      return keyID;
   }

   public byte[] getEncryptedKey()
   {
      return encryptedKey;
   }
}
