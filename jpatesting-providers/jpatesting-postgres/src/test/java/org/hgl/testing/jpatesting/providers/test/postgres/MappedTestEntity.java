package org.hgl.testing.jpatesting.providers.test.postgres;

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
