package com.ifd.menu.domains.promotion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Promotion {
    private String id;
    private int discount = 0;
}
