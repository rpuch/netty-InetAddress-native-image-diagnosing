import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.InjectAccessors;
import com.oracle.svm.core.annotate.TargetClass;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.NetUtil;

import java.net.InetAddress;

/**
 * @author rpuch
 */
@TargetClass(InternetProtocolFamily.class)
final class InternetProtocolFamilySubstitutions {
    @Alias
    @InjectAccessors(InternetProtocolFamilyLocalhostAccessor.class)
    private InetAddress localHost;

    private static class InternetProtocolFamilyLocalhostAccessor {
        static InetAddress get(InternetProtocolFamily family) {
            switch (family) {
                case IPv4:
                    return NetUtil.LOCALHOST4;
                case IPv6:
                    return NetUtil.LOCALHOST6;
                default:
                    throw new IllegalStateException("Unsupported internet protocol family: " + family);
            }
        }

        static void set(InternetProtocolFamily family, InetAddress address) {
            // storing nothing as the getter derives all it needs from its argument
        }
    }
}
