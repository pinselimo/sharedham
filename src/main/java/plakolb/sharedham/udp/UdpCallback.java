package plakolb.sharedham.udp;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Callback for UDP Discovery Thread
 * Created by pinselimo on 04/04/16.
 */
public interface UdpCallback {
    void onDeviceDiscovered(InetAddress dev);
    void onSocketError(IOException e);
}
