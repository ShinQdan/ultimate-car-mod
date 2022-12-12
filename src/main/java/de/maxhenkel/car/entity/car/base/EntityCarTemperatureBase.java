package de.maxhenkel.car.entity.car.base;

import de.maxhenkel.corelib.math.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityCarTemperatureBase extends EntityCarBase {

    private static final EntityDataAccessor<Float> TEMPERATURE = SynchedEntityData.defineId(EntityCarTemperatureBase.class, EntityDataSerializers.FLOAT);

    public EntityCarTemperatureBase(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            return;
        }
        if (tickCount % 20 != 0) {
            return;
        }

        float temp = getTemperature();

        float tempToReach = getTemperatureToReach();

        float change = (tempToReach-temp+random.nextFloat()-0.5F)/100;

        setTemperature(temp+change);
    }

    public float getTemperatureToReach() {
        float biomeTemp = getBiomeTemperatureCelsius();

        if (!isStarted()) {
            return biomeTemp;
        }
        return Math.max(biomeTemp, getOptimalTemperature());
    }

    public float getBiomeTemperatureCelsius() {
        return (level.getBiome(blockPosition()).value().getTemperature(blockPosition()) - 0.3F) * 30F;
    }

    public float getTemperature() {
        return this.entityData.get(TEMPERATURE);
    }

    public void setTemperature(float temperature) {
        this.entityData.set(TEMPERATURE, temperature);
    }

    public abstract float getOptimalTemperature();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEMPERATURE, 0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setTemperature(compound.getFloat("temperature"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("temperature", getTemperature());
    }

    /**
     * Sets the car temperature to the current temperature at the cars position
     */
    public void initTemperature() {
        setTemperature(getBiomeTemperatureCelsius());
    }

}
