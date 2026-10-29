package auca.ac.rw.stadiumManagement.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "organizer")
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String organizationName;
    private String contactPerson;

    @Column(unique = true)
    private String organizerCode;

    private String phone;
    private String email;

    public Organizer() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getOrganizerCode() { return organizerCode; }
    public void setOrganizerCode(String organizerCode) { this.organizerCode = organizerCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
