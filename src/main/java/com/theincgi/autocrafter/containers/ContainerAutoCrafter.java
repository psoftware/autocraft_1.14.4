//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.containers;

import com.theincgi.autocrafter.AutoCrafter;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ContainerAutoCrafter extends Container {
    private TileAutoCrafter tileAutoCrafter;
    private ItemStack lastTarget = null;
    public Slot targetSlot;

    // Client side
    public ContainerAutoCrafter(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv, (TileAutoCrafter) Minecraft.getInstance().world.getTileEntity(extraData.readBlockPos()));
    }

    // Server side
    public ContainerAutoCrafter(int windowId, PlayerInventory playerInv, TileAutoCrafter te) {
        super(AutoCrafter.CONTAINER_AUTOCRAFTER.get(), windowId);
        this.tileAutoCrafter = te;
        int slot = 0;

        int y;
        int x;
        for (y = 0; y < 3; ++y) {
            for (x = 0; x < 3; ++x) {
                final int theSlot = slot;
                this.addSlot(new Slot(this.tileAutoCrafter, slot++, 30 + x * 18, 17 + y * 18) {
                    public boolean isItemValid(ItemStack stack) {
                        return ContainerAutoCrafter.this.tileAutoCrafter.isSlotAllowed(theSlot, stack);
                    }
                });
            }
        }

        this.addSlot(new Slot(this.tileAutoCrafter, slot++, 124, 49) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });

        this.addSlot(this.targetSlot = new Slot(this.tileAutoCrafter, slot++, 124, 18) {
            public int getItemStackLimit(ItemStack stack) {
                return 0;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
            }

            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                return true;
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        slot = 0;

        for (y = 0; y < 9; ++y) {
            this.addSlot(new Slot(playerInv, slot++, 8 + y * 18, 142));
        }

        slot = 9;

        for (y = 0; y < 3; ++y) {
            for (x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInv, slot++, 8 + x * 18, 84 + y * 18));
            }
        }

    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        ContainerAutoCrafter.Slots category = ContainerAutoCrafter.Slots.getCategory(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();
            if (category != null) {
                switch (category) {
                    case CRAFTING:
                    case OUTPUT:
                        //case TARGET: target doesnt hold anything anymore
                        if (!this.mergeItemStack(current, ContainerAutoCrafter.Slots.PLAYERINV.getStart(), ContainerAutoCrafter.Slots.PLAYERINV.getEnd() + 1, false)) {
                            return ItemStack.EMPTY;
                        }

                        if (!this.mergeItemStack(current, ContainerAutoCrafter.Slots.HOTBAR.getStart(), ContainerAutoCrafter.Slots.HOTBAR.getEnd() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                        break;
                    case HOTBAR:
                    case PLAYERINV:
                        if (this.tileAutoCrafter.getCrafts().isEmpty()) {
                            if (!this.mergeItemStack(current, ContainerAutoCrafter.Slots.TARGET.getStart(), ContainerAutoCrafter.Slots.TARGET.getEnd() + 1, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.mergeItemStack(current, ContainerAutoCrafter.Slots.CRAFTING.getStart(), ContainerAutoCrafter.Slots.CRAFTING.getEnd() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                }
            }

            if (current.getCount() <= 0) {
                slot.putStack(ItemStack.EMPTY);
            }

            if (current.getCount() == previous.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, current);
        }

        return previous;
    }

    /*
    private ItemStack moveToCategory(ContainerAutoCrafter.Slots category, PlayerInventory inventory, ItemStack containerStack) {
        int i;
        ItemStack invStack;
        for(i = category.getStart(); i <= category.getEnd(); ++i) {
            invStack = inventory.getStackInSlot(i);
            if (invStack.isItemEqual(containerStack)) {
                int addable = Math.min(invStack.getMaxStackSize() - invStack.getCount(), containerStack.getCount());
                if (addable > 0) {
                    invStack.setCount(invStack.getCount() + addable);
                    containerStack.setCount(containerStack.getCount() - addable);
                    if (containerStack.getCount() == 0) {
                        return ItemStack.EMPTY;
                    }

                    return containerStack;
                }
            }
        }

        for(i = category.getStart(); i <= category.getEnd(); ++i) {
            invStack = inventory.getStackInSlot(i);
            if (invStack.isEmpty()) {
                inventory.mainInventory.set(i - ContainerAutoCrafter.Slots.PLAYERINV.getStart(), containerStack);
                containerStack.setCount(0);
                if (containerStack.getCount() == 0) {
                    return ItemStack.EMPTY;
                }

                return containerStack;
            }
        }

        return containerStack;
    }
    */

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    private enum Slots {
        CRAFTING(0, 8),
        OUTPUT(9),
        TARGET(10),
        PLAYERINV(20, 46),
        HOTBAR(11, 19);

        int x;
        int y;

        Slots(int x) {
            this(x, x);
        }

        Slots(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getStart() {
            return this.x;
        }

        public int getEnd() {
            return this.y;
        }

        public static ContainerAutoCrafter.Slots getCategory(int index) {
            for (Slots s : values()) {
                if (s.getStart() <= index && index <= s.getEnd()) {
                    return s;
                }
            }

            return null;
        }
    }


    public TileAutoCrafter getTileAutoCrafter() {
        return tileAutoCrafter;
    }
}
