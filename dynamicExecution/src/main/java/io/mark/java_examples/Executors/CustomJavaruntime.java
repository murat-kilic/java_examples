package io.mark.java_examples.Executors;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomJavaruntime

  {
    private static Options options = new Options();

    public static void main(String[] args ) {
	String jarName="",className="",methodName="",pathName="";
	String methodData=null;
	System.out.println(args);
	
	options.addOption("c","class",true,"Fully qualified lass name in the form of package_name.class_name")
	       .addOption("m","method",true,"Method name to be executed")
	       .addOption("p","path",true,"Path")
	       .addOption("d","data",true,"Data to be passed to method in JSON format");
	  
	 CommandLineParser parser = new BasicParser();
	     CommandLine cmd = null;
	          try {
		          cmd = parser.parse(options, args);
		          if (cmd.hasOption("h"))
		                      help();
			  if (cmd.hasOption("p")) {
			             pathName=cmd.getOptionValue("p");
                       } else {
			      log("Missing --path option");
						              help();
																			                             }

		     if (cmd.hasOption("m")) {
	                methodName=cmd.getOptionValue("m");
	                    } else {
                            log("Missing --method option");
                               help();
		            }
		      if (cmd.hasOption("c")) {
		             className=cmd.getOptionValue("c");
		       } else {
		          log("Missing --class  option");
		           help();
	              }
		 if (cmd.hasOption("d"))
		   methodData=cmd.getOptionValue("d");	 

	          } catch (ParseException e) {
	               log("Failed to parse comand line properties");
	                    e.printStackTrace();
	                     help();
                }
	
		if (methodData == null){
       			log("We will run the "+methodName+" method of class "+className+" from this JAR file "+jarName);
		}else {
			log("We will run the "+methodName+" method of class "+className+" from this JAR file "+jarName+" with method data: "+methodData);
    		}



	try{
		ClassLoader classLoader = JavaExecutor.class.getClassLoader();
		Path classPath = Paths.get(pathName);
   		URLClassLoader urlClassLoader = new URLClassLoader(
      			new URL[] {classPath.toUri().toURL()},
       		         classLoader);

		Class executableClass = urlClassLoader.loadClass(className);
		
		Method method;
		Object obj1=executableClass.newInstance();
		Object returnedObject=null;
		Gson gson = new GsonBuilder()
		         	   .setLenient()
		                   .create();

		Method[] methods=executableClass.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				Class[] pTypes = m.getParameterTypes();
		 		if (pTypes.length == 1) {
					Class pType=pTypes[0];
					method = executableClass.getMethod(methodName,new Class[]{pType});
					returnedObject=method.invoke(obj1,gson.fromJson(methodData,pType));
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
		     log("");
		 else
		     log(gson.toJson(returnedObject));

    	
    	}catch (MalformedURLException e) {
		log("JAR file path problem");
	        e.printStackTrace();
		help();
	}catch(ClassNotFoundException e){
		log("JAR file does not contain the class");
		e.printStackTrace();
		help();
	}catch(NoSuchMethodException e){
		log("Could not find the method in the class");
	        e.printStackTrace();
		help();
	}catch(IllegalAccessException e){
		log("Illegal access to the method");
		e.printStackTrace();
	        help();
	}catch(InvocationTargetException e){
	        log("Could not invoke the method");
	        e.printStackTrace();
		help();
	}catch (Exception e){
		log("Exception occured");
		e.printStackTrace();
	        help();
	}	

   }


 private static void help() {
     HelpFormatter formater = new HelpFormatter();
        formater.printHelp("MAIN", options);
          System.exit(0);
           }

private static void log(String message) {
	System.out.println(message);
  }
}

