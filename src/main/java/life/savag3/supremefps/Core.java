package life.savag3.supremefps;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class Core extends JavaPlugin {

    public static Core core;

    private static final int TNT_ID = 50;
    private static final int FALLING_BLOCK_ID = 70;
    private static final String ID_FIELD_NAME = "j";

    @Override
    public void onEnable() {
        core = this;
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY) {
                    @Override @SneakyThrows
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
                            StructureModifier<Entity> entityModifier = event.getPacket().getEntityModifier(event);
                            // Lazy Stream List. No Check for present.
                            Field fi = entityModifier.getFields().stream().filter(f -> f.getName().equals(ID_FIELD_NAME)).findFirst().get();
                            fi.setAccessible(true);
                            int x = fi.getInt(entityModifier.getTarget());
                            if (x == FALLING_BLOCK_ID || x == TNT_ID) event.setCancelled(true);
                        }
                    }
                }
        );
    }
}
