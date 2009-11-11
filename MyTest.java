import java.math.BigInteger;
public class MyTest
{
   public static void main(String[] args)
   {
      long value = -2330047853866800956L;
      System.out.println("Original is " + value);
      byte[] temp = Common.makeLongBytes(value);
      System.out.println("Original as Big " + new BigInteger(1,temp));
      RSAPrivateKey key = new RSAPrivateKey(1024);
      RSAEncryption rsa = new RSAEncryption(temp, key);
      BigInteger result = rsa.encrypt();
      System.out.println("Result is " + result);
      byte[] asBytes = OpenPGP.makeMultiprecisionInteger(result);

      rsa.switchOriginal(new BigInteger(OpenPGP.getMultiprecisionInteger(asBytes, 0)));
      BigInteger decrypted = rsa.decrypt();
      System.out.println("decrypted is " + decrypted);
      System.out.println("and as long " + decrypted.longValue());
   }
}
