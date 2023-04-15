package helloworld;

//import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.*;


@DynamoDBTable(tableName = "students-table")
public class Item {
    private String stdType;
    private String studentID;
    private String firstName;
    private String lastName;

    @DynamoDBHashKey(attributeName = "stdType")
    public String getStdType() {
        return stdType;
    }
    public void setStdType(String stdType) {
        this.stdType = stdType;
    }

    @DynamoDBRangeKey(attributeName = "studentID")
    public String getStudentID() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    @DynamoDBAttribute(attributeName = "firstName")
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute(attributeName = "lastName")
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}