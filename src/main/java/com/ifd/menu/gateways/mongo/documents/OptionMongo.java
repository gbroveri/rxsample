
package com.ifd.menu.gateways.mongo.documents;

import com.ifd.menu.domains.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptionMongo {
    private String id;
    private String name;
    private Long price;
    private Status status = Status.AVAILABLE;
}
