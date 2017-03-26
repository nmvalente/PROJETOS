package interfaces;

public class Menu {

	public void mmain(){
		
		System.out.println("******************************");
		System.out.println(" Peer Terminal");
		System.out.println("******************************");
		System.out.println("");
		System.out.println("1 - Backup File");
		System.out.println("2 - Restore File");
		System.out.println("3 - Delete File\n");
		System.out.println("0 - Quit\n");
		System.out.println("> ");
		// Ler da consola
	}

	public void backup(){

		System.out.println("******************************");
		System.out.println(" File Backup");
		System.out.println("******************************/n");
	}

	public void restore(){
		
		System.out.println("******************************");
		System.out.println(" File Restore");
		System.out.println("");
		System.out.println("******************************");
	}

	public void delete(){
		
		System.out.println("******************************");
		System.out.println(" File Delete");
		System.out.println("");
		System.out.println("******************************");
	}

}
