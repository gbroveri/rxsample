
package com.ifd.menu.gateways.mongo.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "restaurants")
public class RestaurantMongo {
    private String id;
    private String name;
    private ChainMongo chain;
    private Collection<MenuMongo> menus = new ArrayList<>();
    private Collection<String> tags = new ArrayList<>();
}
