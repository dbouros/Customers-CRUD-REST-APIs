package com.spring_boot_example;

// SPRING DOCUMENTATION LINKS:
// 1) https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/
// 2) https://spring.io/projects/spring-boot

// This is added along with the annotation(@) for the Spring Boot Application !
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

// The Objects lib. is used in the commented class that can replace the "record" class we made.

// To use the "List" in the "record" classes.
import java.util.List;

//======================================================================================================================
// Root level (Before main() function).

@SpringBootApplication
// "@SpringBootApplication" == "@Configuration" + "@EnableAutoConfiguration" + "@ComponentScan".
// If I were to use "@ComponentScan" I would also need to give a path for components:
// @ComponentScan(basePackages = "com.spring_boot_example") which is my project's path.

@RestController
/*
 @RestController annotation means that any method inside class "Main" with the annotations (GetMapping, PutMapping
 and PostMapping) will be exposed as REST endpoints that the client can call (created APIs).
*/

@RequestMapping("api/v1/customers")
public class Main {

//=================================================================================================================================================================
// First section (Using the "record" class and creating a 'Greet_User' API).

    @GetMapping("/Greet_User") // Path "/" == "http://localhost:8080/" , ex. Path "/Greet_User" == "http://localhost:8080/Greet_User".
    private greet_Response greet_User() {
        // This "response" object is what we print on the screen.
        greet_Response response = new greet_Response("Hello User !!", List.of("Java", "Python", "C"), new Person("Edy", 25, 30_000));
        return response;
    }
    /*
     Methods that the client will be able to call.
     "@GetMapping(value = "/Path")" == a smaller easier "@RequestMapping(method = ... , value = "/Path")"
     These methods are for the "read" operation in "CRUD" for "HTTP Get requests.

     In the above code, we are creating a method named "greet_User" of type "greet_Response()" (The "record" class below) and an
     instance(object) of it in the "return".
     The response that appears on the screen "{"greet":"Hello User !!"}" is JSON format, a JSON object. This is due to Maven's
     external library called "Jackson" which is the best known JSON parser for Java. (JSON == JavaScript Object Notation)
    */

    // SOS! -> After the "@RequestMapping" adding a new universal url the new path that our "greet_User"
    // method is running is "http://localhost:8080/api/v1/customers/Greet_User".

    record  Person (String name, int age, double savings){}

    record greet_Response (
            String greet,
            List<String> favProgrammingLangs,
            Person person){}
    /*
     "record" == keyword to declare a special type of class that is to hold immutable data(mostly of type JSON).
          1) Fields in "record" classes are of type "final" and cannot be modified after object creation etc.
          2) They auto-generate constructors for basic methods like "toString()", "equals()", "hashCode()" etc. which saves
          code and also makes it more readable and maintainable.
    */

//======================================================================================================================
// Second section (APIs).

    // 1) Read Customers API.
    // This is a GET request, for the client to get an existing resource from our database.
    // (More on "resources/Notes and Images/Notes")

    // Injection
    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    // The "main" function with "run" function that runs the SpringBootApplication.
    public static void main(String[] args){
        //System.out.println("Hello World !!");
        SpringApplication.run(Main.class, args);
    }

    /*
     Path is given to "@RequestMapping" in "root level".
     It would be better to use "@GetMapping("api/v1/customers")" instead of "@RequestMapping("api/v1/customers")"
     because then I would have both urls available instead of having one generalized. Now the other url
     "@GetMapping("/Greet_User")" is useless and I have no way of retrieving those responses.
    */
    @GetMapping
    public List<Customer> getCustomers(){
        // Calling ".findAll()" to find all customers. (Don't forget the "CustomerRepository" interface
        // might look empty but with inheritance it has all the methods of "JpaRepository" !!)
        return customerRepository.findAll();
    }

    // 2) Create Customer API.

     /*
     This is a POST request, for the client to add a new resource to our database. We basically want
     to expose an API endpoint so that clients can insert into our database too instead of only us.
     We can use a paid tool called "Postman" to do the HTTP POST Request, or we can do the web request
     with the command prompt using the 'curl' command. (More on "resources/Notes and Images/Notes")
    */
    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request){
        // The object below of type "Customer" is using our default constructor(The one-line one!).
        Customer customer = new Customer();

        /*
         1) "request.name/email/age" are the values the user posted to us which we get through the
         "request" variable.
         2) Then we are setting the values inside the "customer" object that we created equal to the "request"
         values and now our data is ready to be inserted into the database.
        */
        customer.setName(request.name);
        customer.setEmail(request.email);
        customer.setAge(request.age);

        /*
         Using the "customerRepository" interface that is connected to the database(jdbc), we use the "save"
         method which is contained inside the "JpaRepository", to save our new customer into the database
         by giving the save method the "customer" object.
        */
        customerRepository.save(customer);
    }

    // Creating the body of the request that we want to accept from our client.
    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    // 3) Delete Customer API.

    // This is a DELETE request, for the client to delete an existing resource from inside our database
    // that was either posted(POST) there by the client or inserted there by us.
    // (More on "resources/Notes and Images/Notes")

    @DeleteMapping("delete_resource/{customerID}")
    public void deleteCustomer(@PathVariable("customerID") Integer id){
        // Deletes a customer from the database using their "id".
        customerRepository.deleteById(id);
    }

    // 4) Update Customer API.

    /*
     1) This API uses the "@PutMapping" annotation so the client needs to do an HTTP PUT request to
     our server to update(edit) information about an existing customer, in our case the "email" variable.
     (More on "resources/Notes and Images/Notes")
    */

    @PutMapping("update_resource/{customerID}")
    public void updateCustomer(@PathVariable("customerID") Integer id, @RequestBody NewCustomerRequest request) {
        Customer customer = customerRepository.getReferenceById(id);
        if (customer.getId().equals(id)){
            customer.setEmail(request.email);
        }
        customerRepository.save(customer);
    }

}

/*
//SOS!! -> The class below is the exact equal of the single line "record" class that we created with the auto-generated methods
// that were also mentioned above, and it generates the exact same result when we run the program.

//SOS! -> To test this fact comment the "record" class and uncomment this class below.

class greet_Response {

    //Private and final variable "greet", exactly as the "record" class would have it.
    private final String greet;

    //Constructor auto-generated for "greet_Response" class.
    public greet_Response(String greet) {
        this.greet = greet;
    }

    // "Getter" method for the private "greet" variable.
    // Without the "Getter" we won't get the result at all.
    public String getGreet() {
        return greet;
    }

    // The "toString()" method, exactly as the "record" class would have it.
    @Override
    public String toString() {
        return "greet_Response{" +
                "greet='" + greet + '\'' +
                '}';
    }

    // The "equals()" method, exactly as the "record" class would have it.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        greet_Response that = (greet_Response) o;
        return Objects.equals(greet, that.greet);
    }

    // The "hashCode()" method, exactly as the "record" class would have it.
    @Override
    public int hashCode() {
        return Objects.hash(greet);
    }
}
*/
