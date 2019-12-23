package de.maxhenkel.car.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.car.entity.car.base.EntityGenericCar;
import de.maxhenkel.car.entity.model.obj.OBJModelInstance;
import de.maxhenkel.car.entity.model.obj.OBJModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class GenericCarModel extends OBJModelRenderer {

    public GenericCarModel(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public List<OBJModelInstance> getModels(EntityGenericCar entity) {
        return entity.getModels();
    }

    @Override
    public void translateLicensePlate(EntityGenericCar entity, MatrixStack matrixStack) {
        Vec3d offset = entity.getLicensePlateOffset();
        matrixStack.func_227861_a_(offset.x, offset.y, offset.z);
    }
}