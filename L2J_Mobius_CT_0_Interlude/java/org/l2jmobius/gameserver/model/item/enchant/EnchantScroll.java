/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.model.item.enchant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.data.xml.EnchantItemData;
import org.l2jmobius.gameserver.data.xml.EnchantItemGroupsData;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.item.type.EtcItemType;
import org.l2jmobius.gameserver.model.item.type.ItemType;

/**
 * @author UnAfraid, Mobius
 */
public class EnchantScroll extends AbstractEnchantItem
{
	private final boolean _isWeapon;
	private final boolean _isBlessed;
	private final boolean _isSafe;
	private final int _scrollGroupId;
	private final Map<Integer, Integer> _items = new HashMap<>();
	
	public EnchantScroll(StatSet set)
	{
		super(set);
		_scrollGroupId = set.getInt("scrollGroupId", 0);
		
		final ItemType type = getItem().getItemType();
		_isWeapon = (type == EtcItemType.ANCIENT_CRYSTAL_ENCHANT_WP) || (type == EtcItemType.BLESS_SCRL_ENCHANT_WP) || (type == EtcItemType.SCRL_ENCHANT_WP);
		_isBlessed = (type == EtcItemType.BLESS_SCRL_ENCHANT_AM) || (type == EtcItemType.BLESS_SCRL_ENCHANT_WP);
		_isSafe = (type == EtcItemType.ANCIENT_CRYSTAL_ENCHANT_AM) || (type == EtcItemType.ANCIENT_CRYSTAL_ENCHANT_WP);
	}
	
	@Override
	public boolean isWeapon()
	{
		return _isWeapon;
	}
	
	/**
	 * @return {@code true} for blessed scrolls (enchanted item will remain on failure), {@code false} otherwise
	 */
	public boolean isBlessed()
	{
		return _isBlessed;
	}
	
	/**
	 * @return {@code true} for safe-enchant scrolls (enchant level will remain on failure), {@code false} otherwise
	 */
	public boolean isSafe()
	{
		return _isSafe;
	}
	
	/**
	 * Enforces current scroll to use only those items as possible items to enchant
	 * @param itemId
	 * @param scrollGroupId
	 */
	public void addItem(int itemId, int scrollGroupId)
	{
		_items.put(itemId, scrollGroupId > -1 ? scrollGroupId : _scrollGroupId);
	}
	
	public Collection<Integer> getItems()
	{
		return _items.keySet();
	}
	
	/**
	 * @param itemToEnchant the item to be enchanted
	 * @return {@code true} if this scroll can be used with the specified support item and the item to be enchanted, {@code false} otherwise
	 */
	@Override
	public boolean isValid(Item itemToEnchant)
	{
		if (!_items.isEmpty() && !_items.containsKey(itemToEnchant.getId()))
		{
			return false;
		}
		
		if (_items.isEmpty())
		{
			for (EnchantScroll scroll : EnchantItemData.getInstance().getScrolls())
			{
				if (scroll.getId() == getId())
				{
					continue;
				}
				final Collection<Integer> scrollItems = scroll.getItems();
				if (!scrollItems.isEmpty() && scrollItems.contains(itemToEnchant.getId()))
				{
					return false;
				}
			}
		}
		
		return super.isValid(itemToEnchant);
	}
	
	/**
	 * @param player
	 * @param enchantItem
	 * @return the chance of current scroll's group.
	 */
	public double getChance(Player player, Item enchantItem)
	{
		final int scrollGroupId = _items.getOrDefault(enchantItem.getId(), _scrollGroupId);
		if (EnchantItemGroupsData.getInstance().getScrollGroup(scrollGroupId) == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Unexistent enchant scroll group specified for enchant scroll: " + getId());
			return -1;
		}
		
		final EnchantItemGroup group = EnchantItemGroupsData.getInstance().getItemGroup(enchantItem.getTemplate(), scrollGroupId);
		if (group == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Couldn't find enchant item group for scroll: " + getId() + " requested by: " + player);
			return -1;
		}
		
		return group.getChance(enchantItem.getEnchantLevel());
	}
	
	/**
	 * @param player
	 * @param enchantItem
	 * @return the total chance for success rate of this scroll
	 */
	public EnchantResultType calculateSuccess(Player player, Item enchantItem)
	{
		if (!isValid(enchantItem))
		{
			return EnchantResultType.ERROR;
		}
		
		final double chance = getChance(player, enchantItem);
		if (chance == -1)
		{
			return EnchantResultType.ERROR;
		}
		
		final double bonusRate = getBonusRate();
		final double finalChance = Math.min(chance + bonusRate, 100);
		final double random = 100 * Rnd.nextDouble();
		final boolean success = (random < finalChance);
		return success ? EnchantResultType.SUCCESS : EnchantResultType.FAILURE;
	}
}
