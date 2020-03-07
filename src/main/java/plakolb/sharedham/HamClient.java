package plakolb.sharedham;


import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import plakolb.sharedham.cmd.Command;
import plakolb.sharedham.msg.Message;
import plakolb.sharedham.msg.MessageHandler;
import plakolb.sharedham.udp.UdpCallback;
import plakolb.sharedham.udp.UdpDiscoverer;

/**
 * Created by pinselimo on 04/04/16.
 */
public class HamClient implements UdpCallback
{
	public interface ConnectionUpdates
	{
		void onConnected();
		void onConnectionError(Exception e);
	}

	public interface OnMessageListener
	{
		void onMessage(Message msg);
		void onError(String errorDescription);
	}

	private final OnMessageListener messageListener;
	private final ConnectionUpdates connectionUpdates;
	private final int PORT = 16363;
	private final Thread udpThread;

	private Socket sock;
	private Thread inputThread;
	private OutputStream outStream;


	public HamClient(ConnectionUpdates connectionUpdates,
	                 OnMessageListener messageListener)
	{
		this.connectionUpdates = connectionUpdates;
		this.messageListener   = messageListener;

		udpThread = new Thread(new UdpDiscoverer(this));
		udpThread.start();
	}

	public void close()
	{   //TODO input might not be connected
		inputThread.interrupt();
		try
		{
			sock.close();
		}
		catch (IOException ignored)
		{

		}
	}

	public boolean isConnected()
	{
		return outStream != null;
	}

	public void sendCmd(final Command cmd)
	{
		if (!isConnected()) return;

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					outStream.write(cmd.serialize());
				}
				catch (JSONException ignored)
				{

				}
				catch (IOException e)
				{
					connectionUpdates.onConnectionError(e);
				}
			}
		}).start();

	}

	@Override
	public void onDeviceDiscovered(InetAddress dev)
	{
		connect(new InetSocketAddress(dev,PORT));
		udpThread.interrupt();
	}

	@Override
	public void onSocketError(IOException e)
	{
		connectionUpdates.onConnectionError(e);
	}

	private void connect(final InetSocketAddress ham)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Socket sock = new Socket();
				try
				{
					sock.connect(ham);
					onSocket(sock);
				}
				catch (IOException e)
				{
					connectionUpdates.onConnectionError(e);
				}
			}
		}).start();

	}

	private void onSocket(final Socket sock)
	{
		this.sock = sock;
		try
		{
			this.outStream = sock.getOutputStream();

			inputThread = new Thread(new MessageHandler(sock.getInputStream(),this.messageListener));
			inputThread.start();

			connectionUpdates.onConnected();
		}
		catch (IOException e)
		{
			connectionUpdates.onConnectionError(e);
		}
	}
}
