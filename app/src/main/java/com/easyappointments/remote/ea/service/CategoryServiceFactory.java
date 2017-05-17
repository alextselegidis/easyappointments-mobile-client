package com.easyappointments.remote.ea.service;

import com.easyappointments.remote.ea.service.category.CategoryService;

/**
 * Created by matte on 17/05/2017.
 */

public class CategoryServiceFactory extends BaseServiceFactory {
    public static CategoryService getInstance() {
        return getClient(CategoryService.class);
    }
}
