public class TripleDESEncryption
{
   /** The data we are going to encrypt */
   private long original;
   /** All 3 DES keys, the order does matter */
   private DESKey key1;
   private DESKey key2;
   private DESKey key3;
   
   /**
    * Constructor that generates new DES keys
    * @param original - The data to encrypt
    * */
   public TripleDESEncryption(long original)
   {
      this.original = original;
      key1 = new DESKey();
      key2 = new DESKey();
      key3 = new DESKey();
   }
   
   /** Constructor that takes an array of longs for keys
    * @param original - the data to encrypt
    * @param keys - an array of 3 longs to use as keys
    * */
   public TripleDESEncryption(long original, long[] keys)
   {
      this.original = original;
      key1 = new DESKey(keys[0]);
      key2 = new DESKey(keys[1]);
      key3 = new DESKey(keys[2]);
   }
   
   /**
    * Constructor that accepts longs as DES keys. Note that the order of the 
    * keys does matter, it will not decrypt if they are in the wrong order
    * @param original - the data to encrypt
    * @param longKey1 - the first key as a long
    * @param longKey2 - the second key as a long
    * @param longKey3 - the third key as a long
    * */
   public TripleDESEncryption(long original, long longKey1, long longKey2, 
      long longKey3)
   {
      this.original = original;
      this.key1 = new DESKey(longKey1);
      this.key2 = new DESKey(longKey2);
      this.key3 = new DESKey(longKey3);
   }

   /**
    * Constructor that accepts DES keys.  Note that the order of the keys does
    * matter, it will not decrypt if they are in the wrong order.
    * @param original - the data to encrypt
    * @param key1 - the first key
    * @param key2 - the second key
    * @param key3 - the third key
    * */
   public TripleDESEncryption(long original, DESKey key1, DESKey key2, 
           DESKey key3)
   {
      this.original = original;
      this.key1 = key1;
      this.key2 = key2;
      this.key3 = key3;
   }

   /**
    * Encrypts the data in original
    * @return the encrypted value
    * */
   public long encrypt() throws InvalidSelectionException
   {
      /* Encryption is done by encrypting it 3 times with regular DES, 
       * preferably with 3 separate keys.  It encrypts with key1, decrypts 
       * with key2, and encrypts with key3.  This is done so that if you need
       * to stay backwards compatible with regular DES you can; just give it 
       * the same key 3 times.
       * */
      DESEncryption des1 = new DESEncryption(original, key1);
      long current = des1.encrypt();
      DESEncryption des2 = new DESEncryption(current, key2);
      current = des2.decrypt();
      DESEncryption des3 = new DESEncryption(current, key3);
      return des3.encrypt();
   }
   
   /**
    * Decrypts the data in original
    * @return the decrypted value
    * */
   public long decrypt() throws InvalidSelectionException
   {
      DESEncryption des3 = new DESEncryption(original, key3);
      long current = des3.decrypt();
      DESEncryption des2 = new DESEncryption(current, key2);
      current = des2.encrypt();
      DESEncryption des1 = new DESEncryption(current, key1);
      return des1.decrypt();
   }
   
   /**
    * Changes the value of the data to encrypt/decrypt
    * */
   public void changeData(long newLong)
   {
      this.original = newLong;
   }
   
   /**
    * Returns the DES keys
    * @return array with key1 in first element, key2 in second element, 
    *         key3 in third
    * */
   public DESKey[] getKeys()
   {
      return new DESKey[] {key1, key2, key3};
   }
   
   /**
    * Gets the first key
    * @return the first 3DES key
    * */
   public long getKey1()
   {
      return key1.getKey();
   }
   
   /**
    * Gets the second key
    * @return the second 3DES key
    * */
   public long getKey2()
   {
      return key2.getKey();
   }
   
   /**
    * Gets the third key
    * @return the third 3DES key
    * */
   public long getKey3()
   {
      return key3.getKey();
   }
}
