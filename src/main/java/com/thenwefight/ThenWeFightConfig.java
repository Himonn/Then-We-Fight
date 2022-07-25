package com.thenwefight;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("thenwefight")
public interface ThenWeFightConfig extends Config
{
	@ConfigItem(
			name = "Points",
			description = "Points to write in overlayus",
			position = 1,
			keyName = "points"
	)
	default String points() { return "Points: 0"; }

	// OVERLAY SETTINGS

	@ConfigSection(
			name = "Overlay Settings",
			description = "Overlay",
			position = 3,
			closedByDefault = true
	)
	String overlay = "Overlay Settings";


	@Alpha
	@ConfigItem(
			keyName = "gameOverlayColour",
			name = "Game Overlay Colour",
			description = "Colour of overlays that are drawn within the game frame",
			position = 1,
			section = overlay
	)
	default Color gameOverlayColour()
	{
		return Color.GRAY;
	}

	@Alpha
	@ConfigItem(
			keyName = "uiOverlayColour",
			name = "UI Overlay Colour",
			description = "Colour of overlays that are drawn on the UI",
			position = 2,
			section = overlay
	)
	default Color uiOverlayColour()
	{
		return Color.GRAY;
	}

	@Range(
			min = 0,
			max = 255
	)
	@ConfigItem(
			position = 3,
			keyName = "opacity",
			name = "Opacity",
			description = "Overlay Opacity, ranging from 0-255 as an alpha value",
			section = overlay
	)
	default int opacity() { return 100; }

	@ConfigItem(
			name = "Debug Mode",
			description = "Debug overlay",
			position = 7,
			keyName = "debug",
			section = overlay
	)
	default boolean debug() { return false; }

	@ConfigItem(
			name = "Overlay X Offset",
			description = "Horizontal pixels to offset overlay rendering by",
			position = 10,
			keyName = "overlayX",
			section = overlay
	)
	default int overlayX() { return 100; }

	@ConfigItem(
			name = "Overlay Y Offset",
			description = "Vertical pixels to offset overlay rendering by",
			position = 20,
			keyName = "overlayY",
			section = overlay
	)
	default int overlayY() { return 100; }

	@ConfigItem(
			name = "Unlock Image Width",
			description = "Width of each unlock image",
			position = 30,
			keyName = "unlockImageWidth",
			section = overlay
	)
	default int unlockImageWidth() { return 36; }

	@ConfigItem(
			name = "Unlock Image Height",
			description = "Height of each unlock image",
			position = 40,
			keyName = "unlockImageHeight",
			section = overlay
	)
	default int unlockImageHeight() { return 32; }

	//FEATURE SETTINGS

	@ConfigSection(
			name = "Feature Settings",
			description = "Feature",
			position = 5,
			closedByDefault = true
	)
	String feature = "Feature Settings";

	@ConfigItem(
			name = "Unlock Custom NPCs",
			description = "Enable Greying out NPCs by name if they are not unlocked",
			position = 5,
			keyName = "unlockNpcs",
			section = feature
	)
	default boolean unlockNpcs() { return true; }

	@ConfigItem(
			name = "Unlocked NPCS",
			description = "NPC names that are unlocked, separated by commas, not case sensitive",
			position = 10,
			keyName = "unlockedNpcs",
			section = feature
	)
	default String unlockedNpcs() { return "man,woman"; }

	@ConfigItem(
			name = "Unlock Items",
			description = "Enable Greying out Items by ID if they are not unlocked",
			position = 15,
			keyName = "unlockItems",
			section = feature
	)
	default boolean unlockItems() { return true; }

	@ConfigItem(
			name = "Unlocked Items",
			description = "Item IDs that are unlocked, separated by commas, not case sensitive",
			position = 20,
			keyName = "unlockedItems",
			section = feature
	)
	default String unlockedItems() { return "0,0"; }

	@ConfigItem(
			name = "Lock Custom Game Objects",
			description = "Enable Greying out Game Objects by name if they are locked",
			position = 35,
			keyName = "lockGameObjects",
			section = feature
	)
	default boolean lockGameObjects() { return true; }

	@ConfigItem(
			name = "Locked Game Objects",
			description = "Game Object names that are locked, separated by commas, not case sensitive",
			position = 40,
			keyName = "lockedGameObjects",
			section = feature
	)
	default String lockedGameObjects() { return "stairs,door"; }

	@ConfigItem(
			name = "Lock Custom Widgets",
			description = "Enable Greying out Widgets by ID if they are locked",
			position = 42,
			keyName = "lockWidgets",
			section = feature
	)
	default boolean lockWidgets() { return true; }

	@ConfigItem(
			name = "Locked Widgets",
			description = "Widget IDs that are locked, separated by commas, not case sensitive",
			position = 43,
			keyName = "lockedWidgets",
			section = feature
	)
	default String lockedWidgets() { return "0,0"; }

	@ConfigItem(
			name = "Lock Coins",
			description = "Enable Greying out coins",
			position = 45,
			keyName = "lockCoins",
			section = feature
	)
	default boolean lockCoins() { return true; }

	@ConfigItem(
			name = "Lock Attacking",
			description = "Enable Greying out Attacking",
			position = 50,
			keyName = "lockAttacking",
			section = feature
	)
	default boolean lockAttacking() { return true; }

	@ConfigItem(
			name = "Lock Drinking",
			description = "Enable Greying out Drinking",
			position = 55,
			keyName = "lockDrinking",
			section = feature
	)
	default boolean lockDrinking() { return true; }

	@ConfigItem(
			name = "Lock Eating",
			description = "Enable Greying out Eating",
			position = 60,
			keyName = "lockEating",
			section = feature
	)
	default boolean lockEating() { return true; }

	@ConfigItem(
			name = "Lock Teles",
			description = "Enable Greying out Teles",
			position = 65,
			keyName = "lockTeles",
			section = feature
	)
	default boolean lockTeles() { return true; }

	@ConfigItem(
			name = "Lock Banks",
			description = "Enable Greying out banks and bankers",
			position = 70,
			keyName = "lockBanks",
			section = feature
	)
	default boolean lockBanks() { return true; }

	@ConfigItem(
			name = "Lock Underground",
			description = "Enable Greying out trapdoors",
			position = 75,
			keyName = "lockUnderground",
			section = feature
	)
	default boolean lockUnderground() { return true; }

	@ConfigItem(
			name = "Lock Run Energy",
			description = "Enable Greying out run energy orb",
			position = 115,
			keyName = "lockRunEnergy",
			section = feature
	)
	default boolean lockRunEnergy() { return true; }

//	@ConfigItem(
//			name = "Lock Prayers",
//			description = "Enable Greying out prayer orb and prayer book",
//			position = 120,
//			keyName = "lockPrayers",
//			section = feature
//	)
//	default boolean lockPrayers() { return true; }

	@ConfigItem(
			name = "Lock Special Attack",
			description = "Enable Greying out spec orb and spec bar",
			position = 125,
			keyName = "lockSpec",
			section = feature
	)
	default boolean lockSpec() { return true; }

//	@ConfigItem(
//			name = "Lock HP Orb",
//			description = "Enable Greying out HP Orb",
//			position = 130,
//			keyName = "lockHpOrb",
//			section = feature
//	)
//	default boolean lockHpOrb() { return true; }

	@ConfigItem(
			name = "Lock Emotes",
			description = "Enable Greying out Emotes Orb",
			position = 135,
			keyName = "lockEmotes",
			section = feature
	)
	default boolean lockEmotes() { return true; }

	@ConfigItem(
			name = "Lock Doors",
			description = "Enable Greying out Doors",
			position = 140,
			keyName = "lockDoors",
			section = feature
	)
	default boolean lockDoors() { return true; }

	@ConfigItem(
			name = "Lock Stairs",
			description = "Enable Greying out Stairs",
			position = 143,
			keyName = "lockStairs",
			section = feature
	)
	default boolean lockStairs() { return true; }

	@ConfigItem(
			name = "Lock World Hopping",
			description = "Enable Greying out World Hopping",
			position = 145,
			keyName = "lockHopping",
			section = feature
	)
	default boolean lockHopping() { return true; }

	// UNLOCK SKILLS TAB

	@ConfigSection(
			name = "Unlock Skills",
			description = "Unlock Skills",
			position = 10,
			closedByDefault = true
	)
	String unlockSkills = "Unlock Skills";

	@ConfigItem(
			name = "Lock Attack",
			description = "Enable Greying out attack skill",
			position = 10,
			keyName = "lockAttack",
			section = unlockSkills
	)
	default boolean lockAttack() { return true; }

	@ConfigItem(
			name = "Lock Strength",
			description = "Enable Greying out strength skill",
			position = 20,
			keyName = "lockStrength",
			section = unlockSkills
	)
	default boolean lockStrength() { return true; }

	@ConfigItem(
			name = "Lock Defence",
			description = "Enable Greying out defence skill",
			position = 30,
			keyName = "lockDefence",
			section = unlockSkills
	)
	default boolean lockDefence() { return true; }

	@ConfigItem(
			name = "Lock Range",
			description = "Enable Greying out range skill",
			position = 40,
			keyName = "lockRange",
			section = unlockSkills
	)
	default boolean lockRange() { return true; }

	@ConfigItem(
			name = "Lock Prayer",
			description = "Enable Greying out prayer skill",
			position = 50,
			keyName = "lockPrayer",
			section = unlockSkills
	)
	default boolean lockPrayer() { return true; }

	@ConfigItem(
			name = "Lock Magic",
			description = "Enable Greying out magic skill",
			position = 60,
			keyName = "lockMagic",
			section = unlockSkills
	)
	default boolean lockMagic() { return true; }

	@ConfigItem(
			name = "Lock Runecrafting",
			description = "Enable Greying out runecrafting skill",
			position = 70,
			keyName = "lockRunecrafting",
			section = unlockSkills
	)
	default boolean lockRunecrafting() { return true; }

	@ConfigItem(
			name = "Lock Construction",
			description = "Enable Greying out construction skill",
			position = 80,
			keyName = "lockConstruction",
			section = unlockSkills
	)
	default boolean lockConstruction() { return true; }

	@ConfigItem(
			name = "Lock Hitpoints",
			description = "Enable Greying out hitpoints skill",
			position = 90,
			keyName = "lockHitpoints",
			section = unlockSkills
	)
	default boolean lockHitpoints() { return true; }

	@ConfigItem(
			name = "Lock Agility",
			description = "Enable Greying out agility skill",
			position = 100,
			keyName = "lockAgility",
			section = unlockSkills
	)
	default boolean lockAgility() { return true; }

	@ConfigItem(
			name = "Lock Herblore",
			description = "Enable Greying out herblore skill",
			position = 110,
			keyName = "lockHerblore",
			section = unlockSkills
	)
	default boolean lockHerblore() { return true; }

	@ConfigItem(
			name = "Lock Thieving",
			description = "Enable Greying out thieving skill",
			position = 120,
			keyName = "lockThieving",
			section = unlockSkills
	)
	default boolean lockThieving() { return true; }

	@ConfigItem(
			name = "Lock Crafting",
			description = "Enable Greying out crafting skill",
			position = 130,
			keyName = "lockCrafting",
			section = unlockSkills
	)
	default boolean lockCrafting() { return true; }

	@ConfigItem(
			name = "Lock Fletching",
			description = "Enable Greying out fletching skill",
			position = 140,
			keyName = "lockFletching",
			section = unlockSkills
	)
	default boolean lockFletching() { return true; }

	@ConfigItem(
			name = "Lock Slayer",
			description = "Enable Greying out slayer skill",
			position = 150,
			keyName = "lockSlayer",
			section = unlockSkills
	)
	default boolean lockSlayer() { return true; }

	@ConfigItem(
			name = "Lock Hunter",
			description = "Enable Greying out hunter skill",
			position = 160,
			keyName = "lockHunter",
			section = unlockSkills
	)
	default boolean lockHunter() { return true; }

	@ConfigItem(
			name = "Lock Mining",
			description = "Enable Greying out mining skill",
			position = 170,
			keyName = "lockMining",
			section = unlockSkills
	)
	default boolean lockMining() { return true; }

	@ConfigItem(
			name = "Lock Smithing",
			description = "Enable Greying out smithing skill",
			position = 180,
			keyName = "lockSmithing",
			section = unlockSkills
	)
	default boolean lockSmithing() { return true; }

	@ConfigItem(
			name = "Lock Fishing",
			description = "Enable Greying out fishing skill",
			position = 190,
			keyName = "lockFishing",
			section = unlockSkills
	)
	default boolean lockFishing() { return true; }

	@ConfigItem(
			name = "Lock Cooking",
			description = "Enable Greying out cooking skill",
			position = 200,
			keyName = "lockCooking",
			section = unlockSkills
	)
	default boolean lockCooking() { return true; }

	@ConfigItem(
			name = "Lock Firemaking",
			description = "Enable Greying out firemaking skill",
			position = 210,
			keyName = "lockFiremaking",
			section = unlockSkills
	)
	default boolean lockFiremaking() { return true; }

	@ConfigItem(
			name = "Lock Woodcutting",
			description = "Enable Greying out woodcutting skill",
			position = 220,
			keyName = "lockWoodcutting",
			section = unlockSkills
	)
	default boolean lockWoodcutting() { return true; }

	@ConfigItem(
			name = "Lock Farming",
			description = "Enable Greying out farming skill",
			position = 230,
			keyName = "lockFarming",
			section = unlockSkills
	)
	default boolean lockFarming() { return true; }

	// PRAYER SETTINGS

	@ConfigSection(
			name = "Unlock Prayers",
			description = "Prayer Settings",
			position = 17,
			closedByDefault = true
	)
	String prayers = "Prayers";

	@ConfigItem(
			name = "Lock Thick Skin",
			description = "Enable Greying out Thick Skin Prayer",
			position = 10,
			keyName = "lockThickSkin",
			section = prayers
	)
	default boolean lockThickSkin() { return true; }

	@ConfigItem(
			name = "Lock Burst of Stregth",
			description = "Enable Greying out Burst of Stregth Prayer",
			position = 20,
			keyName = "lockBurstOfStrength",
			section = prayers
	)
	default boolean lockBurstOfStrength() { return true; }

	@ConfigItem(
			name = "Lock Clarity of Thought",
			description = "Enable Greying out Clarity of Thought Prayer",
			position = 30,
			keyName = "lockClarityOfThought",
			section = prayers
	)
	default boolean lockClarityOfThought() { return true; }

	@ConfigItem(
			name = "Lock Sharp Eye",
			description = "Enable Greying out Sharp Eye Prayer",
			position = 40,
			keyName = "lockSharpEye",
			section = prayers
	)
	default boolean lockSharpEye() { return true; }

	@ConfigItem(
			name = "Lock Mystic Will",
			description = "Enable Greying out Mystic Will Prayer",
			position = 50,
			keyName = "lockMysticWill",
			section = prayers
	)
	default boolean lockMysticWill() { return true; }

	@ConfigItem(
			name = "Lock Rock Skin",
			description = "Enable Greying out Rock Skin Prayer",
			position = 60,
			keyName = "lockRockSkin",
			section = prayers
	)
	default boolean lockRockSkin() { return true; }

	@ConfigItem(
			name = "Lock Superhuman Strength",
			description = "Enable Greying out Superhuman Strength Prayer",
			position = 70,
			keyName = "lockSuperhumanStrength",
			section = prayers
	)
	default boolean lockSuperhumanStrength() { return true; }

	@ConfigItem(
			name = "Lock Improved Reflexes",
			description = "Enable Greying out Improved Reflexes Prayer",
			position = 80,
			keyName = "lockImprovedReflexes",
			section = prayers
	)
	default boolean lockImprovedReflexes() { return true; }

	@ConfigItem(
			name = "Lock Rapid Restore",
			description = "Enable Greying out Rapid Restore Prayer",
			position = 90,
			keyName = "lockRapidRestore",
			section = prayers
	)
	default boolean lockRapidRestore() { return true; }

	@ConfigItem(
			name = "Lock Rapid Heal",
			description = "Enable Greying out Rapid Heal Prayer",
			position = 100,
			keyName = "lockRapidHeal",
			section = prayers
	)
	default boolean lockRapidHeal() { return true; }

	@ConfigItem(
			name = "Lock Protect Item",
			description = "Enable Greying out Protect Item Prayer",
			position = 110,
			keyName = "lockProtectItem",
			section = prayers
	)
	default boolean lockProtectItem() { return true; }

	@ConfigItem(
			name = "Lock Hawk Eye",
			description = "Enable Greying out Hawk Eye Prayer",
			position = 120,
			keyName = "lockHawkEye",
			section = prayers
	)
	default boolean lockHawkEye() { return true; }

	@ConfigItem(
			name = "Lock Mystic Lore",
			description = "Enable Greying out Mystic Lore Prayer",
			position = 130,
			keyName = "lockMysticLore",
			section = prayers
	)
	default boolean lockMysticLore() { return true; }

	@ConfigItem(
			name = "Lock Steel Skin",
			description = "Enable Greying out Steel Skin Prayer",
			position = 140,
			keyName = "lockSteelSkin",
			section = prayers
	)
	default boolean lockSteelSkin() { return true; }

	@ConfigItem(
			name = "Lock Ultimate Strength",
			description = "Enable Greying out Ultimate Strength Prayer",
			position = 150,
			keyName = "lockUltimateStrength",
			section = prayers
	)
	default boolean lockUltimateStrength() { return true; }

	@ConfigItem(
			name = "Lock Incredible Reflexes",
			description = "Enable Greying out Incredible Reflexes Prayer",
			position = 160,
			keyName = "lockIncredibleReflexes",
			section = prayers
	)
	default boolean lockIncredibleReflexes() { return true; }

	@ConfigItem(
			name = "Lock Protect From Magic",
			description = "Enable Greying out Protect From Magic Prayer",
			position = 170,
			keyName = "lockProtectFromMagic",
			section = prayers
	)
	default boolean lockProtectFromMagic() { return true; }

	@ConfigItem(
			name = "Lock Protect From Missiles",
			description = "Enable Greying out Protect From Missiles Prayer",
			position = 180,
			keyName = "lockProtectFromMissiles",
			section = prayers
	)
	default boolean lockProtectFromMissiles() { return true; }

	@ConfigItem(
			name = "Lock Protect From Melee",
			description = "Enable Greying out Protect From Melee Prayer",
			position = 190,
			keyName = "lockProtectFromMelee",
			section = prayers
	)
	default boolean lockProtectFromMelee() { return true; }

	@ConfigItem(
			name = "Lock Eagle Eye",
			description = "Enable Greying out Eagle Eye Prayer",
			position = 200,
			keyName = "lockEagleEye",
			section = prayers
	)
	default boolean lockEagleEye() { return true; }

	@ConfigItem(
			name = "Lock Mystic Might",
			description = "Enable Greying out Mystic Might Prayer",
			position = 210,
			keyName = "lockMysticMight",
			section = prayers
	)
	default boolean lockMysticMight() { return true; }

	@ConfigItem(
			name = "Lock Retribution",
			description = "Enable Greying out Retribution Prayer",
			position = 220,
			keyName = "lockRetribution",
			section = prayers
	)
	default boolean lockRetribution() { return true; }

	@ConfigItem(
			name = "Lock Redemption",
			description = "Enable Greying out Redemption Prayer",
			position = 230,
			keyName = "lockRedemption",
			section = prayers
	)
	default boolean lockRedemption() { return true; }

	@ConfigItem(
			name = "Lock Smite",
			description = "Enable Greying out Smite Prayer",
			position = 240,
			keyName = "lockSmite",
			section = prayers
	)
	default boolean lockSmite() { return true; }

	@ConfigItem(
			name = "Lock Preserve",
			description = "Enable Greying out Preserve Prayer",
			position = 250,
			keyName = "lockPreserve",
			section = prayers
	)
	default boolean lockPreserve() { return true; }

	@ConfigItem(
			name = "Lock Chivalry",
			description = "Enable Greying out Chivalry Prayer",
			position = 260,
			keyName = "lockChivalry",
			section = prayers
	)
	default boolean lockChivalry() { return true; }

	@ConfigItem(
			name = "Lock Piety",
			description = "Enable Greying out Piety Prayer",
			position = 270,
			keyName = "lockPiety",
			section = prayers
	)
	default boolean lockPiety() { return true; }

	@ConfigItem(
			name = "Lock Rigour",
			description = "Enable Greying out Rigour Prayer",
			position = 280,
			keyName = "lockRigour",
			section = prayers
	)
	default boolean lockRigour() { return true; }

	@ConfigItem(
			name = "Lock Augury",
			description = "Enable Greying out Augury Prayer",
			position = 290,
			keyName = "lockAugury",
			section = prayers
	)
	default boolean lockAugury() { return true; }

	// TASK SETTINGS

	@ConfigSection(
			name = "Tasks",
			description = "Tasks",
			position = 18,
			closedByDefault = true
	)
	String tasks = "Tasks";

	@ConfigItem(
			name = "Task Scroll",
			description = "How many tasks into the list to start displaying them",
			position = 5,
			keyName = "taskScroll",
			section = tasks
	)
	default int taskScroll() { return 0; }

	@ConfigItem(
			name = "Task List",
			description = "Task Tick List to display in the form [Y/N,Points,Name]",
			position = 10,
			keyName = "taskList",
			section = tasks
	)
	default String taskList() { return "Y,20, Thing"; }

	// UNLOCK SETTINGS

	@ConfigSection(
			name = "Unlocks",
			description = "Unlocks",
			position = 19,
			closedByDefault = true
	)
	String unlocks = "Unlocks";

	@ConfigItem(
			name = "Unlock Scroll",
			description = "How many unlocks into the list to start displaying them",
			position = 5,
			keyName = "unlockScroll",
			section = unlocks
	)
	default int unlockScroll() { return 0; }

	@ConfigItem(
			name = "Unlock List",
			description = "Unlock List to display in the form [Y/N,[FILENAME / Item ID],Title,Price]",
			position = 10,
			keyName = "unlockList",
			section = unlocks
	)
	default String unlockList() { return "Y,20, Thing"; }

	// UNLOCK SLOT 1 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 1",
			description = "Unlock Slot 1",
			position = 20,
			closedByDefault = true
	)
	String unlock1 = "Unlock Slot 1";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot1Unlocked",
			section = unlock1
	)
	default boolean slot1Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 1 (Custom image needs to be in .runelite folder and called 1.png)",
			position = 15,
			keyName = "slot1UseItem",
			section = unlock1
	)
	default boolean slot1UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot1Id",
			section = unlock1
	)
	default int slot1Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot1Title",
			section = unlock1
	)
	default String slot1Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot1Price",
			section = unlock1
	)
	default String slot1Price() { return "10 Points"; }

	// UNLOCK SLOT 2 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 2",
			description = "Unlock Slot 2",
			position = 30,
			closedByDefault = true
	)
	String unlock2 = "Unlock Slot 2";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot2Unlocked",
			section = unlock2
	)
	default boolean slot2Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 2 (Custom image needs to be in .runelite folder and called 2.png)",
			position = 15,
			keyName = "slot2UseItem",
			section = unlock2
	)
	default boolean slot2UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot2Id",
			section = unlock2
	)
	default int slot2Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot2Title",
			section = unlock2
	)
	default String slot2Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot2Price",
			section = unlock2
	)
	default String slot2Price() { return "10 Points"; }

	// UNLOCK SLOT 3 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 3",
			description = "Unlock Slot 3",
			position = 40,
			closedByDefault = true
	)
	String unlock3 = "Unlock Slot 3";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot3Unlocked",
			section = unlock3
	)
	default boolean slot3Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 3 (Custom image needs to be in .runelite folder and called 3.png)",
			position = 15,
			keyName = "slot3UseItem",
			section = unlock3
	)
	default boolean slot3UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot3Id",
			section = unlock3
	)
	default int slot3Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot3Title",
			section = unlock3
	)
	default String slot3Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot3Price",
			section = unlock3
	)
	default String slot3Price() { return "10 Points"; }

	// UNLOCK SLOT 4 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 4",
			description = "Unlock Slot 4",
			position = 50,
			closedByDefault = true
	)
	String unlock4 = "Unlock Slot 4";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot4Unlocked",
			section = unlock4
	)
	default boolean slot4Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 3 (Custom image needs to be in .runelite folder and called 4.png)",
			position = 15,
			keyName = "slot4UseItem",
			section = unlock4
	)
	default boolean slot4UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot4Id",
			section = unlock4
	)
	default int slot4Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot4Title",
			section = unlock4
	)
	default String slot4Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot4Price",
			section = unlock4
	)
	default String slot4Price() { return "10 Points"; }

	// UNLOCK SLOT 5 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 5",
			description = "Unlock Slot 5",
			position = 60,
			closedByDefault = true
	)
	String unlock5 = "Unlock Slot 5";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot5Unlocked",
			section = unlock5
	)
	default boolean slot5Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 3 (Custom image needs to be in .runelite folder and called 5.png)",
			position = 15,
			keyName = "slot5UseItem",
			section = unlock5
	)
	default boolean slot5UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot5Id",
			section = unlock5
	)
	default int slot5Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot5Title",
			section = unlock5
	)
	default String slot5Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot5Price",
			section = unlock5
	)
	default String slot5Price() { return "10 Points"; }

	// UNLOCK SLOT 3 SETTINGS

	@ConfigSection(
			name = "Unlock Slot 6",
			description = "Unlock Slot 6",
			position = 70,
			closedByDefault = true
	)
	String unlock6 = "Unlock Slot 6";

	@ConfigItem(
			name = "Unlocked",
			description = "Unlocks slot when generating overlay",
			position = 10,
			keyName = "slot6Unlocked",
			section = unlock6
	)
	default boolean slot6Unlocked() { return false; }

	@ConfigItem(
			name = "Use Item Image",
			description = "Use item image / custom image for slot 3 (Custom image needs to be in .runelite folder and called 6.png)",
			position = 15,
			keyName = "slot6UseItem",
			section = unlock6
	)
	default boolean slot6UseItem() { return true; }

	@ConfigItem(
			name = "Item ID",
			description = "Item ID to use as image",
			position = 18,
			keyName = "slot6Id",
			section = unlock6
	)
	default int slot6Id() { return 100; }

	@ConfigItem(
			name = "Title",
			description = "Title text to display under item",
			position = 20,
			keyName = "slot6Title",
			section = unlock6
	)
	default String slot6Title() { return "Green Dragon"; }

	@ConfigItem(
			name = "Price",
			description = "Price text to display under item",
			position = 30,
			keyName = "slot6Price",
			section = unlock6
	)
	default String slot6Price() { return "10 Points"; }
}
