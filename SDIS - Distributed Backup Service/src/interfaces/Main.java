package interfaces;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

import channels.*;
import filefunc.*;
import protocols.Peer;

public class Main
{
	public static boolean exitNow = false;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		// "program mcAddress mcPort mdbAddress mdbPort mdrAddress mdrPort
		// 224.0.0.1 1110 224.0.0.2 1111 224.0.0.3 1112

		if( args.length != 6 )  // minimo de 7 argumentos
		{
			System.out.println("Invalid argument number!\n<program> <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
			return ;
		}

		FileManager menu = new FileManager();
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
		ReceiveDataChannel trMC   = new ReceiveDataChannel("MC" ,ms[0],peer);		// thread de recolha MC
		ReceiveDataChannel trMDB  = new ReceiveDataChannel("MDB",ms[1],peer);		// thread de recolha MDB
		ReceiveDataChannel trMDR  = new ReceiveDataChannel("MDR",ms[2],peer); 		// thread de recolha MDR
		SendDataChannel   trWORK = new SendDataChannel  (group,ms,peer);    // thread de trabalho e envio

		// Inicio de threads
		trMC.start();
		trMDB.start();
		trMDR.start();
		trWORK.start();

		Scanner in = new Scanner(System.in);
		int option;

		peer.files.getAllFilesFromStorage();


		// Ciclo de menu
		do {

			menu.menu();
			option = in.nextInt();

			switch (option)
			{
			case 1 :
				if(peer.backup() == -1)
					exitNow = true;
				break;

			case 2 :
				if(peer.restore() == -1)
					exitNow = true;
				break;

			case 3 :
				if(peer.delete() == -1)
					exitNow = true;
				break;

			case 0 :
				exitNow = true;
				break;

			default:
				System.out.println("Invalid input!");
				break;
			}

		} while( !exitNow );


		System.out.println("Turning off...");
		
		// desliga o socket de MC
		ms[0].leaveGroup(group[0]);
		ms[0].close();

		// desliga o socket de MDB
		ms[1].leaveGroup(group[1]);
		ms[1].close();

		// desliga o socket de MDR
		ms[2].leaveGroup(group[2]);
		ms[2].close();
		
		System.exit(0);
	}


}
