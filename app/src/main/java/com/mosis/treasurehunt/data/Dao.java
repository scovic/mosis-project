package com.mosis.treasurehunt.data;

import java.util.ArrayList;

public interface Dao<T> {

    ArrayList<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);

}
