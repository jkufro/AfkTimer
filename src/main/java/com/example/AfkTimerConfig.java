package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("example")
public interface AfkTimerConfig extends Config
{
	@ConfigItem(
			keyName = "afkTimer",
			name = "AFK Timer",
			description = "How long to wait until sending an AFK notification."
	)
	@Units(Units.SECONDS)
	default int afkTimer()
	{
		return 240;
	}
}
