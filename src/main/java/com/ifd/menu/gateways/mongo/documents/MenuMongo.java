
package com.ifd.menu.gateways.mongo.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class MenuMongo {
    private String id;
    private String name;
    private Collection<MenuItemMongo> items = new ArrayList<>();
}
