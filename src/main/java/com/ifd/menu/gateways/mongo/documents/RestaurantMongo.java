
package com.ifd.menu.gateways.mongo.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantMongo {
    private String id;
    private String name;
    private ChainMongo chain;
    private Collection<MenuMongo> menus = new ArrayList<>();
    private Collection<String> tags = new ArrayList<>();
}
