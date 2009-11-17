import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;
public class RSAEncryption 
{
   private RSABaseKey key;
   private BigInteger original;
   int SIGN=1;


   public RSAEncryption(byte[] data, RSABaseKey key)
   {
      original = new BigInteger(SIGN, data);
      this.key = key;
   }

   public RSAEncryption(BigInteger original, RSABaseKey key)
   {
      this.original = original;
      this.key = key;
   }

   public RSAEncryption(byte[] data, int keylength)
   {
      original = new BigInteger(SIGN, data);
      this.key = new RSAPrivateKey(keylength);
   }

   public BigInteger encrypt()
   {
	   BigInteger n = key.getPrimeProduct();
      if(original.compareTo(n) > 0)
      {
         System.err.println("Message too large.  Implement splitting");
      }

      BigInteger cyphertext = original.modPow(key.getEncryptionExponent(), n);
	   return cyphertext;
   }
   
   public BigInteger decrypt()
   {
      if(!(key instanceof RSAPrivateKey))
      {
         System.err.println("Messages may only be decrypted with private keys");
         System.exit(1);
      }

      RSAPrivateKey privateKey = (RSAPrivateKey) key;
	   BigInteger n = key.getPrimeProduct();
      BigInteger result = original.modPow(privateKey.getDecryptionExponent(), n);
	   return result;
   }

   public void switchOriginal(BigInteger newText)
   {
      this.original = newText;
   }

}
