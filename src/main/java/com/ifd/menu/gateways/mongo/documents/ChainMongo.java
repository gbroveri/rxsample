package com.ifd.menu.gateways.mongo.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class ChainMongo {
    private String id;
    private String name;
    private Collection<MenuMongo> menus;
}
