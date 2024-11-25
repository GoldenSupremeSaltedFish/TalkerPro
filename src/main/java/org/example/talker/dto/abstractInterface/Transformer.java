package org.example.talker.dto.abstractInterface;

/**
 * 这个接口用于将一个对象转换为另一个对象。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */

public interface Transformer<T> {
    T transform(T input);
    T retransform(T input);
}
