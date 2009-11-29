import java.io.OutputStream;
import java.math.BigInteger;

public class SignaturePacket implements PacketSpecificInterface
{
   private static final byte VERSION = 4;
   private static final byte TYPE = 0x0;
   private static final byte PUBLIC_KEY_ALGORITHM = OpenPGP.RSA_CONSTANT;
   private static final byte HASH_ALGORITHM = OpenPGP.SHA1_CONSTANT; 
   private static final BigInteger signature;

   public SignaturePacket(byte[] data)
   {
   }
   
   public SignaturePacket(File input)
   {
      byte[] fileData = Common.readAllData(new FileInputStream(input));
      SHA1.hash(fileData);
   }

   public int getBodyLength()
   {
      return 0;
   }

   public void write(OutputStream out)
   {
   }
}
