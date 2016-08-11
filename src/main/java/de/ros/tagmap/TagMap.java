package de.ros.tagmap;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TagMap<VALUE_TYPE> {

    private List<ValueWithTags<VALUE_TYPE>> taggedValues = Lists.newArrayList();

    public TagMap<VALUE_TYPE> put(
            @Nonnull VALUE_TYPE value,
            @Nonnull Object tag,
            Object ... tags){

        Objects.requireNonNull(value);
        Objects.requireNonNull(tag);

        ValueWithTags<VALUE_TYPE> valueWithTags = new ValueWithTags<>();

        valueWithTags.tags.add(tag);
        valueWithTags.value = value;

        for (Object o : tags) {
            valueWithTags.tags.add(o);
        }

        taggedValues.add(valueWithTags);

        return this;
    }

    public List<VALUE_TYPE> getValues(@Nonnull Object tag, Object ... tags){

        return taggedValues
                .stream()
                .filter(matchingTagsFilter(tag, tags))
                .map(ValueWithTags::getValue)
                .collect(Collectors.toList());
    }

    public TagMap<VALUE_TYPE> merge(@Nonnull TagMap<VALUE_TYPE> other){
        Preconditions.checkNotNull(other);

        this.taggedValues.addAll(other.taggedValues);

        return this;
    }

    private Predicate<ValueWithTags<VALUE_TYPE>> matchingTagsFilter(@Nonnull Object tag, Object[] tags) {
        return v -> {
            HashSet<Object> tagSet = Sets.newHashSet(tags);
            tagSet.add(tag);
            return v.tags.containsAll(tagSet);
        };
    }


    private final static class ValueWithTags<V>{

        private Set<Object> tags = new HashSet<>();
        private V value;

        public V getValue() {
            return value;
        }
    }
}
