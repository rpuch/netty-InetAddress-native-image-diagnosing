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
        private static volatile Inet4Address ADDR;

        static Inet4Address get() {
            Inet4Address result = ADDR;
            if (result == null) {
                // Lazy initialization on first access.
                result = initializeOnce();
            }
            return result;
        }

        private static synchronized Inet4Address initializeOnce() {
            Inet4Address result = ADDR;
            if (result != null) {
                // Double-checked locking is OK because INSTANCE is volatile.
                return result;
            }

            byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
            // Create IPv4 loopback address.
            try {
                result = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
            } catch (Exception e) {
                // We should not get here as long as the length of the address is correct.
                PlatformDependent.throwException(e);
            }

            ADDR = result;
            return result;
        }
    }

    private static class NetUtilLocalhost6Accessor {
        private static volatile Inet6Address ADDR;

        static Inet6Address get() {
            Inet6Address result = ADDR;
            if (result == null) {
                // Lazy initialization on first access.
                result = initializeOnce();
            }
            return result;
        }

        private static synchronized Inet6Address initializeOnce() {
            Inet6Address result = ADDR;
            if (result != null) {
                // Double-checked locking is OK because INSTANCE is volatile.
                return result;
            }

            byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
            // Create IPv6 loopback address.
            try {
                result = (Inet6Address) InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
            } catch (Exception e) {
                // We should not get here as long as the length of the address is correct.
                PlatformDependent.throwException(e);
            }

            ADDR = result;
            return result;
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