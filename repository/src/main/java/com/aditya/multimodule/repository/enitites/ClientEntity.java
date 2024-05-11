package com.aditya.multimodule.repository.enitites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "client")
public class ClientEntity {
    @Id
    @Column(name = "nit", nullable = false)
    private Long nit;
    @Column(name = "name", nullable=false)
    private String name;

    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "phoneNumber")
    private Long phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "state", nullable = false)
    private Boolean state;
}
