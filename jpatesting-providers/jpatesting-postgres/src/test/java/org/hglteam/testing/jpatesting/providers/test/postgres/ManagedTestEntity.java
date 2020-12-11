package org.hglteam.testing.jpatesting.providers.test.postgres;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "managed_test_entity")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ManagedTestEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;
}
