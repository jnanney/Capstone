public class DESEncryption
{
   private long original;
   private DESKey key;
   private static final int ENCRYPTION_ITERATIONS = 16;
   private static final int FIRST_BIT = 1;
   private static final int MIDDLE_BIT = 32;
   private static final int LAST_BIT = 64;



   public DESEncryption(long original)
   {
      this.original = original;
      key = new DESKey();
   }

   public DESEncryption(long original, DESKey key)
   {
      this.original = original;
      this.key = key;
   }

   public long encrypt() throws InvalidSelectionException
   {
      return encryptCycle(false);
   }

   public long encryptionIteration(long left, long right, int iteration)
      throws InvalidSelectionException
   {
      long result = right << 32;
      long shortkey = key.keyScheduler(iteration);  
      long cypherResult = cypherFunction(right, shortkey);
      result = result | (left ^ cypherResult);
      return result;
   }

   private long cypherFunction(long block, long shortKey) 
      throws InvalidSelectionException
   {
      long result = 0;
      block = block << 32;
      long expandedBlock = Common.switchBits(block, DESArrays.getExpandedPositions());
      expandedBlock = expandedBlock >>> 16;
      long temp = shortKey ^ expandedBlock;
      temp = temp << 16;
      for(int i = 0; i < 8; i++)
      {
         byte sixBits =  (byte) Common.getBits(temp, i * 6 + 1, (i + 1) * 6);
         byte[][] sTable = DESArrays.getSelectionTables(i + 1);
         byte fourBits = sFunction(sixBits, sTable);
         result = result << 4;
         result = result | fourBits;
      }
      result = result << 32;
      result = Common.switchBits(result, DESArrays.getPermutationFunction());
      result = result >>> 32;
      return result;
   }

   public long decrypt() throws InvalidSelectionException
   {
      return encryptCycle(true);
   }
   
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

   public DESKey getKey()
   {
      return key;
   }
   
   /*
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
