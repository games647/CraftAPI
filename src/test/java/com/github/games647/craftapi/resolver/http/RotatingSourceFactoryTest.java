package com.github.games647.craftapi.resolver.http;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import static org.junit.Assert.assertThat;

public class RotatingSourceFactoryTest {

    private RotatingSourceFactory sslFactory;

    @Before
    public void setUp() throws Exception {
        sslFactory = new RotatingSourceFactory();
    }

    @Test
    public void testDefault() throws Exception {
        try (Socket socket = sslFactory.createSocket()) {
            assertThat(socket.getLocalAddress(), notNullValue());
            assertThat(socket.getLocalAddress().isAnyLocalAddress(), is(true));
        }
    }

    @Test
    public void testRotating() throws Exception {
        List<InetAddress> localAddresses = new ArrayList<>();
        localAddresses.add(InetAddress.getByName("192.168.0.1"));
        localAddresses.add(InetAddress.getByName("192.168.0.2"));
        localAddresses.add(InetAddress.getByName("192.168.0.3"));

        sslFactory.setOutgoingAddresses(localAddresses);

        for (int i = 1; i <= 4; i++) {
            Optional<InetAddress> localAddress = sslFactory.getNextLocalAddress();
            assertThat(localAddress.isPresent(), is(true));
            assertThat(localAddress.get(), is(localAddresses.get((i - 1) % localAddresses.size())));
        }
    }

    @Test
    public void testCollectionModification() throws Exception {
        List<InetAddress> localAddresses = new ArrayList<>();
        localAddresses.add(InetAddress.getByName("192.168.0.1"));

        sslFactory.setOutgoingAddresses(localAddresses);
        localAddresses.clear();

        assertThat(sslFactory.getNextLocalAddress().isPresent(), is(true));
    }
}
