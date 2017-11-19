package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class Combo implements MenuItem {
    private String id;
    private String name;
    private Long price;
    private Collection<RegularItem> items;
    private Status status = Status.AVAILABLE;
}
