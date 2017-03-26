package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import javax.swing.SwingUtilities;

import interfaces.OutputWindow;
import protocols.Peer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveControlChannel extends Thread
{
	public static final int MAXBUFFER = 1024;

	String name;
	MulticastSocket socket;
	Peer peer;

	public ReceiveControlChannel(String name, MulticastSocket s, Peer peer)
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
		OutputWindow console = new OutputWindow("Thread Receiver :: " + name );
		SwingUtilities.invokeLater(console);

		try
		{
			console.println(getCTS() + " - Started receiver thread :: "+ name);
		}
		catch (ArithmeticException ex)
		{
			console.printStackTrace(ex);
		}

		try
		{
			byte[] buf;
			DatagramPacket dg;
			String dgString;
			String msg;// char

			//socket.setSoTimeout(400);

			do
			{
				try{Thread.sleep(10);}catch(InterruptedException e){}

				buf = new byte[ MAXBUFFER ];

				dg = new DatagramPacket( buf , buf.length );
				socket.receive(dg);
				dgString = new String( dg.getData() );

//				console.println(peer.getLocalhost());
//				console.println(dg.getAddress().toString());

				if ( !dg.getAddress().toString().substring(1).equals(peer.getLocalhost()) )
				{
					msg = peer.inbox.newMessage(dg.getAddress().toString(), dg.getPort() , dgString );

					try
					{
						console.println(getCTS() + " - RECEIVED - " + msg);
					}
					catch (ArithmeticException ex)
					{
						console.printStackTrace(ex);
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
			console.println(getCTS() + " - Connection terminated");
			//n.printStackTrace();
		}
	}
}
