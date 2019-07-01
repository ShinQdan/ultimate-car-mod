package de.maxhenkel.car.net;

import de.maxhenkel.car.entity.car.base.EntityCarBatteryBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class MessageStarting implements Message<MessageStarting> {

    private boolean start;
    private boolean playSound;
    private UUID uuid;

    public MessageStarting() {
        this.start = true;
        this.uuid = new UUID(0, 0);
    }

    public MessageStarting(boolean start, boolean playSound, PlayerEntity player) {
        this.start = start;
        this.playSound = playSound;
        this.uuid = player.getUniqueID();
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        if (!context.getSender().getUniqueID().equals(uuid)) {
            System.out.println("---------UUID was not the same-----------");
            return;
        }

        Entity riding = context.getSender().getRidingEntity();

        if (!(riding instanceof EntityCarBatteryBase)) {
            return;
        }

        EntityCarBatteryBase car = (EntityCarBatteryBase) riding;
        if (context.getSender().equals(car.getDriver())) {
            car.setStarting(start, playSound);
        }
    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {

    }

    @Override
    public MessageStarting fromBytes(PacketBuffer buf) {
        this.start = buf.readBoolean();
        this.playSound = buf.readBoolean();

        long l1 = buf.readLong();
        long l2 = buf.readLong();
        this.uuid = new UUID(l1, l2);

        return this;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(start);
        buf.writeBoolean(playSound);

        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }
}
