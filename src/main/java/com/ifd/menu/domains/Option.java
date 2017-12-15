
package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Option implements Serializable {
    private String id;
    private String name;
    private Long price;
    private Status status = Status.AVAILABLE;
}
