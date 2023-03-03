package com.providerService.service;

import java.util.Set;

import com.providerService.model.Provider;

public interface ProviderService {

	public Set<Provider> getAllProviders();


	public Object add(Provider provider);

}
