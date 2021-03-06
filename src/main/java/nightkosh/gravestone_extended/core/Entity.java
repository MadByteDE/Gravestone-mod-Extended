package nightkosh.gravestone_extended.core;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import nightkosh.gravestone_extended.config.ExtendedConfig;
import nightkosh.gravestone_extended.entity.EntityRaven;
import nightkosh.gravestone_extended.entity.helper.EntityGroupOfGravesMobSpawnerHelper;
import nightkosh.gravestone_extended.entity.monster.*;
import nightkosh.gravestone_extended.entity.monster.crawler.EntitySkullCrawler;
import nightkosh.gravestone_extended.entity.monster.crawler.EntityWitherSkullCrawler;
import nightkosh.gravestone_extended.entity.monster.crawler.EntityZombieSkullCrawler;
import nightkosh.gravestone_extended.entity.monster.horse.EntitySkeletonHorse;
import nightkosh.gravestone_extended.entity.monster.horse.EntityZombieHorse;
import nightkosh.gravestone_extended.entity.monster.pet.EntitySkeletonCat;
import nightkosh.gravestone_extended.entity.monster.pet.EntitySkeletonDog;
import nightkosh.gravestone_extended.entity.monster.pet.EntityZombieCat;
import nightkosh.gravestone_extended.entity.monster.pet.EntityZombieDog;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class Entity {

    private static Entity instance;
    private static int mobId = 0;

    private Entity() {
        instance = this;
        getEntity();
    }

    public static Entity getInstance() {
        if (instance == null) {
            return new Entity();
        } else {
            return instance;
        }
    }

    public static final String SKELETON_NAME = "GSSkeleton";
    public static final String ZOMBIE_DOG_NAME = "GSZombieDog";
    public static final String ZOMBIE_CAT_NAME = "GSZombieCat";
    public static final String SKELETON_DOG_NAME = "GSSkeletonDog";
    public static final String SKELETON_CAT_NAME = "GSSkeletonCat";
    public static final String SKULL_CRAWLER_NAME = "GSSkullCrawler";
    public static final String WITHER_SKULL_CRAWLER_NAME = "GSWitherSkullCrawler";
    public static final String ZOMBIE_SKULL_CRAWLER_NAME = "GSZombieSkullCrawler";
    public static final String ZOMBIE_HORSE_NAME = "GSZombieHorse";
    public static final String SKELETON_HORSE_NAME = "GSSkeletonHorse";
    public static final String SKELETON_RAIDER_NAME = "GSSkeletonRaider";
    public static final String ZOMBIE_RAIDER_NAME = "GSZombieRaider";
    public static final String RAVEN_NAME = "GSRaven";
    public static final String DAMNED_WARRIOR_NAME = "GSDamnedWarrior";
    public static final String SPAWNER_HELPER_NAME = "GSSpawnerHelper";

    // EntityList
    public static final String MINECRAFT_ZOMBIE_ID = "Zombie";
    public static final String MINECRAFT_PIGZOMBIE_ID = "PigZombie";
    public static final String MINECRAFT_SKELETON_ID = "Skeleton";
    public static final String MINECRAFT_SPIDER_ID = "Spider";
    public static final String MINECRAFT_CAVE_SPIDER_ID = "CaveSpider";
    public static final String MINECRAFT_WITHER_ID = "WitherBoss";
    public static final String MINECRAFT_ZOMBIE_HORSE_NAME = "zombiehorse";
    public static final String MINECRAFT_SKELETON_HORSE_NAME = "skeletonhorse";

    public static final String SKELETON_ID = ModInfo.ID + "." + SKELETON_NAME;
    public static final String ZOMBIE_DOG_ID = ModInfo.ID + "." + ZOMBIE_DOG_NAME;
    public static final String ZOMBIE_CAT_ID = ModInfo.ID + "." + ZOMBIE_CAT_NAME;
    public static final String SKELETON_DOG_ID = ModInfo.ID + "." + SKELETON_DOG_NAME;
    public static final String SKELETON_CAT_ID = ModInfo.ID + "." + SKELETON_CAT_NAME;
    public static final String SKULL_CRAWLER_ID = ModInfo.ID + "." + SKULL_CRAWLER_NAME;
    public static final String WITHER_SKULL_CRAWLER_ID = ModInfo.ID + "." + WITHER_SKULL_CRAWLER_NAME;
    public static final String ZOMBIE_SKULL_CRAWLER_ID = ModInfo.ID + "." + ZOMBIE_SKULL_CRAWLER_NAME;
    public static final String ZOMBIE_HORSE_ID = ModInfo.ID + "." + ZOMBIE_HORSE_NAME;
    public static final String SKELETON_HORSE_ID = ModInfo.ID + "." + SKELETON_HORSE_NAME;
    public static final String SKELETON_RAIDER_ID = ModInfo.ID + "." + SKELETON_RAIDER_NAME;
    public static final String ZOMBIE_RAIDER_ID = ModInfo.ID + "." + ZOMBIE_RAIDER_NAME;
    public static final String DAMNED_WARRIOR_ID = ModInfo.ID + "." + DAMNED_WARRIOR_NAME;
    public static final String RAVEN_ID = ModInfo.ID + "." + RAVEN_NAME;

    public void getEntity() {
        // zombie dog
        registerModEntity(EntityZombieDog.class, ZOMBIE_DOG_NAME);
        if (ExtendedConfig.spawnZombieDogs) {
            EntityRegistry.addSpawn(EntityZombieDog.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));
        }

        // zombie cat
        registerModEntity(EntityZombieCat.class, ZOMBIE_CAT_NAME);
        if (ExtendedConfig.spawnZombieCats) {
            EntityRegistry.addSpawn(EntityZombieCat.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE));
        }

        // skeleton dog
        registerModEntity(EntitySkeletonDog.class, SKELETON_DOG_NAME);
        if (ExtendedConfig.spawnSkeletonDogs) {
            EntityRegistry.addSpawn(EntityZombieDog.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));
        }

        // skeleton cat
        registerModEntity(EntitySkeletonCat.class, SKELETON_CAT_NAME);
        if (ExtendedConfig.spawnSkeletonCats) {
            EntityRegistry.addSpawn(EntityZombieCat.class, 2, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE));
        }

        // skull crawlers
        registerModEntity(EntitySkullCrawler.class, SKULL_CRAWLER_NAME);
        // wither
        registerModEntity(EntityWitherSkullCrawler.class, WITHER_SKULL_CRAWLER_NAME);
        EntityRegistry.addSpawn(EntityWitherSkullCrawler.class, 3, 1, 4, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.NETHER));
        // zombie
        registerModEntity(EntityZombieSkullCrawler.class, ZOMBIE_SKULL_CRAWLER_NAME);

        registerModEntity(EntityGSSkeleton.class, SKELETON_NAME);
        EntityRegistry.addSpawn(EntityGSSkeleton.class, 1, 1, 3, EnumCreatureType.MONSTER);

        registerModEntity(EntityZombieHorse.class, ZOMBIE_HORSE_NAME);
        registerModEntity(EntitySkeletonHorse.class, SKELETON_HORSE_NAME);
        if (ExtendedConfig.spawnUndeadHorses) {
            EntityRegistry.addSpawn(EntityZombieHorse.class, 2, 1, 3, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
            EntityRegistry.addSpawn(EntitySkeletonHorse.class, 2, 1, 3, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
        }

        registerModEntity(EntityZombieRaider.class, ZOMBIE_RAIDER_NAME);
        if (ExtendedConfig.spawnZombieRaiders) {
            EntityRegistry.addSpawn(EntityZombieRaider.class, 1, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
        }
        registerModEntity(EntitySkeletonRaider.class, SKELETON_RAIDER_NAME);
        if (ExtendedConfig.spawnSkeletonRaiders) {
            EntityRegistry.addSpawn(EntitySkeletonRaider.class, 1, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
        }

        registerModEntity(EntityRaven.class, RAVEN_NAME);
//        EntityRegistry.addSpawn(EntityRaven.class, 1, 3, 10, EnumCreatureType.AMBIENT);//TODO!!!!

        // Damned Warrior
        registerModEntity(EntityDamnedWarrior.class, DAMNED_WARRIOR_NAME);

        // ghosts
        // LostSoul
        //EntityRegistry.registerGlobalEntityID(EntityLostSoul.class, "GSLostSoul", EntityRegistry.findGlobalUniqueEntityId(), 15720061, 4802889);
        //EntityRegistry.addSpawn(EntityLostSoul.class, 3, 1, 3, EnumCreatureType.MONSTER, BiomeGenBase.jungle, BiomeGenBase.jungleHills);

        // VengefulSpirit
        //EntityRegistry.registerGlobalEntityID(EntityVengefulSpirit.class, "GSVengefulSpirit", EntityRegistry.findGlobalUniqueEntityId(), 15720061, 4802889);
        //EntityRegistry.addSpawn(EntityVengefulSpirit.class, 3, 1, 3, EnumCreatureType.MONSTER, BiomeGenBase.jungle, BiomeGenBase.jungleHills);


        registerModEntity(EntityGroupOfGravesMobSpawnerHelper.class, SPAWNER_HELPER_NAME);
    }

    private void registerModEntity(Class<? extends net.minecraft.entity.Entity> entityClass, String entityName) {
        registerModEntity(entityClass, entityName, mobId, ModInfo.ID, 100, 1, true);
        mobId++;
    }

    private void registerModEntity(Class<? extends net.minecraft.entity.Entity> entityClass, String entityName, int id,
                                   Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
}
