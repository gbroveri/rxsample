package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class ItemGroup {
    private String id;
    private String name;
    private int pick;
    private Collection<RegularItem> items;
}
