package io.mark.java_examples.Executors;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

public class JavaExecutor

  {
    private static Options options = new Options();

    public static void main(String[] args ) {
	String jarName="",className="",methodName="";
	options.addOption("c","class",true,"Fully qualified lass name in the form of package_name.class_name")
	       .addOption("m","method",true,"Method name to be executed")
	       .addOption("j","jar",true,"Full path of JAR file that includes the class and required code");
	  
	 CommandLineParser parser = new BasicParser();
	     CommandLine cmd = null;
	          try {
		          cmd = parser.parse(options, args);
		          if (cmd.hasOption("h"))
		                      help();
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
		      if (cmd.hasOption("j")) {
			      jarName=cmd.getOptionValue("j");
		      }else {
			      log("Missing --jar option");
			      help();
		      }

	          } catch (ParseException e) {
	               log("Failed to parse comand line properties");
	                    e.printStackTrace();
	                     help();
                }

       System.out.println("We will run the "+methodName+" method of class "+className+" from this JAR file "+jarName);

	ClassLoader classLoader = JavaExecutor.class.getClassLoader();

	try{
		Path jarPath = Paths.get(jarName);
   		URLClassLoader urlClassLoader = new URLClassLoader(
      			new URL[] {jarPath.toUri().toURL()},
       		         classLoader);

		Class javaDemoClass = urlClassLoader.loadClass(className);
      		Method method = javaDemoClass.getMethod(methodName);
      		method.invoke(null);
    	
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

