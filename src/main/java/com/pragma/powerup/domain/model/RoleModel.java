package com.pragma.powerup.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleModel {
    private Long id;
    private String name;
    private String description;
}
