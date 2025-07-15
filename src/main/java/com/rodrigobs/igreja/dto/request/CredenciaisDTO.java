package com.rodrigobs.igreja.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredenciaisDTO {
    private String email;
    private String senha;
}