import java.io.File;
interface EncryptionInterface
{
   /* Decrypts the current file and returns the resulting file*/
   File decrypt();
   
   /* Encrypts the current file and returns the resulting file */
   File encrypt();
}
