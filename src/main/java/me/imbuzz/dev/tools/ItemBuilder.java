package me.imbuzz.dev.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import javafx.scene.chart.ValueAxis;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.nms.Reflection;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.imbuzz.dev.tools.Useful.colorize;

public class ItemBuilder {

    private ItemStack is;
    private ItemMeta itemMeta;

    public ItemBuilder(Material m){
        this(m, 1);
    }

    public ItemBuilder(ItemStack is){
        this.is=is;
        itemMeta = is.getItemMeta();
    }

    public ItemBuilder(Material m, int amount){
        is = new ItemStack(m, amount);
        itemMeta = is.getItemMeta();
    }

    public ItemBuilder setMaterial(Material material){
        is.setType(material);
        return this;
    }

    public ItemBuilder clone(){
        return new ItemBuilder(is);
    }

    public String getName(){
        return itemMeta.getDisplayName();
    }

    public ItemBuilder setName(String name){
        itemMeta.setDisplayName(Useful.colorize(name));
        return this;
    }

    public ItemBuilder addLoreLines(String...lines){
        List<String> lore = new ArrayList<>();
        if(itemMeta.hasLore())lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lines){
            lore.add(colorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder addAllLore(ArrayList<String> lore2){
        List<String> lore = new ArrayList<>();
        if(itemMeta.hasLore())lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lore2){
            lore.add(colorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder setNBTBoolean(String key, boolean state){
        NBTItem nbti = new NBTItem(is);
        nbti.setBoolean(key, state);

        is = nbti.getItem();
        itemMeta = is.getItemMeta();
        return this;
    }

    public ItemBuilder setNBTString(String key, String information){
        NBTItem nbti = new NBTItem(is);
        nbti.setString(key, information);

        is = nbti.getItem();
        itemMeta = is.getItemMeta();
        return this;
    }

    public boolean getNBTBoolean(String tag){
        NBTItem nbti = new NBTItem(is);
        return nbti.getBoolean(tag);
    }

    public String getNBTString(String tag){
        NBTItem nbti = new NBTItem(is);
        return nbti.getString(tag);
    }

    public ItemStack toItemStack(){
        is.setItemMeta(itemMeta);
        return is;
    }

    @RequiredArgsConstructor @Getter
    public enum NBTType {

        STRING(String.class),
        INTEGER(int.class),
        BOOLEAN(boolean.class),
        SHORT(short.class),
        LONG(long.class),
        DOUBLE(double.class),
        BYTE(byte.class);

        private final Class<?> clazz;

    }

}
