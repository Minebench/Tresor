/* This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.milkbowl.vault.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * The usage of this class is discouraged. While no plans to remove it in the near future
 * have been made as of yet it's just kept to keep compatiblity with plugins using it.
 * Its behaviour is really basic now and basically only wraps the Material enum as all
 * special data values were split into separate Material values in 1.13.
 * This isn't even an abstraction at all but
 */
public class Items {

    private static final List<ItemInfo> items = new CopyOnWriteArrayList<>();

    /**
     * Returns the list of ItemInfo's registered in Vault as an UnmodifiableList.
     * @return list of Items
     */
    public static List<ItemInfo> getItemList() {
        return Collections.unmodifiableList(items);
    }

    static {
        for (Material material : Material.values()) {
            //1.13 TODO: Don't add legacy material
            items.add(new ItemInfo(material.toString(), new String[][]{material.toString().toLowerCase().split("_")}, material));
        }
    }

    /**
     * Searchs for an ItemInfo from the given ItemStack
     * @param itemStack to search on
     * @return ItemInfo found, or null
     */
    public static ItemInfo itemByStack(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        for (ItemInfo item : items) {
            if (itemStack.getType().equals(item.getType())) {
                if (itemStack.getType().isSolid() && item.getType().isSolid()) {
                    //Solid, so check durability (Podzol, Colored Wool, et al.)
                    if (item.isDurable()) {
                        return item;
                    }
                } else {
                    //Not solid, so ignore durability (Stick, Stone Button, et al.)
                    return item;
                }
            }
        }

        return null;
    }

    public static ItemInfo itemByItem(ItemInfo item) {
        for (ItemInfo i : items) {
            if (item.equals(i)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Gets a relevant ItemInfo by it's Material
     * @param type of Material
     * @return ItemInfo record found or null if none
     */
    public static ItemInfo itemByType(Material type) {
        return itemByType(type, (short) 0);
    }

    /**
     * Searches for an ItemInfo record by Material and SubTypeID
     * @param type of Material
     * @param subType to check for
     * @return ItemInfo record found or null if none
     */
    public static ItemInfo itemByType(Material type, short subType) {
        for (ItemInfo item : items) {
            if (item.getType() == type && item.getSubTypeId() == subType) {
                return item;
            }
        }
        return null;
    }

    /**
     * Search for an item from a given string, useful for user input.  Uses 3 different types of reg-exp searching.
     *  Checks first for an ItemID.
     *  Checks second for ItemID:SubType
     *  Last, it will run a by-name item search assuming the string is the name of an item.
     *   
     * @param string to parse
     * @return ItemInfo found or null
     */
    public static ItemInfo itemByString(String string) {
        Pattern pattern = Pattern.compile("(?i)^(.*)$");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String name = matcher.group(1);
            return itemByName(name);
        }

        return null;
    }

    public static ItemInfo itemByName(ArrayList<String> search) {
        String searchString = String.join(" ", search);
        return itemByName(searchString);
    }

    public static ItemInfo[] itemByNames(ArrayList<String> search, boolean multi) {
        String searchString = String.join(" ", search);
        return itemsByName(searchString, multi);
    }

    /**
     * Multi-Item return search for dumping all items with the search string to the player
     *
     *
     * @param searchString to search for
     * @param multi whether to return a list of items or just the first
     * @return Array of items found
     */
    public static ItemInfo[] itemsByName(String searchString, boolean multi) {
        if (!multi) {
            return new ItemInfo[]{itemByName(searchString)};
        }

        ItemInfo[] itemList = new ItemInfo[]{};
        
        // Iterate through Items
        int i = 0;
        for (ItemInfo item : items) {
            // Look through each possible match criteria
            for (String[] attributes : item.search) {
                boolean match = false;
                // Loop through entire criteria strings
                for (String attribute : attributes) {
                    if (searchString.toLowerCase().contains(attribute)) {
                        match = true;
                        break;
                    }
                }
                // THIS was a match
                if (match) {
                    itemList[i] = item;
                    i++;
                }
            }
        }

        return itemList;
    }

    /**
     * Single item search function, for when we only ever want to return 1 result
     *
     * @param searchString to search for
     * @return ItemInfo Object
     */
    public static ItemInfo itemByName(String searchString) {
        ItemInfo matchedItem = null;
        int matchedItemStrength = 0;
        int matchedValue = 0;

        // Iterate through Items
        for (ItemInfo item : items) {
            // Look through each possible match criteria
            for (String[] attributes : item.search) {
                int val = 0;
                boolean match = false;
                // Loop through entire criteria strings
                for (String attribute : attributes) {
                    if (searchString.toLowerCase().contains(attribute)) {
                        val += attribute.length();
                        match = true;
                    } else {
                        match = false;
                        break;
                    }
                }

                // THIS was a match
                if (match) {
                    if (matchedItem == null || val > matchedValue || attributes.length > matchedItemStrength) {
                        matchedItem = item;
                        matchedValue = val;
                        matchedItemStrength = attributes.length;
                    }
                }
            }
        }

        return matchedItem;
    }

    /**
     * Joins elements of a String array with the glue between them into a String.
     * @deprecated since Tresor 1.0; use {@link String#join(CharSequence, CharSequence...)}
     *
     * @param array of elements to join together
     * @param glue what to put between each element
     * @return Concacted Array combined with glue
     */
    @Deprecated
    public static String join(String[] array, String glue) {
        return String.join(glue, array);
    }

    /**
     * Joins elements of a String array with the glue between them into a String.
     * @deprecated since Tresor 1.0; use {@link String#join(CharSequence, Iterable)}
     *
     * @param list of items to join together
     * @param glue what to put between each element
     * @return Concacted Array combined with glue
     */
    @Deprecated
    public static String join(List<String> list, String glue) {
        return String.join(glue, list);
    }
}
