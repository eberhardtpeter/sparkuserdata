package hu.sparklabs.model;

import scala.Tuple3;

import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {

    private String firstName;
    private String lastName;
    private String location;

    public UserData(String firstName, String lastName, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }

    public UserData(Tuple3<String, String, String> tuple) {
        this.firstName = tuple._1();
        this.lastName = tuple._2();
        this.location = tuple._3();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData that = (UserData) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, location);
    }
}
