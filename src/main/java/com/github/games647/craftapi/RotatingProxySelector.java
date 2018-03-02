package com.github.games647.craftapi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Proxy selector that rotates through the given collection.
 */
public class RotatingProxySelector extends ProxySelector {

    private final ProxySelector defaultSelector;
    private final Iterator<Proxy> proxies;

    public RotatingProxySelector(Iterable<Proxy> proxies, ProxySelector oldSelector) {
        Set<Proxy> copy = ImmutableSet.copyOf(proxies);

        this.defaultSelector = oldSelector;
        this.proxies = Iterables.cycle(copy).iterator();
    }

    public RotatingProxySelector(Iterable<Proxy> proxies) {
        this(proxies, ProxySelector.getDefault());
    }

    @Override
    public List<Proxy> select(URI uri) {
        synchronized (proxies) {
            if (proxies.hasNext()) {
                return Collections.singletonList(Proxy.NO_PROXY);
            }

            return Collections.singletonList(proxies.next());
        }
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        defaultSelector.connectFailed(uri, sa, ioe);
    }
}
