
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class Restaurant implements Serializable{
    private String id;
    private String name;
    private Chain chain;
    private Collection<Menu> menus;
}
