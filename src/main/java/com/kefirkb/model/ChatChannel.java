package com.kefirkb.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basic chat channel
 */
public class ChatChannel {
	private static final int MAX_USERS_COUNT = 10;
	private static final int MAX_LAST_MESSAGES = 10;

	private final String chatChannelName;
	private User owner;

	private List<User> joinedUsers = new CopyOnWriteArrayList<>();

	// Updated every time when new broadcast message to channel
	private Queue<BroadCastMessage> lastMessages = new ArrayDeque<>(MAX_LAST_MESSAGES);

	public ChatChannel(@Nonnull String chatChannelName, @Nonnull User owner) {
		this.chatChannelName = Objects.requireNonNull(chatChannelName, "chatChannelName");
		this.owner = Objects.requireNonNull(owner, "owner");
	}

	public void joinUser(@Nonnull User user) {
		joinedUsers.add(user);
	}

	public void leaveUser(@Nonnull User user) {
		joinedUsers.remove(user);
	}

	@Nonnull
	public User getOwner() {
		return owner;
	}

	@Nonnull
	public List<User> getUsers() {
		return Collections.unmodifiableList(joinedUsers);
	}

	@Nullable
	public synchronized BroadCastMessage pushLastMessage(@Nonnull BroadCastMessage message) {
		if(lastMessages.size() == MAX_LAST_MESSAGES - 1) {
			lastMessages.poll();
		}
		if(lastMessages.add(message)) {
			return message;
		}
		return null;
	}

	@Nonnull
	public synchronized List<BroadCastMessage> getLastMessages() {
		return new ArrayList<>(lastMessages);
	}

	@Nonnull
	public String getChatChannelName() {
		return chatChannelName;
	}
}
