
package com.ifd.menu.gateways.mongo.documents;

import com.ifd.menu.domains.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegularItemMongo implements MenuItemMongo {
    private String id;
    private String name;
    private Long price;
    private OptionGroupMongo pickOptionals;
    private ItemGroupMongo pickItems;
    private Status status = Status.AVAILABLE;
}
