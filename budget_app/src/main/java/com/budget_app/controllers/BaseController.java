package com.budget_app.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tolenti4-INS on 2018-02-16.
 */

public abstract class BaseController<M, V>
{

    //region Members

    private M m_model;
    private List<V> m_views = new ArrayList<>();

    //endregion

    //region Constructor

    public BaseController(M model, V... views)
    {
    setModel(model);
    for (V view : views)
        addView(view);
    }

    //endregion

    //region Abstract Methods

    protected abstract void updateViews();

    //endregion

    //region Concrete Methods

    public void addView(V view)
    {
        if (view != null)
        {
            m_views.add(view);
            updateViews();
        }
        else
            throw new NullPointerException();
    }

    public boolean removeView(V view)
    {
        boolean removed = false;

        if (view != null)
        {
            removed = m_views.remove(view);
            updateViews();
        }
        else
            throw new NullPointerException();

        return removed;
    }

    //endregion

    //region Getters & Setters

    public M getModel()
    {
        return m_model;
    }
    public void setModel(M model)
    {
        if (model != null)
        {
            m_model = model;
            updateViews();
        }
        else
            throw new NullPointerException();

    }

    public Iterable<V> getViews()
    {
        return m_views;
    }

    //endregion

}
