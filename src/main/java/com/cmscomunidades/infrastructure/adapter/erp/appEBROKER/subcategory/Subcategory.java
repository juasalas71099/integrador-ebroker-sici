package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.subcategory;

import com.cmscomunidades.infrastructure.adapter.erp.appEBROKER.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory {
    private long id;
    private String name;
    private Category category;
}
