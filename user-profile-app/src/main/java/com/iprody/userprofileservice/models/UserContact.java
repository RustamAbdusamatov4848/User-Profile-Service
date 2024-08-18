package com.iprody.userprofileservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_contacts_seq")
    @SequenceGenerator(
            name = "user_contacts_seq",
            sequenceName = "user_contacts_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "telegram_id")
    private String telegramId;

    @Column(name = "mobile_phone")
    private String mobilePhone;
}
