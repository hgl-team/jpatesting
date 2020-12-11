package org.hglteam.testing.jpatesting.providers.test.h2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MappedTestEntity {
    private Integer id;
    private String nombre;
}
