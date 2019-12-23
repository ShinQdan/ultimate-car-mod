package de.maxhenkel.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.car.entity.car.base.EntityCarBase;
import de.maxhenkel.car.entity.car.base.EntityGenericCar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityTools {

    /**
     * Gets the first car in the range of 10 blocks of the player
     *
     * @param player
     * @param uuid   the UUID of the car
     * @return the car or null
     */
    @Nullable
    public static EntityGenericCar getCarByUUID(PlayerEntity player, UUID uuid) {
        double distance = 10D;
        return player.world.getEntitiesWithinAABB(EntityGenericCar.class, new AxisAlignedBB(player.func_226277_ct_() - distance, player.func_226278_cu_() - distance, player.func_226281_cx_() - distance, player.func_226277_ct_() + distance, player.func_226278_cu_() + distance, player.func_226281_cx_() + distance), entity -> entity.getUniqueID().equals(uuid)).stream().findAny().orElse(null);
    }

    public static void drawCarOnScreen(EntityCarBase car, int posX, int posY, int scale, float rotation) {
        RenderSystem.pushMatrix();

        RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.func_227861_a_(0.0D, 0.0D, 1000.0D);
        matrixstack.func_227862_a_((float) scale, (float) scale, (float) scale);

        matrixstack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(135F + rotation));

        Quaternion quaternion = Vector3f.field_229183_f_.func_229187_a_(180.0F);
        matrixstack.func_227863_a_(quaternion);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().func_228019_au_().func_228487_b_();
        entityrenderermanager.func_229084_a_(car, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, buffer, 15728880);
        buffer.func_228461_a_();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }

    public static class CarRenderer {
        private float rotation;
        private float rotationPerTick;
        private Minecraft minecraft;

        public CarRenderer(float rotationPerTick) {
            this.rotationPerTick = rotationPerTick;
            this.minecraft = Minecraft.getInstance();
        }

        public CarRenderer() {
            this(3.6F);
        }

        public void tick() {
            rotation += rotationPerTick;
            if (rotation >= 360F) {
                rotation = 0F;
            }
        }

        public void render(EntityCarBase car, int posX, int posY, int scale) {
            EntityTools.drawCarOnScreen(car, posX, posY, scale, rotation + rotationPerTick * minecraft.getRenderPartialTicks());
        }
    }

    public static class SimulatedCarRenderer {
        private float rotation;
        private float rotationPerTick;
        private SimulatedTicker ticker;

        public SimulatedCarRenderer(float rotationPerTick) {
            this.rotationPerTick = rotationPerTick;
            ticker = new SimulatedTicker();
        }

        public SimulatedCarRenderer() {
            this(3.6F);
        }

        public void render(EntityCarBase car, int posX, int posY, int scale) {
            ticker.render(new Renderer() {
                @Override
                public void render(float partialTicks) {
                    EntityTools.drawCarOnScreen(car, posX, posY, scale, rotation + rotationPerTick * partialTicks);
                }

                @Override
                public void tick() {
                    rotation += rotationPerTick;
                    if (rotation >= 360F) {
                        rotation = 0F;
                    }
                }
            });
        }
    }

    public static class SimulatedTicker {
        private static final long ONE_TICK = 50_000_000L;
        private long lastTick;

        public void render(Renderer renderer) {
            long currentNanos = System.nanoTime();

            if (currentNanos - lastTick >= ONE_TICK) {
                renderer.tick();
                lastTick = currentNanos;
            }
            renderer.render((float) (currentNanos - lastTick) / (float) ONE_TICK);
        }
    }

    public interface Renderer {
        void render(float partialTicks);

        void tick();
    }

}
