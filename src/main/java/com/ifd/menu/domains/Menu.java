
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class Menu {
    private String id;
    private String name;
    private Collection<MenuItem> items;
}
