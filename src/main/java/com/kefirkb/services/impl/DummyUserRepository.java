package com.kefirkb.services.impl;

import com.kefirkb.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple stub logged users holder repository
 */
class DummyUserRepository {
	static final Map<String, User> LOGGED_USERS = new ConcurrentHashMap<>();
}
