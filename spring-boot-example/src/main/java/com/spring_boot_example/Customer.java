package com.spring_boot_example;

import jakarta.persistence.*;

import java.util.Objects;

/*
GPT:
Usage in Entity Class:
When you annotate a field with @GeneratedValue in a JPA entity class, you need to
specify a generator strategy. @SequenceGenerator is one of those strategies.

Attributes of @SequenceGenerator:

        name: The name of the sequence generator. It should be unique within the persistence
        unit.
        sequenceName: The name of the database sequence. This attribute is optional, and if
        not specified, JPA uses a default naming strategy.
        initialValue: The value from which the sequence starts. The default is 1.
        allocationSize: The number of values that should be allocated at once. This helps
        improve performance by reducing the number of database round-trips. The default is 50.

1) @GeneratedValue specifies that the primary key value will be generated.
2) GenerationType.SEQUENCE indicates the usage of a sequence generator.
3) generator = "example_seq_generator" links this strategy to the specific @SequenceGenerator
named "example_seq_generator."
4) @SequenceGenerator defines the details of the sequence generator, such as its name
("example_seq_generator"), the name of the database sequence ("example_seq"), and the
allocation size.

Output after first run:

Hibernate:
    create sequence customer_id_sequence start with 1 increment by 50
Hibernate:
    create table customer (
        age integer,
        id integer not null,
        email varchar(255),
        name varchar(255),
        primary key (id)
    )
*/

// The class "Customer" == "Model".

// "@Entity" belongs with the "JPA" addition to our project. Though we also need a primary key to
// have it "mapped" into the database.
@Entity
public class Customer {

    @Id     // Primary Key
    /*
    @SequenceGenerator(name, sequenceName) & @GeneratedValue(strategy, generator) to create sequential unique
    value for the id(primary key) property whenever we insert a new "Customer" entity to the "customer"
    table in our database.
    */
    // "@SequenceGenerator" == Defines primary key generation strategy.
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence",
            allocationSize = 1 // default == 50, meaning the "id" will increment by 50.
    )
    // "@GeneratedValue" == specifies the strategy for generating primary key values.
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence" // The above named sequence in the @SequenceGenerator.
    )

    private Integer id;
    private String name;
    private String email;
    private Integer age;

    // SOS !!! -> ALWAYS HAVE THE DEFAULT CONSTRUCTOR HERE OR ELSE THE MAPPINGS WILL NOT WORK !!
    public Customer(){}

    // Constructor
    public Customer(Integer id, String name, String email, Integer age){
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getter & Setter for "id".
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Getter & Setter for "name".
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter & Setter for "email".
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter & Setter for "age".
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // equals() & hashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(age, customer.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    // toString()
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
