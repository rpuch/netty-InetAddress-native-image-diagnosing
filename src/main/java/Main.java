import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.resolver.AddressResolver;
import io.netty.resolver.dns.DnsAddressResolverGroup;
import io.netty.resolver.dns.DnsServerAddressStreamProviders;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;

/**
 * @author rpuch
 */
public class Main {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty"));

        DnsAddressResolverGroup resolverGroup = new DnsAddressResolverGroup(NioDatagramChannel.class,
                DnsServerAddressStreamProviders.platformDefault());
        AddressResolver<InetSocketAddress> resolver = resolverGroup.getResolver(group.next());
        System.out.println(resolver);

        resolver.close();
        group.shutdownGracefully().get();
    }
}
