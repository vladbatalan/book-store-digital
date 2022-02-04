package com.javainuse.model.erd;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "blacklist")
@Entity
public class BlackList {
    @Id
    @Column(name="token", nullable = false, unique = true)
    private String token;
}
