package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.professional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professional {
    private long id;
    private String type;
    private String name;
    private String surname1;
    private String surname2;
    private String legalID;
    private String complete_name;
}
