package me.imbuzz.dev.tools;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
