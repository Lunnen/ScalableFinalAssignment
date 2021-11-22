package Proxy;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerChannelInit extends ChannelInitializer<SocketChannel> {

    private final Channel channel;

    public ServerChannelInit(Channel channel) {
        this.channel = channel;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) {


        ChannelPipeline p = socketChannel.pipeline()
                .addLast(new StringDecoder()) // inbound first
                .addLast(new StringEncoder()) // outbound first
                .addLast(new SimpleChannelInboundHandler<String>() {
                    /*
                    ServerChannelHandler as Callback
                    */
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

                        channel.writeAndFlush(msg);
                    }
                });
    }

}