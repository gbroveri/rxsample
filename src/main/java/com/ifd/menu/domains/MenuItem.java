package com.ifd.menu.domains;

import java.io.Serializable;

public interface MenuItem extends Serializable {
    void setPrice(Long price);
    Long getPrice();
}
