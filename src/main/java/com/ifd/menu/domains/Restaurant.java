
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class Restaurant {
    private String id;
    private String name;
    private Chain chain;
    private Collection<Menu> menus;
}
