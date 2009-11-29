import java.io.OutputStream;
import java.math.BigInteger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignaturePacket implements PacketSpecificInterface
{
   private static final byte VERSION = 4;
   private static final byte TYPE = 0x0;
   private static final byte PUBLIC_KEY_ALGORITHM = OpenPGP.RSA_CONSTANT;
   private static final byte HASH_ALGORITHM = OpenPGP.SHA1_CONSTANT; 
   private byte[] signature;

   public SignaturePacket(byte[] data)
   {
   }
   
   public SignaturePacket(File input, RSAPrivateKey key) throws IOException, 
      FileNotFoundException
   {
      byte[] fileData = Common.readAllData(new FileInputStream(input));
      byte[] hashedData = SHA1.hash(fileData);
      RSAEncryption rsa = new RSAEncryption(hashedData, key);
      signature = rsa.decrypt().toByteArray();
   }

   public int getBodyLength()
   {
      return 1 + 1 + 1 + 1 + signature.length;
   }

   public void write(OutputStream out) throws IOException
   {
      out.write(new byte[]{VERSION, TYPE, PUBLIC_KEY_ALGORITHM, 
                           HASH_ALGORITHM});
      out.write(signature);
   }
}
