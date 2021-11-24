package Proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.charset.StandardCharsets;

public class ProxyChannel extends ChannelInitializer<SocketChannel> {

    private final Proxy proxy;

    public ProxyChannel(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        Server chosenServer = proxy.getServer();

        Bootstrap bootstrap = new Bootstrap();
        Channel channel = bootstrap.group(proxy.getWorkerGroup())
                .channel(NioSocketChannel.class)
                .handler(new ServerChannel(socketChannel) )
                .connect(chosenServer.getAddress(), chosenServer.getPort()).sync().channel();

        socketChannel.pipeline()
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override /* ProxyChannelHandler as Callback */
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf input) {
                        System.out.println( //Show which server is chosen
                                chosenServer.getAddress() + " " + chosenServer.getPort());

                        channel.writeAndFlush(changeInputConnectionKeepAliveToClose(input));
                        proxy.gotoNextServer();
                    }
                });
    }

    /*  HTTP header 'connection' needs to be set to close, or server rotation won't work.
        It will keep doing request to the same one, over an over... */
    private ByteBuf changeInputConnectionKeepAliveToClose(ByteBuf input) {
        byte[] buffer = new byte[256];
        int counter = input.readableBytes();
        input.readBytes(buffer, 0, counter);

        String entireMsg = new String(buffer,0 , counter);
        if(entireMsg.toLowerCase().contains("connection:")) {
            entireMsg = entireMsg.replace("keep-alive", "close");
        }
        return Unpooled.copiedBuffer(entireMsg, StandardCharsets.UTF_8);
    }
}
