package com.example.accounts.databaseService;

public interface IDatabaseServiceAbstractFactory
{
    IEntryService createEntryService();

    ICategoryService createCategoryService();

    IExpenseLimitService createExpenseLimitService();
}
