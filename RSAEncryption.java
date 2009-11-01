import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;
public class RSAEncryption 
{
   private String text;
   private RSAKey key;

   public RSAEncryption(String message, int keylength)
   {
      text = message;
      key = new RSAKey(keylength);
   }

   public RSAEncryption(String message, RSAKey key)
   {
      this.key = key;
   }
   
   public String encrypt()
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
	   return cyphertext;
   }
   
   public String decrypt()
   {
	   BigInteger n = key.getPrimeProduct();
	   String result = "";
	   int length = key.getPrimeProduct().toString().length();
	   String splitText[] = Common.split(text, length - 1);
	   for(String current : splitText)
	   {
		   BigInteger temp = new BigInteger(current);
		   temp = temp.modPow(key.getDecryptionExponent(), n);
		   result += temp.toString();
	   }
	   result = Common.addLeadingZeros(new BigInteger(result), Common.CHAR_SIZE);
	   result = Common.makeCharString(result);
	   return result;
   }

   public RSAKey getKey()
   {
      return key;
   }

   public void switchText(String newText)
   {
      this.text = newText;
   }

}
