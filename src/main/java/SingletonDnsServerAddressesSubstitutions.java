import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.InjectAccessors;
import com.oracle.svm.core.annotate.TargetClass;
import io.netty.resolver.dns.DnsServerAddresses;
import io.netty.util.internal.SocketUtils;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author rpuch
 */
@TargetClass(className = "io.netty.resolver.dns.SingletonDnsServerAddresses")
final class SingletonDnsServerAddressesSubstitutions {
    @Alias
    @InjectAccessors(SingletonDnsServerAddressesAddressAccessor.class)
    private InetSocketAddress address;

    private static class SingletonDnsServerAddressesAddressAccessor {
        private static final Map<DnsServerAddresses, InetSocketAddressParts> PARTS_MAP = Collections.synchronizedMap(
                new WeakHashMap<>());

        static InetSocketAddress get(DnsServerAddresses dnsServerAddresses) {
            InetSocketAddressParts parts = PARTS_MAP.get(dnsServerAddresses);
            return parts == null ? null : parts.toInetSocketAddress();
        }

        static void set(DnsServerAddresses dnsServerAddresses, InetSocketAddress address) {
            PARTS_MAP.put(dnsServerAddresses, new InetSocketAddressParts(address));
        }
    }

    private static class InetSocketAddressParts {
        private final String hostString;
        private final int port;

        private InetSocketAddressParts(InetSocketAddress address) {
            this.hostString = address.getHostString();
            this.port = address.getPort();
        }

        private InetSocketAddress toInetSocketAddress() {
            return SocketUtils.socketAddress(hostString, port);
        }
    }
}
