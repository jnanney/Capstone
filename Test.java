import java.math.BigInteger;
import java.util.Formatter;
import java.util.List;
public class Test
{
   public static void main(String[] args)
   {
	   RSAEncryption test = new RSAEncryption("Hello, this is my message ",1024);//and it is very very long it's so long that I'm just going to keep extending it until it is so long that crap gotta go", 1024);
	   System.out.println("Cypher: " + test.encrypt());
	   System.out.println("Original: " + test.decrypt());
   }


}
