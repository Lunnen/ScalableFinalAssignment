package Proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class ServerChannel extends ChannelInitializer<SocketChannel> {

    private final Channel channel;

    public ServerChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {

        socketChannel.pipeline()
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override // ServerChannelHandler as Callback
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf input) {

                        ByteBuf msg = Unpooled.copiedBuffer(input);
                        channel.writeAndFlush(msg);
                    }
                });
    }
}