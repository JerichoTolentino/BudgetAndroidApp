package com.budget_app.jt_interfaces;

public interface ExpenseView
{

    void updateID(long id);
    void updateName(String name);
    void updatePrice(long price);
    void updateCategory(String category);
    void updateDescription(String description);

}
