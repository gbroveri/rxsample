
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class Menu {
    private String id;
    private String name;
    private Collection<MenuItem> items;
}
