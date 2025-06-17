package dev.by1337.yaml;


import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.InlineYamlCodecBuilder;
import dev.by1337.yaml.codec.RecordYamlCodecBuilder;
import dev.by1337.yaml.codec.YamlCodec;
import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.inventory.ItemRarity;
import io.papermc.paper.world.MoonPhase;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.*;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.boss.*;
import org.bukkit.conversations.Conversation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.material.types.MushroomBlockTexture;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class BukkitYamlCodecs {

    public static final YamlCodec<Vector> VECTOR = InlineYamlCodecBuilder.inline(
            " ", "<x> <y> <z>",
            Vector::new,
            YamlCodec.DOUBLE.withGetter(Vector::getX),
            YamlCodec.DOUBLE.withGetter(Vector::getY),
            YamlCodec.DOUBLE.withGetter(Vector::getZ)
    ).whenMap(RecordYamlCodecBuilder.mapOf(
            Vector::new,
            YamlCodec.DOUBLE.fieldOf("x", Vector::getX),
            YamlCodec.DOUBLE.fieldOf("y", Vector::getY),
            YamlCodec.DOUBLE.fieldOf("z", Vector::getZ)
    ));

    public static final YamlCodec<BoundingBox> BOUNDING_BOX = RecordYamlCodecBuilder.mapOf(
            BoundingBox::new,
            YamlCodec.DOUBLE.fieldOf("minX", BoundingBox::getMinX),
            YamlCodec.DOUBLE.fieldOf("minY", BoundingBox::getMinY),
            YamlCodec.DOUBLE.fieldOf("minZ", BoundingBox::getMinZ),
            YamlCodec.DOUBLE.fieldOf("maxX", BoundingBox::getMaxX),
            YamlCodec.DOUBLE.fieldOf("maxY", BoundingBox::getMaxY),
            YamlCodec.DOUBLE.fieldOf("maxZ", BoundingBox::getMaxZ)
    );
    public static final YamlCodec<NamespacedKey> NAMESPACED_KEY = YamlCodec.STRING.flatMap(s -> {
        try {
            if (s.contains(":")) {
                NamespacedKey key = NamespacedKey.fromString(s);
                if (key == null)
                    return DataResult.error("Failed to decode NamespacedKey: Expected: '<space>:<name>', but got " + s);
                return DataResult.success(key);
            } else {
                return DataResult.success(NamespacedKey.minecraft(s));
            }
        } catch (Throwable e) {
            return DataResult.error("Failed to decode NamespacedKey: " + e.getMessage());
        }
    }, NamespacedKey::asString);

    public static final YamlCodec<StructureType> STRUCTURE_TYPE = YamlCodec.STRING
            .map(s -> StructureType.getStructureTypes().get(s.toLowerCase()), StructureType::getName);
    public static final YamlCodec<java.util.UUID> UUID = YamlCodec.STRING.map(java.util.UUID::fromString, java.util.UUID::toString);

    public static final YamlCodec<OfflinePlayer> OFFLINE_PLAYER = UUID.map(Bukkit::getOfflinePlayer, OfflinePlayer::getUniqueId);
    public static final YamlCodec<World> WORLD = YamlCodec.STRING.map(Bukkit::getWorld, World::getName);
    public static final YamlCodec<GameRule<?>> GAME_RULE = YamlCodec.STRING.map(GameRule::getByName, GameRule::getName);
    public static final YamlCodec<Color> COLOR = YamlCodec.STRING.flatMap(s -> {
        try {
            return DataResult.success(ColorUtil.fromHex(s));
        } catch (Throwable e) {
            return DataResult.error("Failed to decode color: Expected: '#rrggbb', but got " + s);
        }
    }, ColorUtil::toHex);

    public static final YamlCodec<EulerAngle> EULER_ANGLE = InlineYamlCodecBuilder.inline(
            " ", "<x> <y> <z>",
            EulerAngle::new,
            YamlCodec.DOUBLE.withGetter(EulerAngle::getX),
            YamlCodec.DOUBLE.withGetter(EulerAngle::getY),
            YamlCodec.DOUBLE.withGetter(EulerAngle::getZ)
    ).whenMap(RecordYamlCodecBuilder.mapOf(
            EulerAngle::new,
            YamlCodec.DOUBLE.fieldOf("x", EulerAngle::getX),
            YamlCodec.DOUBLE.fieldOf("y", EulerAngle::getY),
            YamlCodec.DOUBLE.fieldOf("z", EulerAngle::getZ)
    ));
    public static final YamlCodec<PotionEffectType> POTION_EFFECT_TYPE = YamlCodec.STRING.map(PotionEffectType::getByName, PotionEffectType::getName);
    public static final YamlCodec<PotionType> POTION_TYPE = getEnumCodecMaybeKeyed(PotionType.class, PotionType.values());
    public static final YamlCodec<ServicePriority> SERVICE_PRIORITY = YamlCodec.enumOf(ServicePriority.class);
    public static final YamlCodec<WorldType> WORLD_TYPE = getEnumCodecMaybeKeyed(WorldType.class, WorldType.values());

    public static final YamlCodec<Advancement> ADVANCEMENT = new KeyedYamlCodec<>(Registry.ADVANCEMENT, Advancement.class.getSimpleName());
    public static final YamlCodec<Art> ART = new KeyedYamlCodec<>(Registry.ART, Art.class.getSimpleName());
    public static final YamlCodec<Attribute> ATTRIBUTE = new KeyedYamlCodec<>(Registry.ATTRIBUTE, Attribute.class.getSimpleName());
    public static final YamlCodec<Biome> BIOME = new KeyedYamlCodec<>(Registry.BIOME, Biome.class.getSimpleName());
    public static final YamlCodec<KeyedBossBar> BOSS_BAR = new KeyedYamlCodec<>(Registry.BOSS_BARS, KeyedBossBar.class.getSimpleName());
    public static final YamlCodec<Enchantment> ENCHANTMENT = new KeyedYamlCodec<>(Registry.ENCHANTMENT, Enchantment.class.getSimpleName());
    public static final YamlCodec<EntityType> ENTITY_TYPE = new KeyedYamlCodec<>(Registry.ENTITY_TYPE, EntityType.class.getSimpleName());
    public static final YamlCodec<LootTables> LOOT_TABLE = new KeyedYamlCodec<>(Registry.LOOT_TABLES, LootTables.class.getSimpleName());
    public static final YamlCodec<Material> MATERIAL = new KeyedYamlCodec<>(Registry.MATERIAL, Material.class.getSimpleName());
    public static final YamlCodec<Statistic> STATISTIC = new KeyedYamlCodec<>(Registry.STATISTIC, Statistic.class.getSimpleName());
    public static final YamlCodec<Sound> SOUND = new KeyedYamlCodec<>(Registry.SOUNDS, Sound.class.getSimpleName());
    public static final YamlCodec<Villager.Profession> VILLAGER_PROFESSION = new KeyedYamlCodec<>(Registry.VILLAGER_PROFESSION, Villager.Profession.class.getSimpleName());
    public static final YamlCodec<Villager.Type> VILLAGER_TYPE = new KeyedYamlCodec<>(Registry.VILLAGER_TYPE, Villager.Type.class.getSimpleName());
    @SuppressWarnings({"rawtypes"})
    public static final YamlCodec<MemoryKey> MEMORY_MODULE_TYPE = new KeyedYamlCodec<>(Registry.MEMORY_MODULE_TYPE, MemoryKey.class.getSimpleName());
    public static final YamlCodec<Fluid> FLUID = new KeyedYamlCodec<>(Registry.FLUID, Fluid.class.getSimpleName());


    public static final YamlCodec<EntityCategory> ENTITY_CATEGORY = getEnumCodecMaybeKeyed(EntityCategory.class, EntityCategory.values());
    public static final YamlCodec<StructureRotation> STRUCTURE_ROTATION = getEnumCodecMaybeKeyed(StructureRotation.class, StructureRotation.values());
    public static final YamlCodec<TreeType> TREE_TYPE = getEnumCodecMaybeKeyed(TreeType.class, TreeType.values());
    public static final YamlCodec<PatternType> PATTERN_TYPE = getEnumCodecMaybeKeyed(PatternType.class, PatternType.values());
    public static final YamlCodec<LazyMetadataValue.CacheStrategy> LAZY_METADATA_VALUE_CACHE_STRATEGY = getEnumCodecMaybeKeyed(LazyMetadataValue.CacheStrategy.class, LazyMetadataValue.CacheStrategy.values());
    public static final YamlCodec<Rail.Shape> RAIL_SHAPE = getEnumCodecMaybeKeyed(Rail.Shape.class, Rail.Shape.values());
    public static final YamlCodec<TreeSpecies> TREE_SPECIES = getEnumCodecMaybeKeyed(TreeSpecies.class, TreeSpecies.values());
    public static final YamlCodec<RedstoneWire.Connection> REDSTONE_WIRE_CONNECTION = getEnumCodecMaybeKeyed(RedstoneWire.Connection.class, RedstoneWire.Connection.values());
    public static final YamlCodec<EnchantmentTarget> ENCHANTMENT_TARGET = getEnumCodecMaybeKeyed(EnchantmentTarget.class, EnchantmentTarget.values());
    public static final YamlCodec<BarColor> BAR_COLOR = getEnumCodecMaybeKeyed(BarColor.class, BarColor.values());
    public static final YamlCodec<InventoryView.Property> INVENTORY_VIEW_PROPERTY = getEnumCodecMaybeKeyed(InventoryView.Property.class, InventoryView.Property.values());
    public static final YamlCodec<EquipmentSlot> EQUIPMENT_SLOT = getEnumCodecMaybeKeyed(EquipmentSlot.class, EquipmentSlot.values());
    public static final YamlCodec<Rabbit.Type> RABBIT_TYPE = getEnumCodecMaybeKeyed(Rabbit.Type.class, Rabbit.Type.values());
    public static final YamlCodec<Axis> AXIS = getEnumCodecMaybeKeyed(Axis.class, Axis.values());
    public static final YamlCodec<MushroomBlockTexture> MUSHROOM_BLOCK_TEXTURE = getEnumCodecMaybeKeyed(MushroomBlockTexture.class, MushroomBlockTexture.values());
    public static final YamlCodec<PistonMoveReaction> PISTON_MOVE_REACTION = getEnumCodecMaybeKeyed(PistonMoveReaction.class, PistonMoveReaction.values());
    public static final YamlCodec<MainHand> MAIN_HAND = getEnumCodecMaybeKeyed(MainHand.class, MainHand.values());
    public static final YamlCodec<BanList.Type> BAN_LIST_TYPE = getEnumCodecMaybeKeyed(BanList.Type.class, BanList.Type.values());
    public static final YamlCodec<PluginAwareness.Flags> PLUGIN_AWARENESS_FLAGS = getEnumCodecMaybeKeyed(PluginAwareness.Flags.class, PluginAwareness.Flags.values());
    public static final YamlCodec<BarFlag> BAR_FLAG = getEnumCodecMaybeKeyed(BarFlag.class, BarFlag.values());
    public static final YamlCodec<Team.Option> TEAM_OPTION = getEnumCodecMaybeKeyed(Team.Option.class, Team.Option.values());
    public static final YamlCodec<RenderType> RENDER_TYPE = getEnumCodecMaybeKeyed(RenderType.class, RenderType.values());
    public static final YamlCodec<Stairs.Shape> STAIRS_SHAPE = getEnumCodecMaybeKeyed(Stairs.Shape.class, Stairs.Shape.values());
    public static final YamlCodec<Llama.Color> LLAMA_COLOR = getEnumCodecMaybeKeyed(Llama.Color.class, Llama.Color.values());
    public static final YamlCodec<MapView.Scale> MAP_VIEW_SCALE = getEnumCodecMaybeKeyed(MapView.Scale.class, MapView.Scale.values());
    public static final YamlCodec<TropicalFish.Pattern> TROPICAL_FISH_PATTERN = getEnumCodecMaybeKeyed(TropicalFish.Pattern.class, TropicalFish.Pattern.values());
    public static final YamlCodec<StructureBlock.Mode> STRUCTURE_BLOCK_MODE = getEnumCodecMaybeKeyed(StructureBlock.Mode.class, StructureBlock.Mode.values());
    public static final YamlCodec<InventoryType.SlotType> INVENTORY_TYPE_SLOT_TYPE = getEnumCodecMaybeKeyed(InventoryType.SlotType.class, InventoryType.SlotType.values());
    public static final YamlCodec<SoundCategory> SOUND_CATEGORY = getEnumCodecMaybeKeyed(SoundCategory.class, SoundCategory.values());
    public static final YamlCodec<FaceAttachable.AttachedFace> FACE_ATTACHABLE_ATTACHED_FACE = getEnumCodecMaybeKeyed(FaceAttachable.AttachedFace.class, FaceAttachable.AttachedFace.values());
    public static final YamlCodec<Slab.Type> SLAB_TYPE = getEnumCodecMaybeKeyed(Slab.Type.class, Slab.Type.values());
    public static final YamlCodec<HeightMap> HEIGHT_MAP = getEnumCodecMaybeKeyed(HeightMap.class, HeightMap.values());
    public static final YamlCodec<Statistic.Type> STATISTIC_TYPE = getEnumCodecMaybeKeyed(Statistic.Type.class, Statistic.Type.values());
    public static final YamlCodec<Panda.Gene> PANDA_GENE = getEnumCodecMaybeKeyed(Panda.Gene.class, Panda.Gene.values());
    public static final YamlCodec<ArmorStand.LockType> ARMOR_STAND_LOCK_TYPE = getEnumCodecMaybeKeyed(ArmorStand.LockType.class, ArmorStand.LockType.values());
    public static final YamlCodec<UsageMode> USAGE_MODE = getEnumCodecMaybeKeyed(UsageMode.class, UsageMode.values());
    public static final YamlCodec<Pose> POSE = getEnumCodecMaybeKeyed(Pose.class, Pose.values());
    public static final YamlCodec<Effect.Type> EFFECT_TYPE = getEnumCodecMaybeKeyed(Effect.Type.class, Effect.Type.values());
    public static final YamlCodec<DyeColor> DYE_COLOR = getEnumCodecMaybeKeyed(DyeColor.class, DyeColor.values());
    public static final YamlCodec<ItemFlag> ITEM_FLAG = getEnumCodecMaybeKeyed(ItemFlag.class, ItemFlag.values());
    public static final YamlCodec<PortalType> PORTAL_TYPE = getEnumCodecMaybeKeyed(PortalType.class, PortalType.values());
    public static final YamlCodec<EntityEffect> ENTITY_EFFECT = getEnumCodecMaybeKeyed(EntityEffect.class, EntityEffect.values());
    public static final YamlCodec<Door.Hinge> DOOR_HINGE = getEnumCodecMaybeKeyed(Door.Hinge.class, Door.Hinge.values());
    public static final YamlCodec<Bisected.Half> BISECTED_HALF = getEnumCodecMaybeKeyed(Bisected.Half.class, Bisected.Half.values());
    public static final YamlCodec<Conversation.ConversationState> CONVERSATION_CONVERSATION_STATE = getEnumCodecMaybeKeyed(Conversation.ConversationState.class, Conversation.ConversationState.values());
    public static final YamlCodec<NetherWartsState> NETHER_WARTS_STATE = getEnumCodecMaybeKeyed(NetherWartsState.class, NetherWartsState.values());
    public static final YamlCodec<CropState> CROP_STATE = getEnumCodecMaybeKeyed(CropState.class, CropState.values());
    public static final YamlCodec<Spellcaster.Spell> SPELLCASTER_SPELL = getEnumCodecMaybeKeyed(Spellcaster.Spell.class, Spellcaster.Spell.values());
    public static final YamlCodec<World.Environment> WORLD_ENVIRONMENT = getEnumCodecMaybeKeyed(World.Environment.class, World.Environment.values());
    public static final YamlCodec<BlockFace> BLOCK_FACE = getEnumCodecMaybeKeyed(BlockFace.class, BlockFace.values());
    public static final YamlCodec<ExperienceOrb.SpawnReason> EXPERIENCE_ORB_SPAWN_REASON = getEnumCodecMaybeKeyed(ExperienceOrb.SpawnReason.class, ExperienceOrb.SpawnReason.values());
    public static final YamlCodec<CoalType> COAL_TYPE = getEnumCodecMaybeKeyed(CoalType.class, CoalType.values());
    public static final YamlCodec<EnderDragon.Phase> ENDER_DRAGON_PHASE = getEnumCodecMaybeKeyed(EnderDragon.Phase.class, EnderDragon.Phase.values());
    public static final YamlCodec<DragonBattle.RespawnPhase> DRAGON_BATTLE_RESPAWN_PHASE = getEnumCodecMaybeKeyed(DragonBattle.RespawnPhase.class, DragonBattle.RespawnPhase.values());
    public static final YamlCodec<AbstractArrow.PickupStatus> ABSTRACT_ARROW_PICKUP_STATUS = getEnumCodecMaybeKeyed(AbstractArrow.PickupStatus.class, AbstractArrow.PickupStatus.values());
    public static final YamlCodec<Chest.Type> CHEST_TYPE = getEnumCodecMaybeKeyed(Chest.Type.class, Chest.Type.values());
    public static final YamlCodec<Difficulty> DIFFICULTY = getEnumCodecMaybeKeyed(Difficulty.class, Difficulty.values());
    public static final YamlCodec<TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = getEnumCodecMaybeKeyed(TechnicalPiston.Type.class, TechnicalPiston.Type.values());
    public static final YamlCodec<Instrument> INSTRUMENT = getEnumCodecMaybeKeyed(Instrument.class, Instrument.values());
    public static final YamlCodec<Bed.Part> BED_PART = getEnumCodecMaybeKeyed(Bed.Part.class, Bed.Part.values());
    public static final YamlCodec<PermissionDefault> PERMISSION_DEFAULT = getEnumCodecMaybeKeyed(PermissionDefault.class, PermissionDefault.values());
    public static final YamlCodec<GrassSpecies> GRASS_SPECIES = getEnumCodecMaybeKeyed(GrassSpecies.class, GrassSpecies.values());
    public static final YamlCodec<Parrot.Variant> PARROT_VARIANT = getEnumCodecMaybeKeyed(Parrot.Variant.class, Parrot.Variant.values());
    public static final YamlCodec<Rotation> ROTATION = getEnumCodecMaybeKeyed(Rotation.class, Rotation.values());
    public static final YamlCodec<FluidCollisionMode> FLUID_COLLISION_MODE = getEnumCodecMaybeKeyed(FluidCollisionMode.class, FluidCollisionMode.values());
    public static final YamlCodec<DisplaySlot> DISPLAY_SLOT = getEnumCodecMaybeKeyed(DisplaySlot.class, DisplaySlot.values());
    public static final YamlCodec<ClickType> CLICK_TYPE = getEnumCodecMaybeKeyed(ClickType.class, ClickType.values());
    public static final YamlCodec<Horse.Color> HORSE_COLOR = getEnumCodecMaybeKeyed(Horse.Color.class, Horse.Color.values());
    public static final YamlCodec<Note.Tone> NOTE_TONE = getEnumCodecMaybeKeyed(Note.Tone.class, Note.Tone.values());
    public static final YamlCodec<Action> BLOCK_CLICK_ACTION = getEnumCodecMaybeKeyed(Action.class, Action.values());
    public static final YamlCodec<Mirror> MIRROR = getEnumCodecMaybeKeyed(Mirror.class, Mirror.values());
    public static final YamlCodec<GameMode> GAME_MODE = getEnumCodecMaybeKeyed(GameMode.class, GameMode.values());
    public static final YamlCodec<Effect> EFFECT = getEnumCodecMaybeKeyed(Effect.class, Effect.values());
    public static final YamlCodec<Cat.Type> CAT_TYPE = getEnumCodecMaybeKeyed(Cat.Type.class, Cat.Type.values());
    public static final YamlCodec<DragType> DRAG_TYPE = getEnumCodecMaybeKeyed(DragType.class, DragType.values());
    public static final YamlCodec<Wall.Height> WALL_HEIGHT = getEnumCodecMaybeKeyed(Wall.Height.class, Wall.Height.values());
    public static final YamlCodec<FishHook.HookState> FISH_HOOK_HOOK_STATE = getEnumCodecMaybeKeyed(FishHook.HookState.class, FishHook.HookState.values());
    public static final YamlCodec<Fox.Type> FOX_TYPE = getEnumCodecMaybeKeyed(Fox.Type.class, Fox.Type.values());
    public static final YamlCodec<Bell.Attachment> BELL_ATTACHMENT = getEnumCodecMaybeKeyed(Bell.Attachment.class, Bell.Attachment.values());
    public static final YamlCodec<Jigsaw.Orientation> JIGSAW_ORIENTATION = getEnumCodecMaybeKeyed(Jigsaw.Orientation.class, Jigsaw.Orientation.values());
    public static final YamlCodec<BarStyle> BAR_STYLE = getEnumCodecMaybeKeyed(BarStyle.class, BarStyle.values());
    public static final YamlCodec<MapCursor.Type> MAP_CURSOR_TYPE = getEnumCodecMaybeKeyed(MapCursor.Type.class, MapCursor.Type.values());
    public static final YamlCodec<Comparator.Mode> COMPARATOR_MODE = getEnumCodecMaybeKeyed(Comparator.Mode.class, Comparator.Mode.values());
    public static final YamlCodec<Horse.Style> HORSE_STYLE = getEnumCodecMaybeKeyed(Horse.Style.class, Horse.Style.values());
    public static final YamlCodec<WeatherType> WEATHER_TYPE = getEnumCodecMaybeKeyed(WeatherType.class, WeatherType.values());
    public static final YamlCodec<FireworkEffect.Type> FIREWORK_EFFECT_TYPE = getEnumCodecMaybeKeyed(FireworkEffect.Type.class, FireworkEffect.Type.values());
    public static final YamlCodec<BookMeta.Generation> BOOK_META_GENERATION = getEnumCodecMaybeKeyed(BookMeta.Generation.class, BookMeta.Generation.values());
    public static final YamlCodec<InventoryAction> INVENTORY_ACTION = getEnumCodecMaybeKeyed(InventoryAction.class, InventoryAction.values());
    public static final YamlCodec<InventoryType> INVENTORY_TYPE = getEnumCodecMaybeKeyed(InventoryType.class, InventoryType.values());
    public static final YamlCodec<SandstoneType> SANDSTONE_TYPE = getEnumCodecMaybeKeyed(SandstoneType.class, SandstoneType.values());
    public static final YamlCodec<Bamboo.Leaves> BAMBOO_LEAVES = getEnumCodecMaybeKeyed(Bamboo.Leaves.class, Bamboo.Leaves.values());
    public static final YamlCodec<Particle> PARTICLE = getEnumCodecMaybeKeyed(Particle.class, Particle.values());
    public static final YamlCodec<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION = getEnumCodecMaybeKeyed(AttributeModifier.Operation.class, AttributeModifier.Operation.values());
    public static final YamlCodec<Team.OptionStatus> TEAM_OPTION_STATUS = getEnumCodecMaybeKeyed(Team.OptionStatus.class, Team.OptionStatus.values());
    public static final YamlCodec<ChatColor> CHAT_COLOR = getEnumCodecMaybeKeyed(ChatColor.class, ChatColor.values());
    public static final YamlCodec<MushroomCow.Variant> MUSHROOM_COW_VARIANT = getEnumCodecMaybeKeyed(MushroomCow.Variant.class, MushroomCow.Variant.values());
    public static final YamlCodec<Raid.RaidStatus> RAID_RAID_STATUS = getEnumCodecMaybeKeyed(Raid.RaidStatus.class, Raid.RaidStatus.values());


    public static final YamlCodec<Datapack.Compatibility> DATAPACK_COMPATIBILITY = getEnumCodecMaybeKeyed(Datapack.Compatibility.class, Datapack.Compatibility.values());
    public static final YamlCodec<ItemRarity> ITEM_RARITY = getEnumCodecMaybeKeyed(ItemRarity.class, ItemRarity.values());
    public static final YamlCodec<MoonPhase> MOON_PHASE = getEnumCodecMaybeKeyed(MoonPhase.class, MoonPhase.values());
    public static final YamlCodec<EnchantmentRarity> ENCHANTMENT_RARITY = getEnumCodecMaybeKeyed(EnchantmentRarity.class, EnchantmentRarity.values());

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> YamlCodec<T> getEnumCodecMaybeKeyed(Class<T> type, T[] values) {
        if (values[0] instanceof Keyed) {
            return (YamlCodec<T>) new KeyedYamlCodec<>((Keyed[]) values, type.getSimpleName());
        }

        return (YamlCodec<T>) YamlCodec.enumOf((Class<Enum>) type);
    }

}
