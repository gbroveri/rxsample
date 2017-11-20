package com.ifd.menu.gateways.mongo.documents;

import com.ifd.menu.domains.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class ComboMongo implements MenuItemMongo {
    private String id;
    private String name;
    private Long price;
    private Collection<RegularItemMongo> items;
    private Status status = Status.AVAILABLE;
}
