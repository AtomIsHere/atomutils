package com.github.atomishere.exampleplugin;

import com.github.atomishere.atomutils.services.AtomService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TestService extends AtomService<ExamplePlugin> {
    public TestService(ExamplePlugin plugin) {
        super(plugin);
    }

    public void test() {
        plugin.getLogger().info("Test");
    }

    @Override
    protected void onStart() {
        plugin.getLogger().info("Test service started");
    }

    @Override
    protected void onStop() {
        plugin.getLogger().info("Test service stopped");
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent playerChatEvent) {
        plugin.getLogger().info(playerChatEvent.getMessage());
    }
}
