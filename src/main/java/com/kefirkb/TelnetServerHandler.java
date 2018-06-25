package com.kefirkb;

import com.kefirkb.services.AuthService;
import com.kefirkb.services.CommandsDispatcher;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

	private final AuthService authService;
	private final CommandsDispatcher commandsDispatcher;
	// TODO should move to env
	private static final String SERVER_NAME = "DUMMY_SERVER";

	public TelnetServerHandler(AuthService authService, CommandsDispatcher commandsDispatcher) {
		super();
		this.authService = authService;
		this.commandsDispatcher = commandsDispatcher;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		ctx.write("Welcome to " + SERVER_NAME + "!\r\n");
		ctx.flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		String[] commandNameWithParams = request.split("[ ]+");
		// Generate and write a response.
		String response = "";

		boolean close = false;
		if (request.isEmpty()) {
			response = "Please type something.";
		} else if ("bye".equals(request.toLowerCase())) {
			response = "Have a good day!";
			close = true;
		}

		String commandName = commandNameWithParams[0];
		// TODO Should refactor to requests processors
		// TODO response should send to queue for personal messages
		response = commandsDispatcher.dispatch(request, ctx.channel());

		// We do not need to write a ChannelBuffer here.
		// We know the encoder inserted at TelnetPipelineFactory will do the conversion.
		response += "\r\n";
		ChannelFuture future = ctx.channel().write(response);

		// Close the connection after sending 'Have a good day!'
		// if the client has sent 'bye'.
		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
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