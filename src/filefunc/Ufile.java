package filefunc;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Ufile{
	private String fileName;
	private int fileSize;
	private int nChunks;
	private File file = null;

	public Ufile(String fileName){
		this.fileName = fileName;
		file = new File(fileName);
		fileSize = (int) file.length();
		nChunks  = (int) Math.ceil(fileSize / Math.max(1.0, Utils.PARTSIZE *1.0));
	}

	public Ufile(Ufile u){
		this.fileName = u.fileName;
		fileSize = u.fileSize;
		nChunks  = u.nChunks;
	}

	public String getFileName(){
		return fileName;
	}

	public int getFileSize(){
		return fileSize;
	}

	public int getNChunks(){
		return nChunks;
	}

	public File getFile(){
		return file; 
	}

	@Override
	public String toString(){
		return "Ufile{" +
				"fileName='" + fileName + '\'' +
				", fileSize=" + fileSize +
				", nChunks=" + nChunks +
				", file=" + file +
				'}';
	}

	public String fileName(){
		return  fileName;
	}

	public int fileSize(){
		return  fileSize;
	}

	public void info(){
		System.out.println("file     : " + fileName);
		System.out.println("exists   ? " + file.exists());
		System.out.println("readable ? " + file.canRead());
	}

	public void split(){
		FileInputStream inStream;
		String newFileName, fileId = sha256();
		FileOutputStream fPartitioned;

		int i, chunkNo = 0, read = 0, length = Utils.PARTSIZE, currentFileSize = fileSize;

		byte[] chunk;

		try{
			inStream = new FileInputStream(file);

			System.out.println("\n File Split" );
			System.out.println(" file   : " + getFileName() );
			System.out.println(" fileId : " + fileId );
			System.out.println("******************************");

			for (i = 0; currentFileSize > 0; i++, chunkNo++){
				length = Math.min(currentFileSize, Utils.PARTSIZE);
				chunk = new byte[length];

				read = inStream.read(chunk, 0, length);
				currentFileSize -= read;

				assert (read == chunk.length);

				// sha256.partX - Sha-256
				newFileName = fileId + ".part" + chunkNo;

				System.out.printf("%2d ~ %s , %d bytes\n", i, newFileName, length);

				fPartitioned = new FileOutputStream(new File(newFileName));
				fPartitioned.write(chunk);
				fPartitioned.flush();
				fPartitioned.close();
			}

			System.out.println("******************************");
			System.out.printf(" File split into %d chunk%s.\n\n", i, ((i == 1) ? "" : "s"));

			inStream.close();

		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	public void merge(){
		File ofile = new File(fileName);

		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int nRead = 0;

		List<File> list = new ArrayList<File>();

		String fileId = sha256();

		for (int i = 0 ; i < nChunks ; i++){
			list.add(new File(fileId + File.separator + fileId + ".part" + i));
		}

		try{
			fos = new FileOutputStream(ofile,true);

			for (File file : list){
				fis = new FileInputStream(file);
				fileBytes = new byte[(int) file.length()];
				nRead = fis.read(fileBytes, 0,(int)  file.length());
				assert(nRead == fileBytes.length);
				assert(nRead == (int) file.length());
				fos.write(fileBytes);
				fos.flush();
				fileBytes = null;
				fis.close();
				fis = null;
			}

			fos.close();
			fos = null;
		}
		catch (Exception exception){
			exception.printStackTrace();
		}
	}

	public String sha256(){
		String hashname = null;
		try{
			hashname = Hash2String(String2Hash(fileName));
		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return hashname;
	}

	/*
	 * source from http://www.mkyong.com/java/java-sha-hashing-example/
	 */
	private byte[] String2Hash(String msg) throws NoSuchAlgorithmException{
		// algorithm can be "MD5", "SHA-1", "SHA-256"
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] inputBytes = msg.getBytes(); // get bytes array from message
		byte[] hashBytes = digest.digest(inputBytes);
		return hashBytes; // convert hash bytes to string (usually in hexadecimal form)
	}

	private String Hash2String(byte[] bytes) throws NoSuchAlgorithmException{
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}


	public void addFolder(String name) throws FileNotFoundException{
		File dir = new File(name);
		if (!dir.exists()){
			dir.mkdir();
		}
	}

	protected void addChunk(String name, String content) throws IOException{
		FileOutputStream fos;

		fos = new FileOutputStream(new File(name));
		fos.write(content.getBytes());
		fos.close();// fechei aqui
	}

	// source from http://www.java2s.com/Tutorial/Java/0180__File/Removeadirectoryandallofitscontents.htm
	public static boolean removeDirectory(File directory){
	
		if(directory == null)
			return false;
		if(!directory.exists())
			return true;
		if(!directory.isDirectory())
			return false;

		String[] list = directory.list();

		// Some JVMs return null for File.list() when the
		// directory is empty.
		if(list != null){
			for(int i = 0 ; i < list.length ; i++){
				File entry = new File(directory, list[i]);
				if(entry.isDirectory()){
					if (!removeDirectory(entry))
						return false;
				}
				else{
					if(!entry.delete())
						return false;
				}
			}
		}

		return directory.delete();
	}

	public void removeFile(String path){
		File aux = new File(path);
		if(aux.exists()){
			aux.delete();
		}
	}
}
