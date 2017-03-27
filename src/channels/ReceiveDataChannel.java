package channels;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import javax.swing.SwingUtilities;

import interfaces.Main;
import interfaces.OutputWindow;
import protocols.Peer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveDataChannel extends Thread
{
	public static final int MAXBUFFER = 1024;

	String name;
	MulticastSocket socket;
	Peer peer;

	public ReceiveDataChannel(String name, MulticastSocket s, Peer peer)
	{
		this.name = name;
		this.socket = s;
		this.peer   = peer;
	}

	public static String getCTS() // CurrentTimeStamp
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("[HH:mm:ss.SSS]");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public void run()
	{
		try
		{
			if(name == "MC")
				Main.windows.printlnReceiverMC(getCTS() + " - Started receiver thread :: "+ name);
			if(name == "MDB")
				Main.windows.printlnReceiverMB(getCTS() + " - Started receiver thread :: "+ name);
			if(name == "MDR")
				Main.windows.printlnReceiverMR(getCTS() + " - Started receiver thread :: "+ name);
		
		}
		catch (ArithmeticException ex)
		{
			if(name == "MC")
				Main.windows.printStackTraceReceiverMC(ex); 
			if(name == "MDB")
				Main.windows.printStackTraceReceiverMB(ex);
			if(name == "MDR")
				Main.windows.printStackTraceReceiverMR(ex);
		}

		try
		{
			byte[] buf;
			DatagramPacket dg;
			String dgString;
			String msg;// char

			do
			{
				try{Thread.sleep(10);}catch(InterruptedException e){}

				buf = new byte[ MAXBUFFER ];

				dg = new DatagramPacket( buf , buf.length );
				socket.receive(dg);
				dgString = new String( dg.getData() );

				if ( !dg.getAddress().toString().substring(1).equals(peer.getLocalhost()) )
				{
					msg = peer.inbox.newMessage(dg.getAddress().toString(), dg.getPort() , dgString );

					try
					{
						
						if(name == "MC")
							Main.windows.printlnReceiverMC(getCTS() + " - RECEIVED - " + msg);
						if(name == "MDB")
							Main.windows.printlnReceiverMB(getCTS() + " - RECEIVED - " + msg);
						if(name == "MDR")
							Main.windows.printlnReceiverMR(getCTS() + " - RECEIVED - " + msg);
											}
					catch (ArithmeticException ex)
					{
						if(name == "MC")
							Main.windows.printStackTraceReceiverMC(ex); 
						if(name == "MDB")
							Main.windows.printStackTraceReceiverMB(ex);
						if(name == "MDR")
							Main.windows.printStackTraceReceiverMR(ex);
					}
				} 

				try
				{
					sleep(10);
				}
				catch(InterruptedException e)
				{
					e.getMessage();
				}
			} while(true);

		}
		catch(IOException n) 
		{
			if(name == "MC")
				Main.windows.printlnReceiverMC(getCTS() + " - Connection terminated");
			if(name == "MDB")
				Main.windows.printlnReceiverMB(getCTS() + " - Connection terminated");
			if(name == "MDR")
				Main.windows.printlnReceiverMR(getCTS() + " - Connection terminated");
		}
	}
}
