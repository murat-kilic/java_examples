package io.mark.java_examples.Executors.grpc;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExecutionServiceImpl extends  ExecutionServiceGrpc.ExecutionServiceImplBase {
	 public void execute(io.mark.java_examples.Executors.grpc.ExecutionRequest request,
			         io.grpc.stub.StreamObserver<io.mark.java_examples.Executors.grpc.ExecutionResponse> responseObserver) {
	  System.out.println(request);
	   ExecutionResponse.Builder resBuilder = ExecutionResponse.newBuilder();

	   try{
             ClassLoader classLoader =ExecutionServiceImpl.class.getClassLoader();
	     Path jarPath = Paths.get(request.getJarLocation());
	     URLClassLoader urlClassLoader = new URLClassLoader(
			     new URL[] {jarPath.toUri().toURL()},
			     classLoader);
	     Class executableClass = urlClassLoader.loadClass(request.getClassName());
	     Method method;
	     Object obj1=executableClass.newInstance();
	     Object returnedObject=null;
	     Gson gson = new GsonBuilder()
	                      .setLenient()
	                      .create();
	     Method[] methods=executableClass.getDeclaredMethods();
	     String methodName=request.getMethodName();
	     for (Method m : methods) {
		     if (m.getName().equals(methodName)) {
			      Class[] pTypes = m.getParameterTypes();
			      if (pTypes.length == 1) {
				      Class pType=pTypes[0];
				      method = executableClass.getMethod(methodName,new Class[]{pType});
				      returnedObject=method.invoke(obj1,gson.fromJson(request.getData(),pType));
				      break;
				}else if(pTypes.length == 0) {
					method = executableClass.getMethod(methodName);
					returnedObject=method.invoke(obj1);
					break;
				 }else
					 continue;
		     }
	     } 
	     if (returnedObject == null)
			  resBuilder.setErrCode("0").build();

		else
				 resBuilder.setErrCode(gson.toJson(returnedObject)).build();

	 }catch (MalformedURLException e) {
                resBuilder.setErrCode("JAR file path problem");
	 }catch(Exception e) {
		 resBuilder.setErrCode("Exception occured");
	 }

	 responseObserver.onNext(resBuilder.build());
	 responseObserver.onCompleted();


 }

}
