/**
 * This class performs DES encryption.  The main reference I used was the 
 * Federal Information Processing Standard (FIPS) 46.  Consult that for more
 * information.
 * @author - Jonathan Nanney
 * */
public class DESEncryption
{
   /** the text to encrypt/decrypt */
   private long original;
   /** The key to use for encryption/decryption */
   private DESKey key;
   /** The number of iterations in the algorithm */
   private static final int ENCRYPTION_ITERATIONS = 16;
   /** First bit, the standard refers to the MSB as bit 1 so that's how I do 
    * it here*/
   private static final int FIRST_BIT = 1;
   /** The middle bit*/
   private static final int MIDDLE_BIT = 32;
   /** The last bit */
   private static final int LAST_BIT = 64;

   /**
    * Constructor that will generate a new DES key.
    * @param original - the data to encrypt/decrypt
    * */
   public DESEncryption(long original)
   {
      this.original = original;
      key = new DESKey();
   }
   
   /**
    * Constructor that takes an existing DES key.
    * @param original - the data to encrypt/decrypt
    * @param key - the DES key to use for encryption/decryption
    * */
   public DESEncryption(long original, DESKey key)
   {
      this.original = original;
      this.key = key;
   }
   
   /**
    * Encrypts the data
    * @return the encrypted value.
    * */
   public long encrypt() throws InvalidSelectionException
   {
      //Calls this because decryption is the same as encryption but applied in
      //reverse.  The false just says run the algorithm in order not in reverse
      return encryptCycle(false);
   }
   
   /**
    * This is the method that performs one iteration of the algorithm
    * @param left - the left half of the number i.e bits 1-32
    * @param right - the right half of the number i.e bits 33-64
    * @param iteration - the value of this iteration.  This matters because 
    *                    a different part of the key is used depending on the
    *                    iteration
    * @return the result of this iteration
    * */
   private long encryptionIteration(long left, long right, int iteration)
      throws InvalidSelectionException
   {
      //Puts the right half from the previous iteration in the left half
      long result = right << 32;
      //Gets the specific part of the key used for this iteration
      long shortkey = key.keyScheduler(iteration);  
      //The result of the cypher function
      long cypherResult = cypherFunction(right, shortkey);
      //Right half of the number is the result of the cypher function XOR'ed 
      //with the left half from the previous iteration
      result = result | (left ^ cypherResult);
      return result;
   }
   
   /**
    * The cypher function takes 32 bits makes it 48 bits and then XORs that 
    * with the 48 bits of the shortkey.  Then it takes this value down to 32 
    * bits
    * @param block - a 32 bit number, probably the right half of the number
    * @param shortkey - a 48 bit portion of the key
    * @return the result of the cypher function.
    * */
   private long cypherFunction(long block, long shortKey) 
      throws InvalidSelectionException
   {
      //the result of the cypher function
      long result = 0;
      //Move the block into the most significant places so it will work well 
      //the switchBits function
      block = block << 32;
      //Expands the block into 48 bits by duplicating some of the bits
      long expandedBlock = Common.switchBits(block, 
                                             DESArrays.getExpandedPositions());
      //Move the 48 bits into the least significant places so it can be easily
      //XOR'ed with the shortKey
      expandedBlock = expandedBlock >>> 16;
      long temp = shortKey ^ expandedBlock;
      //Move this 48 bit value into the most significant places for easy access
      temp = temp << 16;
      //This loop takes each 6 bits of the number and reduces it to 4 bits so 
      //the final number will be 32 bits.
      for(int i = 0; i < 8; i++)
      {
         byte sixBits =  (byte) Common.getBits(temp, i * 6 + 1, (i + 1) * 6);
         byte[][] sTable = DESArrays.getSelectionTables(i + 1);
         byte fourBits = sFunction(sixBits, sTable);
         result = result << 4;
         result = result | fourBits;
      }
      //Moves the result to the most significant places so that it will work
      //with switchBits
      result = result << 32;
      result = Common.switchBits(result, DESArrays.getPermutationFunction());
      //Move the bits back to the least significant places so that the user 
      //can use it easily
      result = result >>> 32;
      return result;
   }
   
   /**
    * Decrypts the data
    * @return the decrypted value
    * */
   public long decrypt() throws InvalidSelectionException
   {
      // Calls encryptCycle because the algorithm is the same as 
      // encrypt only applied in reverse. The true says go in reverse
      return encryptCycle(true);
   }
   
   /**
    * The s function takes a 6 bit number and returns it as a 4 bit number 
    * based on the given table.  The first and last bit of the number give the
    * row index for the table and the middle 4 bits give the column index.  The
    * 4 bit number is the number at that row and column in the table.
    * @param sixBits - the number to translate
    * @param sTable - the table that tells it how to translate the number
    * @return the 4 bit number
    * */
   public byte sFunction(byte sixBits, byte[][] sTable) 
      throws InvalidSelectionException
   {
      byte firstBitPosition = 3;
      byte lastBitPosition = 8;
      //get first and last bit as the row, get the middle 4 bits as the col
      int row = (Common.getBit(sixBits, firstBitPosition) << 1) | 
         (Common.getBit(sixBits, lastBitPosition));
      int col = 0;
      for(int i = firstBitPosition + 1; i < lastBitPosition; i++)
      {
         col = col | (Common.getBit(sixBits, i) << (7 - i));
      }
      return sTable[row][col];
   }
   
   /**
    * Returns the DES key
    * */
   public DESKey getKey()
   {
      return key;
   }
   
   /**
    * Since encrypt() and decrypt() are both using the same algorithm 
    * (only when decrypt uses it it starts at iteration 16 and goes down to 1)
    * the algorithm is implemented here.  encrypt() and decrypt() both call 
    * this method.
    * @param backwards - if true the method will go from 16 down to 1.  Used by
    *                    decrypt()
    * @return - the encrypted or decrypted value
    * */
   private long encryptCycle(boolean backwards)
      throws InvalidSelectionException
   {
      int START = 1;
      long result = Common.switchBits(original, DESArrays.getInitialPermutation());
      long left = Common.getBits(result, FIRST_BIT, MIDDLE_BIT);
      long right = Common.getBits(result, MIDDLE_BIT, LAST_BIT);
      int iteration;
      for (int i = START; i <= ENCRYPTION_ITERATIONS; i++)
      {
         iteration = i;
         if (backwards)
         {
            //+1 because we need to go from 16 down to 1.  But we start 
            //counting at 1.  16 - 1 = 15 so it would skip an iteration
            iteration = ENCRYPTION_ITERATIONS - i + 1;
         }
         result = encryptionIteration(left, right, iteration);
         left = Common.getBits(result, FIRST_BIT, MIDDLE_BIT);
         right = Common.getBits(result, MIDDLE_BIT, LAST_BIT);
      }
      result = Common.switchBits(result, DESArrays.getFlipPositions());
      result = Common.switchBits(result, DESArrays.getInversePermutation());
      return result;
   }
}
