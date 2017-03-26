package interfaces;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

import channels.*;
import protocols.Peer;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
	{
		// "program mcAddress mcPort mdbAddress mdbPort mdrAddress mdrPort
		// 224.0.0.1 1110 224.0.0.2 1111 224.0.0.3 1112

     	if( args.length != 6 )  // minimo de 7 argumentos
		{
			System.out.println("Invalid argument number!\n<program> <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
			return ;
		}

		Menu menu = new Menu();
		Peer peer = new Peer( InetAddress.getLocalHost().getHostAddress() );

		String 	address = null,
				port 	= null;

		MulticastSocket[] ms = new MulticastSocket[3];
 		InetAddress[] group = new InetAddress[3];

		// Configuração do socket para MC --------------------
 		address = args[0];
		port    = args[1];

		group[0] = InetAddress.getByName( address );
		ms[0] = new MulticastSocket(Integer.parseInt( port ));
		ms[0].joinGroup(group[0]);

		// Configuração do socket para MDB -------------------
		address = args[2];
		port    = args[3];

		group[1] = InetAddress.getByName( address );
		ms[1] = new MulticastSocket(Integer.parseInt( port ));
		ms[1].joinGroup(group[1]);

		// Configuracao do socket para MDR -------------------
		address = args[4];
		port    = args[5];

		group[2] = InetAddress.getByName( address );
		ms[2] = new MulticastSocket(Integer.parseInt( port ));
		ms[2].joinGroup(group[2]);

		// Configuracao de Threads
		ReceiveControlChannel trMC   = new ReceiveControlChannel("MC" ,ms[0],peer);		// thread de recolha MC
		ReceiveBackupChannel trMDB  = new ReceiveBackupChannel("MDB",ms[1],peer);		// thread de recolha MDB
		ReceiveRestoreChannel trMDR  = new ReceiveRestoreChannel("MDR",ms[2],peer); 		// thread de recolha MDR
		SendDataChannel   trWORK = new SendDataChannel  (group,ms,peer);    // thread de trabalho e envio
   	
		// Inicio de threads
		trMC.start();
		trMDB.start();
		trMDR.start();
		trWORK.start();

		Scanner in = new Scanner(System.in);
		int option;
		boolean exitNow = false;

		peer.files.getAllFilesFromStorage();

		peer.files.list();

		// Ciclo de menu
		do {

			menu.mmain();
			option = in.nextInt();

			switch (option)
			{
				case 1 :
					menu.backup();
					peer.backup();
					break;

				case 2 :
					menu.restore();
					peer.restore();
					break;

				case 3 :
					menu.delete();
					peer.delete();
					break;

				case 0 :
					exitNow = true;
					break;

				default:
					System.out.println("Opção inválida.");
					break;
			}

		} while( !exitNow );


		// desliga o socket de MC
	 	ms[0].leaveGroup(group[0]);
		ms[0].close();

		// desliga o socket de MDB
		ms[1].leaveGroup(group[1]);
		ms[1].close();

		// desliga o socket de MDR
		ms[2].leaveGroup(group[2]);
		ms[2].close();
    }

}
