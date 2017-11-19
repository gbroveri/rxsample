package com.ifd.menu.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class OptionGroup {
    private String id;
    private String name;
    private int pick;
    private Collection<Option> options;
}
