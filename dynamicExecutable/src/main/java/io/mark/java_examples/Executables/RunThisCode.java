package io.mark.java_examples.Executables;

public class RunThisCode{

public static void run() {
       System.out.println("Look Ma I'm running this code"); 
    }


public String handleRequest(){
     System.out.println("Handling a request with no parameters passed");
     return "Completed";
}

public String  handleRequestStr(String data) {
     System.out.println("Handling a request with String data: "+data);
     return "Received this:"+data;
     }

public void handleRequestInt(Integer data) {
	 System.out.println("Handling a request with Int data: "+data);
    }

public AddPersonResponse addPerson(AddPersonRequest req) {
	         System.out.println("Adding person first name: "+req.firstName+" last name:"+req.lastName+" age:"+req.age+" isActive:"+req.isActive);
		 AddPersonResponse res=new AddPersonResponse();
		 res.fullName=req.firstName+" "+req.lastName;
		 res.age=req.age;
		 res.shouldExerciseMore=!req.isActive;
		 return res;
   }

}
