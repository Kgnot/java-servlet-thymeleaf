package org.server.config.beans.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeanType<T> {

    private Class<T> clazz;
    private String specificBean;

}
