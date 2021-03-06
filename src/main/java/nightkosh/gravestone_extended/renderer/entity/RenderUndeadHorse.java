package nightkosh.gravestone_extended.renderer.entity;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;
import nightkosh.gravestone_extended.entity.monster.horse.EntityUndeadHorse;

import java.util.Map;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class RenderUndeadHorse extends RenderHorse {

    protected static final Map<String, ResourceLocation> TEXTURES_MAP = Maps.newHashMap();
    public static final String ZOMBIE_HORSE_TEXTURE = "textures/entity/horse/horse_zombie.png";
    public static final String SKELETON_HORSE_TEXTURE = "textures/entity/horse/horse_skeleton.png";
//    protected static final ResourceLocation zombieHorseTexture = new ResourceLocation(ZOMBIE_HORSE_TEXTURE);
//    protected static final ResourceLocation skeletonHorseTexture = new ResourceLocation(SKELETON_HORSE_TEXTURE);

    public RenderUndeadHorse(RenderManager renderManager, ModelHorse modelHorse, float p_i46170_3_) {
        super(renderManager, modelHorse, p_i46170_3_);
    }

    protected ResourceLocation getEntityTexture(EntityUndeadHorse horse) {
        if (!horse.hasLayeredTextures()) {
            return horse.getType().getTexture();
        } else {
            return this.getOrCreateLayeredResourceLoc(horse);
        }
    }

    protected ResourceLocation getOrCreateLayeredResourceLoc(EntityUndeadHorse horse) {
        String s = horse.getHorseTexture();

        if (!horse.hasTexture()) {
            return null;
        } else {
            ResourceLocation resourcelocation = TEXTURES_MAP.get(s);

            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation(s);
//                String horseTexture = "";
//                switch (horse.getHorseType()) {
//                    case 3:
//                        horseTexture = ZOMBIE_HORSE_TEXTURE;
//                        break;
//                    case 4:
//                        horseTexture = SKELETON_HORSE_TEXTURE;
//                        break;
//                }
//                Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(horseTexture, horse.getArmorTexturePaths()));
                Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(horse.getVariantTexturePaths()));
                TEXTURES_MAP.put(s, resourcelocation);
            }

            return resourcelocation;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHorse entity) {
        return this.getEntityTexture((EntityUndeadHorse) entity);
    }
}
