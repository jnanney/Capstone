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
   private BigInteger signature;

   public SignaturePacket(byte[] data) throws MalformedPacketException
   {
      int i = 0;
      if(data[i++] != VERSION)
      {
         throw new MalformedPacketException("Signature version not supported");
      }
      if(data[i++] != TYPE)
      {
         throw new MalformedPacketException("Only binary signatures are " +
            "supported");
      }
      if(data[i++] != PUBLIC_KEY_ALGORITHM)
      {
         throw new MalformedPacketException("RSA is the only supported public" 
            + " key algorithm");
      }
      if(data[i++] != HASH_ALGORITHM)
      {
         throw new MalformedPacketException("SHA-1 is the only supported " + 
            "hash algorithm");
      }
      byte[] signatureData = OpenPGP.getMultiprecisionInteger(data, i); 
      signature = new BigInteger(Common.SIGN, signatureData);
   }
   
   public SignaturePacket(File input, RSAPrivateKey key) throws IOException, 
      FileNotFoundException
   {
      byte[] fileData = Common.readAllData(new FileInputStream(input));
      byte[] hashedData = SHA1.hash(fileData);
      RSAEncryption rsa = new RSAEncryption(hashedData, key);
      signature = rsa.decrypt();
   }

   public BigInteger getSignature()
   {
      return signature;
   }

   public int getBodyLength()
   {
      return 1 + 1 + 1 + 1 + OpenPGP.MPI_LENGTH_BYTES + 
         signature.toByteArray().length;
   }

   public void write(OutputStream out) throws IOException
   {
      out.write(new byte[]{VERSION, TYPE, PUBLIC_KEY_ALGORITHM, 
                           HASH_ALGORITHM});
      out.write(OpenPGP.makeMultiprecisionInteger(signature));
   }
}
