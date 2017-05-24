package org.vaadin.teemusa.gridextensions.paging;

import com.vaadin.data.provider.ConfigurableFilterDataProviderWrapper;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializableBiFunction;

import java.util.Objects;

public class FilteredDataProvider<T, F> extends ConfigurableFilterDataProviderWrapper<T, F, F, F> {

    SerializableBiFunction<F, F, F> combiner;

    FilteredDataProvider(DataProvider dataProvider, SerializableBiFunction<F, F, F> combiner) {
        super(dataProvider);
        this.combiner = Objects.requireNonNull(combiner, "combiner cannot be null");
    }

    @Override
    protected F combineFilters(F queryFilter, F configuredFilter) {
        return combiner.apply(queryFilter, configuredFilter);
    }
}
