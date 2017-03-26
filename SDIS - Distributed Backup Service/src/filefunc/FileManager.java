package filefunc;
import java.util.ArrayList;
import java.util.Iterator;

import protocols.BackupFile;

import java.io.File;

public class FileManager
{
	private ArrayList<Ufile> fileList;

	public FileManager()
	{
		fileList = new ArrayList<Ufile>();
	}

	public ArrayList<Ufile> getFileList() { return fileList; }

	public void add(Ufile u)
	{
		fileList.add(u);
	}

	public boolean add(String pathfile)
	{
		File f = new File(pathfile);
		if ( !f.isFile() )
		{
			return false;
		}

		Ufile u = new Ufile(pathfile);
		add(u);

		return true;
	}

	public boolean remove(String fileName)
	{
		Ufile temp;

		for (Iterator<Ufile> it = fileList.iterator(); it.hasNext();)
		{
			temp = it.next();

			if (temp.getFileName().equals(fileName))
			{
				it.remove();
				return true;
			}
		}

		return false;
	}

	public Ufile find(String fileName)
	{
		Ufile temp;

		for (Iterator<Ufile> it = fileList.iterator(); it.hasNext();)
		{
			temp = it.next();

			if (temp.getFileName().equals(fileName))
			{
				return temp;
			}
		}

		return null;
	}

	public void getAllFilesFromStorage()
	{
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{ 
			if (listOfFiles[i].isFile())
			{
				add(listOfFiles[i].getName());
			}
		}
	}
	
	public String rightpad(String text, int length) {
	    return String.format("%-" + length + "." + length + "s", text);
	}

	public void list()
	{
		Ufile temp;
		int i=0;

		System.out.println("\n**************************************************");
		System.out.println("-> Files Stored : \n" );

		for (Iterator<Ufile> it = fileList.iterator(); it.hasNext(); i++)
		{
			temp = it.next();
			if(i == 0)
			System.out.println("Number  Name\t\t\t\tSize");


			System.out.printf("%d\t", i);
			
			if(temp.fileName().length() > 22)
				System.out.printf("%s...", temp.fileName().subSequence(0, 22));
			else System.out.printf("%25s", rightpad(temp.fileName(), 25));

			System.out.printf("\t%d", temp.fileSize());  

			if(temp instanceof BackupFile)
				System.out.printf("[backup]");
			System.out.println("");

		}
		System.out.printf("\n\nTotal: %d files\n", i);

		System.out.println("\n**************************************************");
	}

	public void addSTORED(String address, String fileId, int chunkNo)
	{
		Ufile temp;
		BackupFile b;

		for (Iterator<Ufile> it = fileList.iterator(); it.hasNext();)
		{
			temp = it.next();

			if ( temp instanceof BackupFile )
			{
				if ( ((BackupFile) temp).getFileId().equals(fileId) )
				{
					b = (BackupFile) temp;
					b.incrSTORED(chunkNo,address);

					return;
				}

			}
		}
	}

	public void removeSTORED(String address, String fileId, int chunkNo)
	{
		Ufile temp;
		BackupFile b;

		for (Iterator<Ufile> it = fileList.iterator(); it.hasNext();)
		{
			temp = it.next();

			if ( temp instanceof BackupFile )
			{
				if ( ((BackupFile) temp).getFileId().equals(fileId) )
				{
					b = (BackupFile) temp;
					b.decrSTORED(chunkNo,address);

					return;
				}
			}
		}
	}

	public BackupFile backup(int fileIndex, int desiredReplicationDeg)
	{
		Ufile u  = fileList.get(fileIndex);
		BackupFile b = new BackupFile(u.getFileName(), desiredReplicationDeg);

		fileList.set(fileIndex, b);

		b.split();

		return b;
	}

}
