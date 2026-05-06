package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Office {
    private long id;
    private String description;
    private Object phone;
    private boolean headOffice;
    private boolean physicalOffice;
}
