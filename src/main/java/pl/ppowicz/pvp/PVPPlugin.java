package pl.ppowicz.pvp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import pl.ppowicz.pvp.listeners.PVPListener;

public class PVPPlugin extends JavaPlugin {
  private Map<Player, Integer> combatLogTimers = new HashMap<Player, Integer>();

  public void addTimer(Player player) {
    combatLogTimers.put(player, 5);
  }

  public void removeTimer(Player player) {
    combatLogTimers.remove(player);
  }

  public Integer getTimer(Player player) {
    return combatLogTimers.get(player);
  }


  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new PVPListener(this), this);
    startCounter();
  }

  public void startCounter() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        Iterator<Map.Entry<Player, Integer>> iterator = combatLogTimers.entrySet().iterator();

        while (iterator.hasNext()) {
          Entry<Player, Integer> next = iterator.next();
          Player player = next.getKey();
          Integer timer = next.getValue();

          if (timer >= 0) {
            combatLogTimers.replace(player, timer - 1);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cNie wylogowuj się!"));
          } else {
            removeTimer(player);
          }
        }
      }
    }, 0, 20);
  }
}