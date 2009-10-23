public class TripleDESEncryption
{
   long original;
   DESKey key1;
   DESKey key2;
   DESKey key3;

   public TripleDESEncryption(long original)
   {
      this.original = original;
      key1 = new DESKey();
      key2 = new DESKey();
      key3 = new DESKey();
   }

   public long encrypt() throws InvalidSelectionException
   {
      DESEncryption des1 = new DESEncryption(original, key1);
      long current = des1.encrypt();
      DESEncryption des2 = new DESEncryption(current, key2);
      current = des2.decrypt();
      DESEncryption des3 = new DESEncryption(current, key3);
      return des3.encrypt();
   }

   public long decrypt() throws InvalidSelectionException
   {
      DESEncryption des3 = new DESEncryption(original, key3);
      long current = des3.decrypt();
      DESEncryption des2 = new DESEncryption(current, key2);
      current = des2.encrypt();
      DESEncryption des1 = new DESEncryption(current, key1);
      return des1.decrypt();
   }

   public void changeOriginal(long newLong)
   {
      this.original = newLong;
   }
}
