package dev.by1337.yaml;

import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.InlineYamlCodecBuilder;
import dev.by1337.yaml.codec.RecordYamlCodecBuilder;
import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.codec.k2v.Key2ValueCodec;
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
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;

public class BukkitCodecs {

    private static final YamlCodec<Vector> VECTOR = InlineYamlCodecBuilder.inline(
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

    private static final YamlCodec<BoundingBox> BOUNDING_BOX = RecordYamlCodecBuilder.mapOf(
            BoundingBox::new,
            YamlCodec.DOUBLE.fieldOf("minX", BoundingBox::getMinX),
            YamlCodec.DOUBLE.fieldOf("minY", BoundingBox::getMinY),
            YamlCodec.DOUBLE.fieldOf("minZ", BoundingBox::getMinZ),
            YamlCodec.DOUBLE.fieldOf("maxX", BoundingBox::getMaxX),
            YamlCodec.DOUBLE.fieldOf("maxY", BoundingBox::getMaxY),
            YamlCodec.DOUBLE.fieldOf("maxZ", BoundingBox::getMaxZ)
    );
    private static final YamlCodec<NamespacedKey> NAMESPACED_KEY = YamlCodec.STRING.flatMap(s -> {
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

    private static final YamlCodec<StructureType> STRUCTURE_TYPE = YamlCodec.STRING
            .map(s -> StructureType.getStructureTypes().get(s.toLowerCase()), StructureType::getName);
    private static final YamlCodec<java.util.UUID> UUID = YamlCodec.STRING.map(java.util.UUID::fromString, java.util.UUID::toString);

    private static final YamlCodec<OfflinePlayer> OFFLINE_PLAYER = UUID.map(Bukkit::getOfflinePlayer, OfflinePlayer::getUniqueId);
    private static final YamlCodec<World> WORLD = YamlCodec.STRING.map(Bukkit::getWorld, World::getName);
    private static final YamlCodec<GameRule<?>> GAME_RULE = YamlCodec.STRING.map(GameRule::getByName, GameRule::getName);
    private static final YamlCodec<Color> COLOR = YamlCodec.STRING.flatMap(s -> {
        try {
            return DataResult.success(ColorUtil.fromHex(s));
        } catch (Throwable e) {
            return DataResult.error("Failed to decode color: Expected: '#rrggbb', but got " + s);
        }
    }, ColorUtil::toHex);

    private static final YamlCodec<EulerAngle> EULER_ANGLE = InlineYamlCodecBuilder.inline(
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
    //todo private static final Key2ValueCodec<PotionEffectType> POTION_EFFECT_TYPE = new LookupCodec<>(Arrays.stream(PotionEffectType.values()).iterator(), PotionEffectType::getName);
    private static final Key2ValueCodec<PotionType> POTION_TYPE = getEnumCodecMaybeKeyed(PotionType.class, PotionType.values());
    private static final Key2ValueCodec<ServicePriority> SERVICE_PRIORITY = YamlCodec.fromEnum(ServicePriority.class);
    private static final Key2ValueCodec<WorldType> WORLD_TYPE = getEnumCodecMaybeKeyed(WorldType.class, WorldType.values());

    private static final Key2ValueCodec<Advancement> ADVANCEMENT = new KeyedYamlCodec<>(Registry.ADVANCEMENT, Advancement.class.getSimpleName());
    private static final Key2ValueCodec<Art> ART = new KeyedYamlCodec<>(Registry.ART, Art.class.getSimpleName());
    private static final Key2ValueCodec<Attribute> ATTRIBUTE = new KeyedYamlCodec<>(Registry.ATTRIBUTE, Attribute.class.getSimpleName());
    private static final Key2ValueCodec<Biome> BIOME = new KeyedYamlCodec<>(Registry.BIOME, Biome.class.getSimpleName());
    private static final Key2ValueCodec<KeyedBossBar> BOSS_BAR = new KeyedYamlCodec<>(Registry.BOSS_BARS, KeyedBossBar.class.getSimpleName());
    private static final Key2ValueCodec<Enchantment> ENCHANTMENT = new KeyedYamlCodec<>(Registry.ENCHANTMENT, Enchantment.class.getSimpleName());
    private static final Key2ValueCodec<EntityType> ENTITY_TYPE = new KeyedYamlCodec<>(Registry.ENTITY_TYPE, EntityType.class.getSimpleName());
    private static final Key2ValueCodec<LootTables> LOOT_TABLE = new KeyedYamlCodec<>(Registry.LOOT_TABLES, LootTables.class.getSimpleName());
    private static final Key2ValueCodec<Material> MATERIAL = new KeyedYamlCodec<>(Registry.MATERIAL, Material.class.getSimpleName());
    private static final Key2ValueCodec<Material> BLOCKS = new KeyedYamlCodec<>(Registry.MATERIAL, Material.class.getSimpleName(), Material::isBlock);
    private static final Key2ValueCodec<Statistic> STATISTIC = new KeyedYamlCodec<>(Registry.STATISTIC, Statistic.class.getSimpleName());
    private static final Key2ValueCodec<Sound> SOUND = new KeyedYamlCodec<>(Registry.SOUNDS, Sound.class.getSimpleName());
    private static final Key2ValueCodec<Villager.Profession> VILLAGER_PROFESSION = new KeyedYamlCodec<>(Registry.VILLAGER_PROFESSION, Villager.Profession.class.getSimpleName());
    private static final Key2ValueCodec<Villager.Type> VILLAGER_TYPE = new KeyedYamlCodec<>(Registry.VILLAGER_TYPE, Villager.Type.class.getSimpleName());
    @SuppressWarnings({"rawtypes"})
    private static final Key2ValueCodec<MemoryKey> MEMORY_MODULE_TYPE = new KeyedYamlCodec<>(Registry.MEMORY_MODULE_TYPE, MemoryKey.class.getSimpleName());
    private static final Key2ValueCodec<Fluid> FLUID = new KeyedYamlCodec<>(Registry.FLUID, Fluid.class.getSimpleName());


    private static final Key2ValueCodec<EntityCategory> ENTITY_CATEGORY = getEnumCodecMaybeKeyed(EntityCategory.class, EntityCategory.values());
    private static final Key2ValueCodec<StructureRotation> STRUCTURE_ROTATION = getEnumCodecMaybeKeyed(StructureRotation.class, StructureRotation.values());
    private static final Key2ValueCodec<TreeType> TREE_TYPE = getEnumCodecMaybeKeyed(TreeType.class, TreeType.values());
    private static final Key2ValueCodec<PatternType> PATTERN_TYPE = getEnumCodecMaybeKeyed(PatternType.class, PatternType.values());
    private static final Key2ValueCodec<LazyMetadataValue.CacheStrategy> LAZY_METADATA_VALUE_CACHE_STRATEGY = getEnumCodecMaybeKeyed(LazyMetadataValue.CacheStrategy.class, LazyMetadataValue.CacheStrategy.values());
    private static final Key2ValueCodec<Rail.Shape> RAIL_SHAPE = getEnumCodecMaybeKeyed(Rail.Shape.class, Rail.Shape.values());
    private static final Key2ValueCodec<TreeSpecies> TREE_SPECIES = getEnumCodecMaybeKeyed(TreeSpecies.class, TreeSpecies.values());
    private static final Key2ValueCodec<RedstoneWire.Connection> REDSTONE_WIRE_CONNECTION = getEnumCodecMaybeKeyed(RedstoneWire.Connection.class, RedstoneWire.Connection.values());
    private static final Key2ValueCodec<EnchantmentTarget> ENCHANTMENT_TARGET = getEnumCodecMaybeKeyed(EnchantmentTarget.class, EnchantmentTarget.values());
    private static final Key2ValueCodec<BarColor> BAR_COLOR = getEnumCodecMaybeKeyed(BarColor.class, BarColor.values());
    private static final Key2ValueCodec<InventoryView.Property> INVENTORY_VIEW_PROPERTY = getEnumCodecMaybeKeyed(InventoryView.Property.class, InventoryView.Property.values());
    private static final Key2ValueCodec<EquipmentSlot> EQUIPMENT_SLOT = getEnumCodecMaybeKeyed(EquipmentSlot.class, EquipmentSlot.values());
    private static final Key2ValueCodec<Rabbit.Type> RABBIT_TYPE = getEnumCodecMaybeKeyed(Rabbit.Type.class, Rabbit.Type.values());
    private static final Key2ValueCodec<Axis> AXIS = getEnumCodecMaybeKeyed(Axis.class, Axis.values());
    private static final Key2ValueCodec<MushroomBlockTexture> MUSHROOM_BLOCK_TEXTURE = getEnumCodecMaybeKeyed(MushroomBlockTexture.class, MushroomBlockTexture.values());
    private static final Key2ValueCodec<PistonMoveReaction> PISTON_MOVE_REACTION = getEnumCodecMaybeKeyed(PistonMoveReaction.class, PistonMoveReaction.values());
    private static final Key2ValueCodec<MainHand> MAIN_HAND = getEnumCodecMaybeKeyed(MainHand.class, MainHand.values());
    private static final Key2ValueCodec<BanList.Type> BAN_LIST_TYPE = getEnumCodecMaybeKeyed(BanList.Type.class, BanList.Type.values());
    private static final Key2ValueCodec<PluginAwareness.Flags> PLUGIN_AWARENESS_FLAGS = getEnumCodecMaybeKeyed(PluginAwareness.Flags.class, PluginAwareness.Flags.values());
    private static final Key2ValueCodec<BarFlag> BAR_FLAG = getEnumCodecMaybeKeyed(BarFlag.class, BarFlag.values());
    private static final Key2ValueCodec<Team.Option> TEAM_OPTION = getEnumCodecMaybeKeyed(Team.Option.class, Team.Option.values());
    private static final Key2ValueCodec<RenderType> RENDER_TYPE = getEnumCodecMaybeKeyed(RenderType.class, RenderType.values());
    private static final Key2ValueCodec<Stairs.Shape> STAIRS_SHAPE = getEnumCodecMaybeKeyed(Stairs.Shape.class, Stairs.Shape.values());
    private static final Key2ValueCodec<Llama.Color> LLAMA_COLOR = getEnumCodecMaybeKeyed(Llama.Color.class, Llama.Color.values());
    private static final Key2ValueCodec<MapView.Scale> MAP_VIEW_SCALE = getEnumCodecMaybeKeyed(MapView.Scale.class, MapView.Scale.values());
    private static final Key2ValueCodec<TropicalFish.Pattern> TROPICAL_FISH_PATTERN = getEnumCodecMaybeKeyed(TropicalFish.Pattern.class, TropicalFish.Pattern.values());
    private static final Key2ValueCodec<StructureBlock.Mode> STRUCTURE_BLOCK_MODE = getEnumCodecMaybeKeyed(StructureBlock.Mode.class, StructureBlock.Mode.values());
    private static final Key2ValueCodec<InventoryType.SlotType> INVENTORY_TYPE_SLOT_TYPE = getEnumCodecMaybeKeyed(InventoryType.SlotType.class, InventoryType.SlotType.values());
    private static final Key2ValueCodec<SoundCategory> SOUND_CATEGORY = getEnumCodecMaybeKeyed(SoundCategory.class, SoundCategory.values());
    private static final Key2ValueCodec<FaceAttachable.AttachedFace> FACE_ATTACHABLE_ATTACHED_FACE = getEnumCodecMaybeKeyed(FaceAttachable.AttachedFace.class, FaceAttachable.AttachedFace.values());
    private static final Key2ValueCodec<Slab.Type> SLAB_TYPE = getEnumCodecMaybeKeyed(Slab.Type.class, Slab.Type.values());
    private static final Key2ValueCodec<HeightMap> HEIGHT_MAP = getEnumCodecMaybeKeyed(HeightMap.class, HeightMap.values());
    private static final Key2ValueCodec<Statistic.Type> STATISTIC_TYPE = getEnumCodecMaybeKeyed(Statistic.Type.class, Statistic.Type.values());
    private static final Key2ValueCodec<Panda.Gene> PANDA_GENE = getEnumCodecMaybeKeyed(Panda.Gene.class, Panda.Gene.values());
    private static final Key2ValueCodec<ArmorStand.LockType> ARMOR_STAND_LOCK_TYPE = getEnumCodecMaybeKeyed(ArmorStand.LockType.class, ArmorStand.LockType.values());
    private static final Key2ValueCodec<UsageMode> USAGE_MODE = getEnumCodecMaybeKeyed(UsageMode.class, UsageMode.values());
    private static final Key2ValueCodec<Pose> POSE = getEnumCodecMaybeKeyed(Pose.class, Pose.values());
    private static final Key2ValueCodec<Effect.Type> EFFECT_TYPE = getEnumCodecMaybeKeyed(Effect.Type.class, Effect.Type.values());
    private static final Key2ValueCodec<DyeColor> DYE_COLOR = getEnumCodecMaybeKeyed(DyeColor.class, DyeColor.values());
    private static final Key2ValueCodec<ItemFlag> ITEM_FLAG = getEnumCodecMaybeKeyed(ItemFlag.class, ItemFlag.values());
    private static final Key2ValueCodec<PortalType> PORTAL_TYPE = getEnumCodecMaybeKeyed(PortalType.class, PortalType.values());
    private static final Key2ValueCodec<EntityEffect> ENTITY_EFFECT = getEnumCodecMaybeKeyed(EntityEffect.class, EntityEffect.values());
    private static final Key2ValueCodec<Door.Hinge> DOOR_HINGE = getEnumCodecMaybeKeyed(Door.Hinge.class, Door.Hinge.values());
    private static final Key2ValueCodec<Bisected.Half> BISECTED_HALF = getEnumCodecMaybeKeyed(Bisected.Half.class, Bisected.Half.values());
    private static final Key2ValueCodec<Conversation.ConversationState> CONVERSATION_CONVERSATION_STATE = getEnumCodecMaybeKeyed(Conversation.ConversationState.class, Conversation.ConversationState.values());
    private static final Key2ValueCodec<NetherWartsState> NETHER_WARTS_STATE = getEnumCodecMaybeKeyed(NetherWartsState.class, NetherWartsState.values());
    private static final Key2ValueCodec<CropState> CROP_STATE = getEnumCodecMaybeKeyed(CropState.class, CropState.values());
    private static final Key2ValueCodec<Spellcaster.Spell> SPELLCASTER_SPELL = getEnumCodecMaybeKeyed(Spellcaster.Spell.class, Spellcaster.Spell.values());
    private static final Key2ValueCodec<World.Environment> WORLD_ENVIRONMENT = getEnumCodecMaybeKeyed(World.Environment.class, World.Environment.values());
    private static final Key2ValueCodec<BlockFace> BLOCK_FACE = getEnumCodecMaybeKeyed(BlockFace.class, BlockFace.values());
    private static final Key2ValueCodec<ExperienceOrb.SpawnReason> EXPERIENCE_ORB_SPAWN_REASON = getEnumCodecMaybeKeyed(ExperienceOrb.SpawnReason.class, ExperienceOrb.SpawnReason.values());
    private static final Key2ValueCodec<CoalType> COAL_TYPE = getEnumCodecMaybeKeyed(CoalType.class, CoalType.values());
    private static final Key2ValueCodec<EnderDragon.Phase> ENDER_DRAGON_PHASE = getEnumCodecMaybeKeyed(EnderDragon.Phase.class, EnderDragon.Phase.values());
    private static final Key2ValueCodec<DragonBattle.RespawnPhase> DRAGON_BATTLE_RESPAWN_PHASE = getEnumCodecMaybeKeyed(DragonBattle.RespawnPhase.class, DragonBattle.RespawnPhase.values());
    private static final Key2ValueCodec<AbstractArrow.PickupStatus> ABSTRACT_ARROW_PICKUP_STATUS = getEnumCodecMaybeKeyed(AbstractArrow.PickupStatus.class, AbstractArrow.PickupStatus.values());
    private static final Key2ValueCodec<Chest.Type> CHEST_TYPE = getEnumCodecMaybeKeyed(Chest.Type.class, Chest.Type.values());
    private static final Key2ValueCodec<Difficulty> DIFFICULTY = getEnumCodecMaybeKeyed(Difficulty.class, Difficulty.values());
    private static final Key2ValueCodec<TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = getEnumCodecMaybeKeyed(TechnicalPiston.Type.class, TechnicalPiston.Type.values());
    private static final Key2ValueCodec<Instrument> INSTRUMENT = getEnumCodecMaybeKeyed(Instrument.class, Instrument.values());
    private static final Key2ValueCodec<Bed.Part> BED_PART = getEnumCodecMaybeKeyed(Bed.Part.class, Bed.Part.values());
    private static final Key2ValueCodec<PermissionDefault> PERMISSION_DEFAULT = getEnumCodecMaybeKeyed(PermissionDefault.class, PermissionDefault.values());
    private static final Key2ValueCodec<GrassSpecies> GRASS_SPECIES = getEnumCodecMaybeKeyed(GrassSpecies.class, GrassSpecies.values());
    private static final Key2ValueCodec<Parrot.Variant> PARROT_VARIANT = getEnumCodecMaybeKeyed(Parrot.Variant.class, Parrot.Variant.values());
    private static final Key2ValueCodec<Rotation> ROTATION = getEnumCodecMaybeKeyed(Rotation.class, Rotation.values());
    private static final Key2ValueCodec<FluidCollisionMode> FLUID_COLLISION_MODE = getEnumCodecMaybeKeyed(FluidCollisionMode.class, FluidCollisionMode.values());
    private static final Key2ValueCodec<DisplaySlot> DISPLAY_SLOT = getEnumCodecMaybeKeyed(DisplaySlot.class, DisplaySlot.values());
    private static final Key2ValueCodec<ClickType> CLICK_TYPE = getEnumCodecMaybeKeyed(ClickType.class, ClickType.values());
    private static final Key2ValueCodec<Horse.Color> HORSE_COLOR = getEnumCodecMaybeKeyed(Horse.Color.class, Horse.Color.values());
    private static final Key2ValueCodec<Note.Tone> NOTE_TONE = getEnumCodecMaybeKeyed(Note.Tone.class, Note.Tone.values());
    private static final Key2ValueCodec<Action> BLOCK_CLICK_ACTION = getEnumCodecMaybeKeyed(Action.class, Action.values());
    private static final Key2ValueCodec<Mirror> MIRROR = getEnumCodecMaybeKeyed(Mirror.class, Mirror.values());
    private static final Key2ValueCodec<GameMode> GAME_MODE = getEnumCodecMaybeKeyed(GameMode.class, GameMode.values());
    private static final Key2ValueCodec<Effect> EFFECT = getEnumCodecMaybeKeyed(Effect.class, Effect.values());
    private static final Key2ValueCodec<Cat.Type> CAT_TYPE = getEnumCodecMaybeKeyed(Cat.Type.class, Cat.Type.values());
    private static final Key2ValueCodec<DragType> DRAG_TYPE = getEnumCodecMaybeKeyed(DragType.class, DragType.values());
    private static final Key2ValueCodec<Wall.Height> WALL_HEIGHT = getEnumCodecMaybeKeyed(Wall.Height.class, Wall.Height.values());
    private static final Key2ValueCodec<FishHook.HookState> FISH_HOOK_HOOK_STATE = getEnumCodecMaybeKeyed(FishHook.HookState.class, FishHook.HookState.values());
    private static final Key2ValueCodec<Fox.Type> FOX_TYPE = getEnumCodecMaybeKeyed(Fox.Type.class, Fox.Type.values());
    private static final Key2ValueCodec<Bell.Attachment> BELL_ATTACHMENT = getEnumCodecMaybeKeyed(Bell.Attachment.class, Bell.Attachment.values());
    private static final Key2ValueCodec<Jigsaw.Orientation> JIGSAW_ORIENTATION = getEnumCodecMaybeKeyed(Jigsaw.Orientation.class, Jigsaw.Orientation.values());
    private static final Key2ValueCodec<BarStyle> BAR_STYLE = getEnumCodecMaybeKeyed(BarStyle.class, BarStyle.values());
    private static final Key2ValueCodec<MapCursor.Type> MAP_CURSOR_TYPE = getEnumCodecMaybeKeyed(MapCursor.Type.class, MapCursor.Type.values());
    private static final Key2ValueCodec<Comparator.Mode> COMPARATOR_MODE = getEnumCodecMaybeKeyed(Comparator.Mode.class, Comparator.Mode.values());
    private static final Key2ValueCodec<Horse.Style> HORSE_STYLE = getEnumCodecMaybeKeyed(Horse.Style.class, Horse.Style.values());
    private static final Key2ValueCodec<WeatherType> WEATHER_TYPE = getEnumCodecMaybeKeyed(WeatherType.class, WeatherType.values());
    private static final Key2ValueCodec<FireworkEffect.Type> FIREWORK_EFFECT_TYPE = getEnumCodecMaybeKeyed(FireworkEffect.Type.class, FireworkEffect.Type.values());
    private static final Key2ValueCodec<BookMeta.Generation> BOOK_META_GENERATION = getEnumCodecMaybeKeyed(BookMeta.Generation.class, BookMeta.Generation.values());
    private static final Key2ValueCodec<InventoryAction> INVENTORY_ACTION = getEnumCodecMaybeKeyed(InventoryAction.class, InventoryAction.values());
    private static final Key2ValueCodec<InventoryType> INVENTORY_TYPE = getEnumCodecMaybeKeyed(InventoryType.class, InventoryType.values());
    private static final Key2ValueCodec<SandstoneType> SANDSTONE_TYPE = getEnumCodecMaybeKeyed(SandstoneType.class, SandstoneType.values());
    private static final Key2ValueCodec<Bamboo.Leaves> BAMBOO_LEAVES = getEnumCodecMaybeKeyed(Bamboo.Leaves.class, Bamboo.Leaves.values());
    //todo private static final Key2ValueCodec<Particle> PARTICLE = getEnumCodecMaybeKeyed(Particle.class, Particle.values());
    private static final Key2ValueCodec<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION = getEnumCodecMaybeKeyed(AttributeModifier.Operation.class, AttributeModifier.Operation.values());
    private static final Key2ValueCodec<Team.OptionStatus> TEAM_OPTION_STATUS = getEnumCodecMaybeKeyed(Team.OptionStatus.class, Team.OptionStatus.values());
    private static final Key2ValueCodec<ChatColor> CHAT_COLOR = getEnumCodecMaybeKeyed(ChatColor.class, ChatColor.values());
    private static final Key2ValueCodec<MushroomCow.Variant> MUSHROOM_COW_VARIANT = getEnumCodecMaybeKeyed(MushroomCow.Variant.class, MushroomCow.Variant.values());
    private static final Key2ValueCodec<Raid.RaidStatus> RAID_RAID_STATUS = getEnumCodecMaybeKeyed(Raid.RaidStatus.class, Raid.RaidStatus.values());


    private static final Key2ValueCodec<Datapack.Compatibility> DATAPACK_COMPATIBILITY = getEnumCodecMaybeKeyed(Datapack.Compatibility.class, Datapack.Compatibility.values());
    private static final Key2ValueCodec<ItemRarity> ITEM_RARITY = getEnumCodecMaybeKeyed(ItemRarity.class, ItemRarity.values());
    private static final Key2ValueCodec<MoonPhase> MOON_PHASE = getEnumCodecMaybeKeyed(MoonPhase.class, MoonPhase.values());
    private static final Key2ValueCodec<EnchantmentRarity> ENCHANTMENT_RARITY = getEnumCodecMaybeKeyed(EnchantmentRarity.class, EnchantmentRarity.values());

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Key2ValueCodec<T> getEnumCodecMaybeKeyed(Class<T> type, T[] values) {
        if (values[0] instanceof Keyed) {
            return (Key2ValueCodec<T>) new KeyedYamlCodec<>((Keyed[]) values, type.getSimpleName());
        }

        return (Key2ValueCodec<T>) YamlCodec.fromEnum((Class<Enum>) type);
    }

    public static YamlCodec<Vector> vector() {
        return VECTOR;
    }

    public static YamlCodec<BoundingBox> bounding_box() {
        return BOUNDING_BOX;
    }

    public static YamlCodec<NamespacedKey> namespaced_key() {
        return NAMESPACED_KEY;
    }

    public static YamlCodec<StructureType> structure_type() {
        return STRUCTURE_TYPE;
    }

    public static YamlCodec<UUID> uuid() {
        return UUID;
    }

    public static YamlCodec<OfflinePlayer> offline_player() {
        return OFFLINE_PLAYER;
    }

    public static YamlCodec<World> world() {
        return WORLD;
    }

    public static YamlCodec<GameRule<?>> game_rule() {
        return GAME_RULE;
    }

    public static YamlCodec<Color> color() {
        return COLOR;
    }

    public static YamlCodec<EulerAngle> euler_angle() {
        return EULER_ANGLE;
    }

    public static Key2ValueCodec<PotionType> potion_type() {
        return POTION_TYPE;
    }

    public static Key2ValueCodec<ServicePriority> service_priority() {
        return SERVICE_PRIORITY;
    }

    public static Key2ValueCodec<WorldType> world_type() {
        return WORLD_TYPE;
    }

    public static Key2ValueCodec<Advancement> advancement() {
        return ADVANCEMENT;
    }

    public static Key2ValueCodec<Art> art() {
        return ART;
    }

    public static Key2ValueCodec<Attribute> attribute() {
        return ATTRIBUTE;
    }

    public static Key2ValueCodec<Biome> biome() {
        return BIOME;
    }

    public static Key2ValueCodec<KeyedBossBar> boss_bar() {
        return BOSS_BAR;
    }

    public static Key2ValueCodec<Enchantment> enchantment() {
        return ENCHANTMENT;
    }

    public static Key2ValueCodec<EntityType> entity_type() {
        return ENTITY_TYPE;
    }

    public static Key2ValueCodec<LootTables> loot_table() {
        return LOOT_TABLE;
    }

    public static Key2ValueCodec<Material> material() {
        return MATERIAL;
    }

    public static Key2ValueCodec<Material> blocks() {
        return BLOCKS;
    }

    public static Key2ValueCodec<Statistic> statistic() {
        return STATISTIC;
    }

    public static Key2ValueCodec<Sound> sound() {
        return SOUND;
    }

    public static Key2ValueCodec<Villager.Profession> villager_profession() {
        return VILLAGER_PROFESSION;
    }

    public static Key2ValueCodec<Villager.Type> villager_type() {
        return VILLAGER_TYPE;
    }

    public static Key2ValueCodec<MemoryKey> memory_module_type() {
        return MEMORY_MODULE_TYPE;
    }

    public static Key2ValueCodec<Fluid> fluid() {
        return FLUID;
    }

    public static Key2ValueCodec<EntityCategory> entity_category() {
        return ENTITY_CATEGORY;
    }

    public static Key2ValueCodec<StructureRotation> structure_rotation() {
        return STRUCTURE_ROTATION;
    }

    public static Key2ValueCodec<TreeType> tree_type() {
        return TREE_TYPE;
    }

    public static Key2ValueCodec<PatternType> pattern_type() {
        return PATTERN_TYPE;
    }

    public static Key2ValueCodec<LazyMetadataValue.CacheStrategy> lazy_metadata_value_cache_strategy() {
        return LAZY_METADATA_VALUE_CACHE_STRATEGY;
    }

    public static Key2ValueCodec<Rail.Shape> rail_shape() {
        return RAIL_SHAPE;
    }

    public static Key2ValueCodec<TreeSpecies> tree_species() {
        return TREE_SPECIES;
    }

    public static Key2ValueCodec<RedstoneWire.Connection> redstone_wire_connection() {
        return REDSTONE_WIRE_CONNECTION;
    }

    public static Key2ValueCodec<EnchantmentTarget> enchantment_target() {
        return ENCHANTMENT_TARGET;
    }

    public static Key2ValueCodec<BarColor> bar_color() {
        return BAR_COLOR;
    }

    public static Key2ValueCodec<InventoryView.Property> inventory_view_property() {
        return INVENTORY_VIEW_PROPERTY;
    }

    public static Key2ValueCodec<EquipmentSlot> equipment_slot() {
        return EQUIPMENT_SLOT;
    }

    public static Key2ValueCodec<Rabbit.Type> rabbit_type() {
        return RABBIT_TYPE;
    }

    public static Key2ValueCodec<Axis> axis() {
        return AXIS;
    }

    public static Key2ValueCodec<MushroomBlockTexture> mushroom_block_texture() {
        return MUSHROOM_BLOCK_TEXTURE;
    }

    public static Key2ValueCodec<PistonMoveReaction> piston_move_reaction() {
        return PISTON_MOVE_REACTION;
    }

    public static Key2ValueCodec<MainHand> main_hand() {
        return MAIN_HAND;
    }

    public static Key2ValueCodec<BanList.Type> ban_list_type() {
        return BAN_LIST_TYPE;
    }

    public static Key2ValueCodec<PluginAwareness.Flags> plugin_awareness_flags() {
        return PLUGIN_AWARENESS_FLAGS;
    }

    public static Key2ValueCodec<BarFlag> bar_flag() {
        return BAR_FLAG;
    }

    public static Key2ValueCodec<Team.Option> team_option() {
        return TEAM_OPTION;
    }

    public static Key2ValueCodec<RenderType> render_type() {
        return RENDER_TYPE;
    }

    public static Key2ValueCodec<Stairs.Shape> stairs_shape() {
        return STAIRS_SHAPE;
    }

    public static Key2ValueCodec<Llama.Color> llama_color() {
        return LLAMA_COLOR;
    }

    public static Key2ValueCodec<MapView.Scale> map_view_scale() {
        return MAP_VIEW_SCALE;
    }

    public static Key2ValueCodec<TropicalFish.Pattern> tropical_fish_pattern() {
        return TROPICAL_FISH_PATTERN;
    }

    public static Key2ValueCodec<StructureBlock.Mode> structure_block_mode() {
        return STRUCTURE_BLOCK_MODE;
    }

    public static Key2ValueCodec<InventoryType.SlotType> inventory_type_slot_type() {
        return INVENTORY_TYPE_SLOT_TYPE;
    }

    public static Key2ValueCodec<SoundCategory> sound_category() {
        return SOUND_CATEGORY;
    }

    public static Key2ValueCodec<FaceAttachable.AttachedFace> face_attachable_attached_face() {
        return FACE_ATTACHABLE_ATTACHED_FACE;
    }

    public static Key2ValueCodec<Slab.Type> slab_type() {
        return SLAB_TYPE;
    }

    public static Key2ValueCodec<HeightMap> height_map() {
        return HEIGHT_MAP;
    }

    public static Key2ValueCodec<Statistic.Type> statistic_type() {
        return STATISTIC_TYPE;
    }

    public static Key2ValueCodec<Panda.Gene> panda_gene() {
        return PANDA_GENE;
    }

    public static Key2ValueCodec<ArmorStand.LockType> armor_stand_lock_type() {
        return ARMOR_STAND_LOCK_TYPE;
    }

    public static Key2ValueCodec<UsageMode> usage_mode() {
        return USAGE_MODE;
    }

    public static Key2ValueCodec<Pose> pose() {
        return POSE;
    }

    public static Key2ValueCodec<Effect.Type> effect_type() {
        return EFFECT_TYPE;
    }

    public static Key2ValueCodec<DyeColor> dye_color() {
        return DYE_COLOR;
    }

    public static Key2ValueCodec<ItemFlag> item_flag() {
        return ITEM_FLAG;
    }

    public static Key2ValueCodec<PortalType> portal_type() {
        return PORTAL_TYPE;
    }

    public static Key2ValueCodec<EntityEffect> entity_effect() {
        return ENTITY_EFFECT;
    }

    public static Key2ValueCodec<Door.Hinge> door_hinge() {
        return DOOR_HINGE;
    }

    public static Key2ValueCodec<Bisected.Half> bisected_half() {
        return BISECTED_HALF;
    }

    public static Key2ValueCodec<Conversation.ConversationState> conversation_conversation_state() {
        return CONVERSATION_CONVERSATION_STATE;
    }

    public static Key2ValueCodec<NetherWartsState> nether_warts_state() {
        return NETHER_WARTS_STATE;
    }

    public static Key2ValueCodec<CropState> crop_state() {
        return CROP_STATE;
    }

    public static Key2ValueCodec<Spellcaster.Spell> spellcaster_spell() {
        return SPELLCASTER_SPELL;
    }

    public static Key2ValueCodec<World.Environment> world_environment() {
        return WORLD_ENVIRONMENT;
    }

    public static Key2ValueCodec<BlockFace> block_face() {
        return BLOCK_FACE;
    }

    public static Key2ValueCodec<ExperienceOrb.SpawnReason> experience_orb_spawn_reason() {
        return EXPERIENCE_ORB_SPAWN_REASON;
    }

    public static Key2ValueCodec<CoalType> coal_type() {
        return COAL_TYPE;
    }

    public static Key2ValueCodec<EnderDragon.Phase> ender_dragon_phase() {
        return ENDER_DRAGON_PHASE;
    }

    public static Key2ValueCodec<DragonBattle.RespawnPhase> dragon_battle_respawn_phase() {
        return DRAGON_BATTLE_RESPAWN_PHASE;
    }

    public static Key2ValueCodec<AbstractArrow.PickupStatus> abstract_arrow_pickup_status() {
        return ABSTRACT_ARROW_PICKUP_STATUS;
    }

    public static Key2ValueCodec<Chest.Type> chest_type() {
        return CHEST_TYPE;
    }

    public static Key2ValueCodec<Difficulty> difficulty() {
        return DIFFICULTY;
    }

    public static Key2ValueCodec<TechnicalPiston.Type> technical_piston_type() {
        return TECHNICAL_PISTON_TYPE;
    }

    public static Key2ValueCodec<Instrument> instrument() {
        return INSTRUMENT;
    }

    public static Key2ValueCodec<Bed.Part> bed_part() {
        return BED_PART;
    }

    public static Key2ValueCodec<PermissionDefault> permission_default() {
        return PERMISSION_DEFAULT;
    }

    public static Key2ValueCodec<GrassSpecies> grass_species() {
        return GRASS_SPECIES;
    }

    public static Key2ValueCodec<Parrot.Variant> parrot_variant() {
        return PARROT_VARIANT;
    }

    public static Key2ValueCodec<Rotation> rotation() {
        return ROTATION;
    }

    public static Key2ValueCodec<FluidCollisionMode> fluid_collision_mode() {
        return FLUID_COLLISION_MODE;
    }

    public static Key2ValueCodec<DisplaySlot> display_slot() {
        return DISPLAY_SLOT;
    }

    public static Key2ValueCodec<ClickType> click_type() {
        return CLICK_TYPE;
    }

    public static Key2ValueCodec<Horse.Color> horse_color() {
        return HORSE_COLOR;
    }

    public static Key2ValueCodec<Note.Tone> note_tone() {
        return NOTE_TONE;
    }

    public static Key2ValueCodec<Action> block_click_action() {
        return BLOCK_CLICK_ACTION;
    }

    public static Key2ValueCodec<Mirror> mirror() {
        return MIRROR;
    }

    public static Key2ValueCodec<GameMode> game_mode() {
        return GAME_MODE;
    }

    public static Key2ValueCodec<Effect> effect() {
        return EFFECT;
    }

    public static Key2ValueCodec<Cat.Type> cat_type() {
        return CAT_TYPE;
    }

    public static Key2ValueCodec<DragType> drag_type() {
        return DRAG_TYPE;
    }

    public static Key2ValueCodec<Wall.Height> wall_height() {
        return WALL_HEIGHT;
    }

    public static Key2ValueCodec<FishHook.HookState> fish_hook_hook_state() {
        return FISH_HOOK_HOOK_STATE;
    }

    public static Key2ValueCodec<Fox.Type> fox_type() {
        return FOX_TYPE;
    }

    public static Key2ValueCodec<Bell.Attachment> bell_attachment() {
        return BELL_ATTACHMENT;
    }

    public static Key2ValueCodec<Jigsaw.Orientation> jigsaw_orientation() {
        return JIGSAW_ORIENTATION;
    }

    public static Key2ValueCodec<BarStyle> bar_style() {
        return BAR_STYLE;
    }

    public static Key2ValueCodec<MapCursor.Type> map_cursor_type() {
        return MAP_CURSOR_TYPE;
    }

    public static Key2ValueCodec<Comparator.Mode> comparator_mode() {
        return COMPARATOR_MODE;
    }

    public static Key2ValueCodec<Horse.Style> horse_style() {
        return HORSE_STYLE;
    }

    public static Key2ValueCodec<WeatherType> weather_type() {
        return WEATHER_TYPE;
    }

    public static Key2ValueCodec<FireworkEffect.Type> firework_effect_type() {
        return FIREWORK_EFFECT_TYPE;
    }

    public static Key2ValueCodec<BookMeta.Generation> book_meta_generation() {
        return BOOK_META_GENERATION;
    }

    public static Key2ValueCodec<InventoryAction> inventory_action() {
        return INVENTORY_ACTION;
    }

    public static Key2ValueCodec<InventoryType> inventory_type() {
        return INVENTORY_TYPE;
    }

    public static Key2ValueCodec<SandstoneType> sandstone_type() {
        return SANDSTONE_TYPE;
    }

    public static Key2ValueCodec<Bamboo.Leaves> bamboo_leaves() {
        return BAMBOO_LEAVES;
    }

    public static Key2ValueCodec<AttributeModifier.Operation> attribute_modifier_operation() {
        return ATTRIBUTE_MODIFIER_OPERATION;
    }

    public static Key2ValueCodec<Team.OptionStatus> team_option_status() {
        return TEAM_OPTION_STATUS;
    }

    public static Key2ValueCodec<ChatColor> chat_color() {
        return CHAT_COLOR;
    }

    public static Key2ValueCodec<MushroomCow.Variant> mushroom_cow_variant() {
        return MUSHROOM_COW_VARIANT;
    }

    public static Key2ValueCodec<Raid.RaidStatus> raid_raid_status() {
        return RAID_RAID_STATUS;
    }

    public static Key2ValueCodec<Datapack.Compatibility> datapack_compatibility() {
        return DATAPACK_COMPATIBILITY;
    }

    public static Key2ValueCodec<ItemRarity> item_rarity() {
        return ITEM_RARITY;
    }

    public static Key2ValueCodec<MoonPhase> moon_phase() {
        return MOON_PHASE;
    }

    public static Key2ValueCodec<EnchantmentRarity> enchantment_rarity() {
        return ENCHANTMENT_RARITY;
    }
}
