package com.github.games647.craftapi.resolver;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Implementation for rotating outgoing IPv4 addresses.
 */
public class BalancedSSLFactory extends SSLSocketFactory {

    //in order to be thread-safe
    private final SSLSocketFactory oldFactory;
    private Iterator<InetAddress> iterator = Iterators.emptyIterator();

    public BalancedSSLFactory(SSLSocketFactory oldFactory) {
        this.oldFactory = oldFactory;
    }

    public BalancedSSLFactory() {
        this(HttpsURLConnection.getDefaultSSLSocketFactory());
    }

    @Override
    public Socket createSocket() throws IOException {
        Socket socket = oldFactory.createSocket();
        Optional<InetAddress> optAddress = getNextLocalAddress();
        if (optAddress.isPresent()) {
            //port 0 choose any local port that is available once the connection is established
            socket.bind(new InetSocketAddress(optAddress.get(), 0));
        }

        return socket;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return oldFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return oldFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return oldFactory.createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(Socket socket, InputStream consumed, boolean autoClose) throws IOException {
        return oldFactory.createSocket(socket, consumed, autoClose);
    }

    @Override
    public Socket createSocket(String socket, int port) throws IOException {
        return oldFactory.createSocket(socket, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return oldFactory.createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int port) throws IOException {
        return oldFactory.createSocket(inetAddress, port);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int port, InetAddress localHost, int localPort)
            throws IOException {
        return oldFactory.createSocket(inetAddress, port, localHost, localPort);
    }

    public void setOutgoingAddresses(Collection<InetAddress> addresses) {
        Set<InetAddress> copy = ImmutableSet.copyOf(addresses);
        Iterator<InetAddress> cycle = Iterators.cycle(copy);

        synchronized (this) {
            iterator = cycle;
        }
    }

    protected Optional<InetAddress> getNextLocalAddress() {
        synchronized (this) {
            if (!iterator.hasNext()) {
                return Optional.empty();
            }

            return Optional.of(iterator.next());
        }
    }
}
