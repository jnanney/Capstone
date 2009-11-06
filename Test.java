/*import java.math.BigInteger;
public class Test
{
   public static void main(String[] args)
   {
      TripleDESEncryption des = new TripleDESEncryption(1234);
      DESKey[] keys = des.getKeys();
      String text = "" + keys[0] + keys[1] + keys[2];
      System.out.println("Text is " + text);
      RSAEncryption rsa = new RSAEncryption(text, 1024);
      String encrypt = rsa.encrypt();
      System.out.println("Encrypted " + encrypt);
      rsa.switchText(encrypt);
      String decrypt = rsa.decrypt();
      System.out.println("Decrypted " + decrypt);
   }

}*/
