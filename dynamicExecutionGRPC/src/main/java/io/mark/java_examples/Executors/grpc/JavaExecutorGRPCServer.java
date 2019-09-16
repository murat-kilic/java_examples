package io.mark.java_examples.Executors.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.ServerStreamTracer;


public class JavaExecutorGRPCServer
{
	public void startServer() throws Exception {
		  Server server = ServerBuilder.forPort(8080)
			               .addService(new ExecutionServiceImpl())
			              .build();

		    server.start();
		     System.out.println("Server started");
		          server.awaitTermination();

	}

	public static void main( String[] args ) throws Exception
		      {
			              JavaExecutorGRPCServer jes=new JavaExecutorGRPCServer();
				              jes.startServer();
					        }






}


