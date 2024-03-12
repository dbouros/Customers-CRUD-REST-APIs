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

/*
 We initialize that this is a Spring Boot application by doing 2 things !!
 1) First we initialize the annotation(@) for the Spring Boot Application !
 2) Secondly, inside "main" function we must add the "run" function the SpringApplication has !
*/

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
// First section (Learning "record" and "@GetMapping").

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
     external library called "Jackson" which is the best known JSON parser for Java.
     (JSON == JavaScript Object Notation)
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
     to expose an API endpoint that clients can insert into our database too instead of only us.
     SOS -> The tutorial is using a paid tool called "Postman" to do the HTTP POST Request, so I had to
     find a different way to test if the API works.
     1) I created a "data.json" inside the "resources" folder containing the data I wanted to POST to
     our server and save into the database.
     2) I (as a client now) used the following terminal command to do the POST request.
     -> Command: "curl -X POST -H "Content-Type: application/json" -d @data.json http://localhost:8080/api/v1/customers"
          1) curl: The command for the web request.
          2) -X POST: The type of request is going to be "POST".
          3) -H: The type of content that we want to send.
          4) -d: The actual data that we are sending.
          5) Then the link that we have are API endpoint ready to receive the request.
    */

    // Creating the body of the request that we want to accept from our client.
    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    /*
     1) We are using the "@PostMapping" annotation to accept an HTTP POST request from the client.
     2) The url we are using is still the existing one from the "@RequestMapping".
     SOS! -> Note that when you are running it, both the APIs will run at the same type, both the methods
     "getCustomers" from READ API and the "addCustomer" from CREATE API will run(it works fine) at the
     same time. So we can POST the request to our url, then refresh the page and the client will GET back
     the data of his POST.
     3) We are using the "@RequestBody" annotation so that the requested body of the data that we get from
     the user will be the type and form that we want.
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

    // 3) Delete Customer API.

    // This is a DELETE request, for the client to delete an existing resource from inside our database
    // that was either posted(POST) there by the client or inserted there by us.

    /*
     1) We are using the "@DeleteMapping" annotation to accept an HTTP DELETE request from the client.

     2) The url of the "@RequestMapping" annotation is still enabled which means both our GET and POST APIs
     are still operating.

     3) The "@DeleteMapping" annotation though has something added in the default path that given by the
     "@RequestMapping" annotation path: "http://localhost:8080/api/v1/customers".

     4) The new path for the HTTP DELETE request will now have the "delete_resource/{customerID}" added to
     it. The "{}" though will not be actually added in the path since they are there because we want to
     insert a value.

     5) Also the path will obviously change if we want to delete a different customer from the database
     since their "id" is also different.
     -> If id == 1
        path(1): http://localhost:8080/api/v1/customers/delete_resource/1
     -> If id == 2
        path(2): http://localhost:8080/api/v1/customers/delete_resource/2
     -> and so on for as many customers as we may have and want to delete.

     6) As a client now, we want to create two HTTP DELETE requests from our command line to delete those
     two resources(data objects) from the database using the two paths above.
     For path(1):
     -> Command: curl -X DELETE http://localhost:8080/api/v1/customers/delete_resource/1
     For path(2):
     -> Command: curl -X DELETE http://localhost:8080/api/v1/customers/delete_resource/2

     7) Considering the case of needing an "Authorization Token" as a client to be allowed to delete
     anything from the database, the above paths would go as follows:
     For path(1):
     -> Command: curl -X -H "Authorization: Bearer your_token" DELETE http://localhost:8080/api/v1/customers/delete_resource/1
     For path(2):
     -> Command: curl -X -H "Authorization: Bearer your_token" DELETE http://localhost:8080/api/v1/customers/delete_resource/2
     Replace "your_token" with your actual authentication token.

     8) The "@PathVariable" annotation is used to extract values from the URI template and bind them as
     method parameters, in our case as parameter for the "deleteCustomer" method.
    */

    @DeleteMapping("delete_resource/{customerID}")
    public void deleteCustomer(@PathVariable("customerID") Integer id){
        // Deletes a customer from the database using their "id".
        customerRepository.deleteById(id);
    }

    // 4) Update Customer API.

    /*
     1) This API uses the "@PutMapping" annotation and has the client do an HTTP PUT request to our server
     to update(edit) information about an existing customer, in our case the "email" variable.
     2) We use "customerRepository" object's method "getReferenceById" that gets the "id" of an existing
     "Customer" object and returns that "Customer" object itself.
     3) Now as a client once again, we use the following command to update the "email" variable of the
     customer with "id == 2".
     -> Command: curl -X PUT -H "Content-Type: application/json" -d @update_data2.json http://localhost:8080/api/v1/customers/update_resource/2
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

//SOS! -> "class greet_Response" == "record greet_Response", comment "record" and uncomment "class" and check it.
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

    // SOS!! -> The above class is the exact equal of the single line "record" class that we created with the auto-generated methods
    // that were also mentioned above, and it generates the exact same result when we run the program. As one can see, that one line
    // record saves us many lines of code !!
*/
