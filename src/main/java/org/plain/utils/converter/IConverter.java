package org.plain.utils.converter;

import java.util.Collection;

/**
 * 转换器接口
 * @param <S> 源对象
 * @param <T> 目标对象
 */
public interface IConverter<S,T> {
    /**
     * 转换
     * @param source 源对象
     * @return 目标对象
     */
    T convert(S source);

    /**
     * 反转
     * @param target 目标
     * @return 源
     */
    S reverse(T target);

    /**
     * 集合转换
     * @param source source
     * @return Collection<T>
     */
    Collection<T> convert(Collection<S> source);


    /**
     * 集合反向转换
     * @param source source
     * @return Collection<T>
     */
    Collection<S> reverse(Collection<T> source);
}
