/************************************************************************************
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                     *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                     *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.processor.server;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.compiere.util.Env;
import org.spin.processor.controller.Processors;
import org.spin.processor.setup.SetupLoader;
import org.spin.service.grpc.authentication.AuthorizationServerInterceptor;
import org.spin.service.grpc.context.ServiceContextProvider;

import io.grpc.Server;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContextBuilder;
import io.grpc.ServerBuilder;

public class ProcessorServer {
	private static final Logger logger = Logger.getLogger(ProcessorServer.class.getName());

	private ServiceContextProvider contextProvider =  new ServiceContextProvider();

	private Server server;

	/** Services/Methods allow request without Bearer token validation */
	private List<String> ALLOW_REQUESTS_WITHOUT_TOKEN = Arrays.asList(
		// proto package . proto service / proto method
		"processor.Processors/GetSystemInfo"
	);

	/**	Revoke session	*/
	private List<String> REVOKE_TOKEN_SERVICES = Arrays.asList(
		// proto package . proto service / proto method
	);

	private AuthorizationServerInterceptor getInterceptor() {
		AuthorizationServerInterceptor interceptor = new AuthorizationServerInterceptor();
		interceptor.setAllowRequestsWithoutToken(this.ALLOW_REQUESTS_WITHOUT_TOKEN);
		interceptor.setRevokeTokenServices(this.REVOKE_TOKEN_SERVICES);
		return interceptor;
	}


	/**
	 * Get SSL / TLS context
	 * @return
	 */
	private SslContextBuilder getSslContextBuilder() {
		SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(new File(SetupLoader.getInstance().getServer().getCertificate_chain_file()),
                new File(SetupLoader.getInstance().getServer().getPrivate_key_file()));
        if (SetupLoader.getInstance().getServer().getTrust_certificate_collection_file() != null) {
            sslClientContextBuilder.trustManager(new File(SetupLoader.getInstance().getServer().getTrust_certificate_collection_file()));
            sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
        }
        return GrpcSslContexts.configure(sslClientContextBuilder);
	}

	private void start() throws IOException {
		//	Start based on provider
		Env.setContextProvider(contextProvider);

		logger.info("Service Template added on " + SetupLoader.getInstance().getServer().getPort());
		//	
		ServerBuilder<?> serverBuilder;
		if(SetupLoader.getInstance().getServer().isTlsEnabled()) {
			serverBuilder = NettyServerBuilder.forPort(SetupLoader.getInstance().getServer().getPort())
				.sslContext(getSslContextBuilder().build());
		} else {
			serverBuilder = ServerBuilder.forPort(SetupLoader.getInstance().getServer().getPort());
		}

		// Validate JWT on all requests
		AuthorizationServerInterceptor interceptor = getInterceptor();
		serverBuilder.intercept(interceptor);

		serverBuilder.addService(new Processors());
		server = serverBuilder.build().start();
		logger.info("Server started, listening on " + SetupLoader.getInstance().getServer().getPort());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				logger.info("*** shutting down gRPC server since JVM is shutting down");
				ProcessorServer.this.stop();
				logger.info("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws Exception {
		if (args == null) {
			throw new Exception("Arguments Not Found");
		}
		//
		if (args.length == 0) {
			throw new Exception("Arguments Must Be: [property file name]");
		}
		String setupFileName = args[0];
		if(setupFileName == null || setupFileName.trim().length() == 0) {
			throw new Exception("Setup File not found");
		}
		SetupLoader.loadSetup(setupFileName);
		//	Validate load
		SetupLoader.getInstance().validateLoad();
		final ProcessorServer server = new ProcessorServer();
		server.start();
		server.blockUntilShutdown();
	}
}
