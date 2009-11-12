import org.junit.Assert;

public class UnitTests
{
   @org.junit.Test public void basicDESTest() throws Exception
   {
      long original = 1234567;
      DESEncryption des = new DESEncryption(original);
      long encrypted = des.encrypt();
      DESEncryption desDecrypt = new DESEncryption(encrypted, des.getKey());
      long decrypted = desDecrypt.decrypt();
      org.junit.Assert.assertTrue(encrypted != decrypted);
      org.junit.Assert.assertTrue(decrypted == original);
   }

   @org.junit.Test public void basic3DESTest() throws Exception
   {
      long original = 1234567;
      TripleDESEncryption des = new TripleDESEncryption(original);
      long encrypted = des.encrypt();
      des.changeOriginal(encrypted);
      long decrypted = des.decrypt();
      org.junit.Assert.assertTrue(encrypted != decrypted);
      org.junit.Assert.assertTrue(decrypted == original);
   }
}
