package Proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProxyChannelInit extends ChannelInitializer<SocketChannel> {

    private final Proxy proxy;

    private final List<Integer> nodeServers = new ArrayList<>();

    public ProxyChannelInit(Proxy proxy) throws FileNotFoundException {
        this.proxy = proxy;
        readPorts();
    }

    int counter = 0;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        if(counter == nodeServers.size()){
           counter = 0;
        }

        int chosenServer = nodeServers.get(counter);

        Bootstrap bootstrap = new Bootstrap();
        Channel channel = bootstrap.group(proxy.getWorkerGroup())
                .channel(NioSocketChannel.class)
                .handler(new ServerChannelInit(socketChannel))
                .connect("localhost", chosenServer).sync().channel();

        socketChannel.pipeline()
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new SimpleChannelInboundHandler<String>() {
                    /*
                    ProxyChannelHandler as Callback
                    */
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

                        channel.writeAndFlush(msg);

                        counter++;
                        System.out.println("nr " + counter + " " + nodeServers.size());
                    }
                });


    }
    public void readPorts() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("ports.txt"));
        while(scanner.hasNextInt()) {
            nodeServers.add(scanner.nextInt());
        }
        scanner.close();
    }

}
