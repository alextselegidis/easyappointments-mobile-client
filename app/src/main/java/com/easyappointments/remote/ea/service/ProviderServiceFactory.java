package com.easyappointments.remote.ea.service;

import com.easyappointments.remote.ea.service.BaseServiceFactory;
import com.easyappointments.remote.ea.service.provider.ProviderService;

/**
 * Created by matteo on 24/04/17.
 */

public abstract class ProviderServiceFactory extends BaseServiceFactory {
    public static ProviderService getInstance(String username, String psw, String url) {
        return getClient(ProviderService.class, username, psw, url);
    }
}
