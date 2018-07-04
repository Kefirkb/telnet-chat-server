package com.kefirkb;

import com.kefirkb.registries.CommonRegistry;
import com.kefirkb.services.CommandsDispatcher;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class TelnetRequestHandler extends SimpleChannelInboundHandler<String> {

    private final CommandsDispatcher commandsDispatcher;

    public TelnetRequestHandler(CommandsDispatcher commandsDispatcher) {
        super();
        this.commandsDispatcher = commandsDispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("Welcome to " + System.getProperty(CommonRegistry.SERVER_NAME_PROPERTY) + "!\r\n");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        if ("exit".equals(request)) {
            ctx.close().addListener(ChannelFutureListener.CLOSE);
        } else {
            commandsDispatcher.dispatch(request, ctx.channel());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}