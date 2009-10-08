import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;
public class RSAEncryption 
{
   private String original;
   private String cyphertext;
   private RSAKey key;

   public RSAEncryption(String message, int keylength)
   {
	  original = message;
      key = new RSAKey(keylength);
   }
   
   public String encrypt()
   {
	   BigInteger n = key.getPrimeProduct();
	   String numString = Common.makeNumberString(original);
	   String splitText[] = Common.split(numString, n.toString().length() - 1);
	   cyphertext = "";
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
	   String splitText[] = Common.split(cyphertext, length);
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


}
