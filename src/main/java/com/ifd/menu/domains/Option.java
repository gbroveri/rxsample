
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Option {
    private String id;
    private String name;
    private Long price;
    private Status status = Status.AVAILABLE;
}
