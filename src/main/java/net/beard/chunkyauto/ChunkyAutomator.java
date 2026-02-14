package net.beard.chunkyauto;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber
public class ChunkyAutomator {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftServer server = event.getEntity().getServer();
        if (server != null) {
            final CommandSourceStack commandSourceStack = server.createCommandSourceStack();
            runCommand(server, commandSourceStack, "chunky pause");
        }
    }

    @SubscribeEvent
    public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        final Set<ServerPlayer> players = new HashSet<>(server.getPlayerList().getPlayers());
        players.remove(player);
        final int playerCount = players.size();

        if (playerCount == 0) {
            final CommandSourceStack commandSourceStack = server.createCommandSourceStack();
            runCommand(server, commandSourceStack, "chunky continue");
            runCommand(server, commandSourceStack, "chunky quiet 600");
        } else {
            System.out.println("Server player count: " + playerCount);
        }
    }

    private static void runCommand(MinecraftServer server, CommandSourceStack commandSourceStack, String command) {
        System.out.println("Running command: " + command);
        server.getCommands().performPrefixedCommand(commandSourceStack, command);
    }
}