package plakolb.sharedham.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * This will discover any HamP3s running on the local network
 * Created by pinselimo on 04/04/16.
 */
public class UdpDiscoverer implements Runnable
{
    private final static DatagramPacket MSG_PACK;
    private final static String RESPONSE = "<discoverResponse>HamP3</discoverResponse>";
    private final static String MESSAGE = "<discover>HamP3</discover>";
    private final static int PORT = 13636;

    static {
        byte[] msgBytes = {};

        try
        {
            msgBytes = "<discover>HamP3</discover>".getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ignored)
        {
            msgBytes = MESSAGE.getBytes();
        }
        MSG_PACK = new DatagramPacket(msgBytes,msgBytes.length);
    }

    private final UdpCallback callback;
    private DatagramPacket rcvPack =
            new DatagramPacket(new byte[RESPONSE.length()], RESPONSE.length());

    public UdpDiscoverer(UdpCallback ucb)
    {
        this.callback = ucb;
    }

    @Override
    public void run()
    {
        DatagramSocket udpSock;

        try
        {
            udpSock = new DatagramSocket(PORT);
            udpSock.setBroadcast(true);
            while(true)
            {
                if (getBroadcast() != null)
                {
                    MSG_PACK.setAddress(getBroadcast());
                    MSG_PACK.setPort(PORT);
                    udpSock.send(MSG_PACK);

                    udpSock.receive(rcvPack);

                    if (check(rcvPack))
                    {
                        callback.onDeviceDiscovered(rcvPack.getAddress());
                        break;
                    }
                }
                Thread.sleep(1000,0);
            }
        }
        catch (IOException e)
        {
            callback.onSocketError(e);
        }
        catch (InterruptedException ignored)
        { }
    }

    private boolean check(DatagramPacket packet)
    {
        try
        {
            String data = new String(packet.getData(), "UTF-8");
            return data.equals(RESPONSE);
        }
        catch (UnsupportedEncodingException ignored)
        { }

        return false;
    }

    private InetAddress getBroadcast()
    {

        try
        {
            Enumeration<NetworkInterface> sockets = NetworkInterface.getNetworkInterfaces();
            while (sockets.hasMoreElements())
            {
                NetworkInterface face = sockets.nextElement();
                if (face.isLoopback())
                {
                    continue;
                }

                for (InterfaceAddress addr : face.getInterfaceAddresses())
                {
                    if (addr.getBroadcast() != null)
                    {
                        return addr.getBroadcast();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            callback.onSocketError(e);
        }
        return null;
    }
}
