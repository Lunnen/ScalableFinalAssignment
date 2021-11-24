package Proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Proxy {

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final List<Server> nodeServers = new ArrayList<>();
    private int serverIndex = 0;

    public Proxy(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    public void load() {
        try {
            Scanner input = new Scanner(new File("servers.txt"));
            while(input.hasNextLine()) {
                String line = input.nextLine();
                String[] row = line.split(":");

                Server serverFromFile = new Server();
                serverFromFile.setAddress(row[0]);
                serverFromFile.setPort(Integer.parseInt(row[1]));
                nodeServers.add(serverFromFile);
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new Channels(this))
                    .bind(this.port).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Round Robin load-balancing */
    public Server getServer() {
        if(serverIndex >= nodeServers.size()) {
            serverIndex = 0;
        }
        return nodeServers.get(serverIndex);
    }

    /* InitChannel runs twice, so increment has to run separately */
    public void gotoNextServer() {
        serverIndex++;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

}

