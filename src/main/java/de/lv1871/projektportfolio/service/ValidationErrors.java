package de.lv1871.projektportfolio.service;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by SchuererR on 26.08.2016.
 */
public class ValidationErrors {

    private List<String> fehlerList = Lists.newArrayList();

    public ValidationErrors add(@Nonnull String fehler){
        fehlerList.add(fehler);
        return this;
    }

    public List<String> getFehlerList() {
        return fehlerList;
    }
}
