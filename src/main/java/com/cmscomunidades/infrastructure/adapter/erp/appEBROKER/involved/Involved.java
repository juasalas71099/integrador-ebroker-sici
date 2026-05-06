package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.involved;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.address.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Involved {
    public int id;
    public String name;
    public String surname1;
    public String surname2;
    public String phone;
    public String email;
    public Address address;
    public String legal_id;
}
