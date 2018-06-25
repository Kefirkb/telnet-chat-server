package com.kefirkb.services;

import com.kefirkb.model.ChatChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Service for view and manage chat channels
 */
public interface ChatChannelService {
	/**
	 * Method returns chat channel by channel name.
	 *
	 * @return chat channel or null id chat channel not exists
	 *
	 * @implNote must be thread-safe
	 */
	@Nullable
	ChatChannel chatChannelByName(@Nonnull String chatChannelName);

	/**
	 * Method returns all channels
	 *
	 * @return all registered channels
	 */
	@Nonnull
	List<ChatChannel> getAllChannels();

	/**
	 * Method creates channel.
	 *
	 * @param chatChannel channel to create
	 *
	 * @return created channel
	 *
	 * @implNote must be thread-safe
	 */
	@Nonnull
	ChatChannel createAndAddChannel(@Nonnull ChatChannel chatChannel);
}
