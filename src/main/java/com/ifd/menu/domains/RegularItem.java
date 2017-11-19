
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegularItem implements MenuItem {
    private String id;
    private String name;
    private Long price;
    private OptionGroup pickOptionals;
    private ItemGroup pickItems;
    private Status status = Status.AVAILABLE;
}
