import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.InjectAccessors;
import com.oracle.svm.core.annotate.TargetClass;
import io.netty.util.NetUtil;
import io.netty.util.internal.PlatformDependent;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * @author rpuch
 */
@TargetClass(NetUtil.class)
final class NetUtilSubstitutions {
    @Alias
    @InjectAccessors(NetUtilLocalhost4Accessor.class)
    public static Inet4Address LOCALHOST4;

    @Alias
    @InjectAccessors(NetUtilLocalhost6Accessor.class)
    public static Inet6Address LOCALHOST6;

    @Alias
    @InjectAccessors(NetUtilLocalhostAccessor.class)
    public static InetAddress LOCALHOST;

    private static class NetUtilLocalhost4Accessor {
        static Inet4Address get() {
            // using https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
            return NetUtilLocalhost4LazyHolder.LOCALHOST4;
        }
    }

    private static class NetUtilLocalhost4LazyHolder {
        private static final Inet4Address LOCALHOST4;

        static {
            byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
            // Create IPv4 loopback address.
            try {
                LOCALHOST4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
            } catch (Exception e) {
                // We should not get here as long as the length of the address is correct.
                PlatformDependent.throwException(e);
                throw new IllegalStateException("Should not reach here");
            }
        }
    }

    private static class NetUtilLocalhost6Accessor {
        static Inet6Address get() {
            // using https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
            return NetUtilLocalhost6LazyHolder.LOCALHOST6;
        }
    }
    
    private static class NetUtilLocalhost6LazyHolder {
        private static final Inet6Address LOCALHOST6;

        static {
            byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
            // Create IPv6 loopback address.
            try {
                LOCALHOST6 = (Inet6Address) InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
            } catch (Exception e) {
                // We should not get here as long as the length of the address is correct.
                PlatformDependent.throwException(e);
                throw new IllegalStateException("Should not reach here");
            }
        }
    }

    // NOTE: this is the simpliest implementation I could invent to just demonstrate the idea; it is probably not
    // too efficient. An efficient implementation would only have getter and it would compute the InetAddress
    // there; but the post is already very long, and NetUtil.LOCALHOST computation logic in Netty is rather cumbersome.
    private static class NetUtilLocalhostAccessor {
        private static volatile InetAddress ADDR;

        static InetAddress get() {
            return ADDR;
        }

        static void set(InetAddress addr) {
            ADDR = addr;
        }
    }
}