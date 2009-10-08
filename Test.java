import java.math.BigInteger;
import java.util.Formatter;
public class Test
{
   public static void main(String[] args)
   {
	   RSAEncryption test = new RSAEncryption("Hello, this is my message", 1024);
	   System.out.println("Cypher: " + test.encrypt());
	   System.out.println("Original: " + test.decrypt());
   }


}
