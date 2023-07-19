package api.pojo_classes.go_rest;

import lombok.Builder;
import lombok.Data;
// this annotation from lombok will create setters and getters for our private variables
// to keep our code clean
/**
 * The @Data is a special annotation from Project Lombok. It helps us to keep our code clean and tidy
 * <p>
 * By marking a class @Data, you will get:
 * - Automatically generated getter and setter methods for all our variables
 * <p>
 * So, instead of writing all these methods ourselves, we can simply use @Data, and it does the work for us!
 */
@Data
/**
 * The @Builder annotation helping us to assign values to our fields in another class
 * without needing creating an object from the class
 */
@Builder
public class CreateUserWithLombok {


    private String name;
    private String gender;
    private String email;
    private String status;
}
