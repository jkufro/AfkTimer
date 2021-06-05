package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.time.Duration;

@Slf4j
@PluginDescriptor(
	name = "AFK Timer"
)
public class AfkTimerPlugin extends Plugin
{
	/**
	 * Keep track of if the notification has already been triggered
	 * since last time player was AFK.
	 */
	private boolean alreadyTriggered = false;

	@Inject
	private Client client;

	@Inject
	private AfkTimerConfig config;

	@Inject
	private Notifier notifier;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		final Player local = client.getLocalPlayer();

		if (client.getGameState() == GameState.LOGGED_IN || local != null)
		{
			final long lastKeyboardMillis = System.currentTimeMillis() - (client.getKeyboardIdleTicks() * 600);
			final long lastInteraction = Math.max(client.getMouseLastPressedMillis(), lastKeyboardMillis);
			final Duration waitDuration = Duration.ofSeconds(config.afkTimer());

			if (System.currentTimeMillis() > (lastInteraction + waitDuration.toMillis()))
			{
				if (!alreadyTriggered)
				{
					alreadyTriggered = true;
					log.info("Sending notification!");
					notifier.notify("You have been AFK for " + waitDuration.getSeconds() + " seconds!");
				}
			} else
			{
				alreadyTriggered = false;
			}
		}
	}

	@Provides
    AfkTimerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AfkTimerConfig.class);
	}
}
