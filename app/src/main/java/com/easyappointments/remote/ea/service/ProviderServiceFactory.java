package com.easyappointments.remote.ea.service;

/**
 * Created by matteo on 24/04/17.
 */

public abstract class ProviderServiceFactory extends BaseServiceFactory{
    public static ProviderService getInstance(String username, String psw, String url) {
        return getClient(ProviderService.class, username, psw, url);
    }
}
