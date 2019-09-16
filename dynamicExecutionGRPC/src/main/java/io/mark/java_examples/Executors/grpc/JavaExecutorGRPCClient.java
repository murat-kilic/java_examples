package io.mark.java_examples.Executors.grpc;

import io.grpc.*;

public class JavaExecutorGRPCClient
{
	    public static void main( String[] args ) throws Exception
		        {
				  final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
					          .usePlaintext(true)
						          .build();

				   ExecutionServiceGrpc.ExecutionServiceBlockingStub stub = ExecutionServiceGrpc.newBlockingStub(channel);
				         ExecutionRequest request =
						         ExecutionRequest.newBuilder()
							           .setClassName(args[0])
								   .setMethodName(args[1])
								   .setJarLocation(args[2])
								   .setData(args[3])
								   .build();
					  ExecutionResponse response = 
						          stub.execute(request);

					        System.out.println(response);

						  channel.shutdownNow();
						      }
}
