package com.bonitasoft.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Employee implements com.bonitasoft.engine.bdm.Entity {

    private static final long serialVersionUID = -506130279298072307L;

    @Id
    @GeneratedValue
    private Long persistenceId;

    @Version
    private Long persistenceVersion;

    private String firstName;

    private String lastName;

    protected Employee() {
        super();
    }

    public Employee(final long persistenceId, final String firstName, final String lastName) {
        this(firstName, lastName);
        this.persistenceId = persistenceId;
    }

    public Employee(final String firstName, final String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(final Employee employee) {
        persistenceId = employee.getPersistenceId();
        persistenceVersion = employee.getPersistenceVersion();
        firstName = employee.getFirstName();
        lastName = employee.getLastName();
    }

    @Override
    public Long getPersistenceId() {
        return persistenceId;
    }

    @Override
    public Long getPersistenceVersion() {
        return persistenceVersion;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (firstName == null ? 0 : firstName.hashCode());
        result = prime * result + (persistenceId == null ? 0 : persistenceId.hashCode());
        result = prime * result + (lastName == null ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        if (persistenceId == null) {
            if (other.persistenceId != null) {
                return false;
            }
        } else if (!persistenceId.equals(other.persistenceId)) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee [persistenceId=" + persistenceId + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
