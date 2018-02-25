package com.github.games647.craftapi.resolver;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

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
    public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
        return oldFactory.createSocket(socket, s, i, b);
    }

    @Override
    public Socket createSocket(Socket s, InputStream consumed, boolean autoClose) throws IOException {
        return oldFactory.createSocket(s, consumed, autoClose);
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        return oldFactory.createSocket(s, i);
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return oldFactory.createSocket(s, i, inetAddress, i1);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return oldFactory.createSocket(inetAddress, i);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return oldFactory.createSocket(inetAddress, i, inetAddress1, i1);
    }

    public void setOutgoingAddresses(Collection<InetAddress> addresses) {
        ImmutableSet<InetAddress> copy = ImmutableSet.copyOf(addresses);
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
