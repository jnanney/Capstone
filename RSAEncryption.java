import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;
public class RSAEncryption 
{
   private String text;
   private RSABaseKey key;
   private BigInteger original;

   public RSAEncryption(String message, int keylength)
   {
      text = message;
      key = new RSAPrivateKey(keylength);
   }
   public RSAEncryption(byte[] data, RSABaseKey key)
   {
      BigInteger original = new BigInteger(data);
      this.key = key;
   }

   public RSAEncryption(byte[] data, int keylength)
   {
      BigInteger original = new BigInteger(data);
      this.key = new RSAPrivateKey(keylength);
   }

   public RSAEncryption(String message, RSABaseKey key)
   {
      this.text = message;
      this.key = key;
   }
   
   /*public BigInteger encrypt()
   {
	   BigInteger n = key.getPrimeProduct();
	   String numString = Common.makeNumberString(text);
	   String splitText[] = Common.split(numString, n.toString().length() - 1);
	   String cyphertext = "";
	   for(String current : splitText)
	   {
		   BigInteger num = new BigInteger(current);
		   cyphertext +=  num.modPow(key.getEncryptionExponent(), n);
	   }
	   return new BigInteger(cyphertext);
   }*/

   public BigInteger encrypt()
   {
	   BigInteger n = key.getPrimeProduct();
	   String numString = original.toString();
	   String splitText[] = Common.split(numString, n.toString().length() - 1);
	   String cyphertext = "";
	   for(String current : splitText)
	   {
		   BigInteger num = new BigInteger(current);
		   cyphertext +=  num.modPow(key.getEncryptionExponent(), n);
	   }
	   return new BigInteger(cyphertext);
   }
   
   public String decrypt()
   {
      if(!(key instanceof RSAPrivateKey))
      {
         System.err.println("Messages may only be decrypted with private keys");
         System.exit(1);
      }
      RSAPrivateKey privateKey = (RSAPrivateKey) key;
	   BigInteger n = privateKey.getPrimeProduct();
	   String result = "";
	   int length = privateKey.getPrimeProduct().toString().length();
	   String splitText[] = Common.split(text, length);
	   for(String current : splitText)
	   {
		   BigInteger temp = new BigInteger(current);
		   temp = temp.modPow(privateKey.getDecryptionExponent(), n);
		   result += temp.toString();
	   }
	   result = Common.addLeadingZeros(new BigInteger(result), Common.CHAR_SIZE);
	   result = Common.makeCharString(result);
	   return result;
   }

   public void switchText(String newText)
   {
      this.text = newText;
   }

}
