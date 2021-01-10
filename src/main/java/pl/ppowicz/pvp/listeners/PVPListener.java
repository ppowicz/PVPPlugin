package pl.ppowicz.pvp.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.ppowicz.pvp.PVPPlugin;

public class PVPListener implements Listener{
  private PVPPlugin plugin;

  public PVPListener(PVPPlugin parent) {
    plugin = parent;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

    if (attribute == null) {
      return;
    }

    attribute.setBaseValue(5);
    player.saveData();
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();

      if (event.getCause().equals(DamageCause.FALL)) {
        return;
      }

      if (event.getCause().equals(DamageCause.FLY_INTO_WALL)) {
        return;
      }

      if (plugin.getTimer(player) == null) {
        plugin.addTimer(player);
      }
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (plugin.getTimer(player) != null) {
      player.setHealth(0);
      plugin.getServer().broadcastMessage("§c" + player.getName() + " wylogował się podczas walki!");
    }
  }
}
