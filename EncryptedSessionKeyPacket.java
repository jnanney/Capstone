import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
public class EncryptedSessionKeyPacket implements PacketSpecificInterface
{
   private static final byte VERSION = 3;
   private BigInteger encryptedKey;
   private byte[] keyID;
   private static final int KEY_ID_SIZE = 8;
   
   public EncryptedSessionKeyPacket(RSABaseKey rsaKey, byte[] unencryptedKey)
   {
      RSAEncryption rsa = new RSAEncryption(unencryptedKey, rsaKey);
      encryptedKey = rsa.encrypt();
      keyID = rsaKey.getKeyID();
   }
   
   public EncryptedSessionKeyPacket(byte[] data)
   {
      int i = 0;
      keyID = new byte[KEY_ID_SIZE];
      if(data[i++] != VERSION)
      {
         System.err.println("Version " + data[i - 1] + " is not supported");
      }
      for(int j = 0; j < KEY_ID_SIZE; i++, j++)
      {
         keyID[j] = data[i];
      }
      if(data[i++] != OpenPGP.RSA_CONSTANT)
      {
         System.err.println("Invalid Public key constant " + data[i - 1]);
      }
      byte[] key = OpenPGP.getMultiprecisionInteger(data, i);
      encryptedKey = new BigInteger(1, key); //XXX: hopefully this will help.  Makes number positive
      //encryptedKey = new BigInteger(key);
   }

   public byte[] getKeyID()
   {
      return keyID;
   }

   public BigInteger getEncryptedKey()
   {
      return encryptedKey;
   }

   public void write(FileOutputStream output) throws IOException
   {
      output.write(new byte[]{VERSION});
      output.write(keyID);
      output.write(new byte[]{OpenPGP.RSA_CONSTANT});
      output.write(OpenPGP.makeMultiprecisionInteger(encryptedKey));
   }

   public int getBodyLength()
   {
      //2 is for MPI
      return 1 + keyID.length + encryptedKey.toByteArray().length + 2 + 1;
   }
}
