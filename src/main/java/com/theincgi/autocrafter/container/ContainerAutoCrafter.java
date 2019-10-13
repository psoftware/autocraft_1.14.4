//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.container;

import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoCrafter extends Container {
    private IInventory playerInv;
    private TileAutoCrafter tileAutoCrafter;
    private ItemStack lastTarget = null;
    public Slot targetSlot;

    public ContainerAutoCrafter(IInventory playerInv, TileAutoCrafter te) {
        this.playerInv = playerInv;
        this.tileAutoCrafter = te;
        int slot = 0;

        int y;
        int x;
        for(y = 0; y < 3; ++y) {
            for(x = 0; x < 3; ++x) {
                final int sloooooot = slot;
                this.func_75146_a(new Slot(this.tileAutoCrafter, slot++, 30 + x * 18, 17 + y * 18) {
                    public boolean func_75214_a(ItemStack stack) {
                        return ContainerAutoCrafter.this.tileAutoCrafter.isSlotAllowed(sloooooot, stack);
                    }
                });
            }
        }

        this.func_75146_a(new Slot(this.tileAutoCrafter, slot++, 124, 49) {
            public boolean func_75214_a(ItemStack stack) {
                return false;
            }
        });
        this.func_75146_a(this.targetSlot = new Slot(this.tileAutoCrafter, slot++, 124, 18) {
            public int func_178170_b(ItemStack stack) {
                return 0;
            }

            public void func_75218_e() {
                super.func_75218_e();
            }

            public boolean func_82869_a(EntityPlayer playerIn) {
                return true;
            }

            public boolean func_75214_a(ItemStack stack) {
                return false;
            }
        });
        slot = 0;

        for(y = 0; y < 9; ++y) {
            this.func_75146_a(new Slot(playerInv, slot++, 8 + y * 18, 142));
        }

        slot = 9;

        for(y = 0; y < 3; ++y) {
            for(x = 0; x < 9; ++x) {
                this.func_75146_a(new Slot(playerInv, slot++, 8 + x * 18, 84 + y * 18));
            }
        }

    }

    public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
        ItemStack previous = ItemStack.field_190927_a;
        Slot slot = (Slot)this.field_75151_b.get(index);
        ContainerAutoCrafter.Slots catagory = ContainerAutoCrafter.Slots.getCatagory(index);
        if (slot != null && slot.func_75216_d()) {
            ItemStack current = slot.func_75211_c();
            previous = current.func_77946_l();
            if (catagory != null) {
                switch(catagory) {
                    case CRAFTING:
                    case OUTPUT:
                        if (!this.func_75135_a(current, ContainerAutoCrafter.Slots.PLAYERINV.getStart(), ContainerAutoCrafter.Slots.PLAYERINV.getEnd() + 1, false)) {
                            return ItemStack.field_190927_a;
                        }

                        if (!this.func_75135_a(current, ContainerAutoCrafter.Slots.HOTBAR.getStart(), ContainerAutoCrafter.Slots.HOTBAR.getEnd() + 1, false)) {
                            return ItemStack.field_190927_a;
                        }
                        break;
                    case HOTBAR:
                    case PLAYERINV:
                        if (this.tileAutoCrafter.getCrafts().func_190926_b()) {
                            if (!this.func_75135_a(current, ContainerAutoCrafter.Slots.TARGET.getStart(), ContainerAutoCrafter.Slots.TARGET.getEnd() + 1, false)) {
                                return ItemStack.field_190927_a;
                            }
                        } else if (!this.func_75135_a(current, ContainerAutoCrafter.Slots.CRAFTING.getStart(), ContainerAutoCrafter.Slots.CRAFTING.getEnd() + 1, false)) {
                            return ItemStack.field_190927_a;
                        }
                }
            }

            if (current.func_190916_E() <= 0) {
                slot.func_75215_d(ItemStack.field_190927_a);
            }

            if (current.func_190916_E() == previous.func_190916_E()) {
                return ItemStack.field_190927_a;
            }

            slot.func_190901_a(playerIn, current);
        }

        return previous;
    }

    private ItemStack moveToCatagory(ContainerAutoCrafter.Slots catagory, InventoryPlayer inventory, ItemStack containerStack) {
        int i;
        ItemStack invStack;
        for(i = catagory.getStart(); i <= catagory.getEnd(); ++i) {
            invStack = inventory.func_70301_a(i);
            if (invStack.func_77969_a(containerStack)) {
                int addable = Math.min(invStack.func_77976_d() - invStack.func_190916_E(), containerStack.func_190916_E());
                if (addable > 0) {
                    invStack.func_190920_e(invStack.func_190916_E() + addable);
                    containerStack.func_190920_e(containerStack.func_190916_E() - addable);
                    if (containerStack.func_190916_E() == 0) {
                        return ItemStack.field_190927_a;
                    }

                    return containerStack;
                }
            }
        }

        for(i = catagory.getStart(); i <= catagory.getEnd(); ++i) {
            invStack = inventory.func_70301_a(i);
            if (invStack.func_190926_b()) {
                inventory.field_70462_a.set(i - ContainerAutoCrafter.Slots.PLAYERINV.getStart(), containerStack);
                containerStack.func_190920_e(0);
                if (containerStack.func_190916_E() == 0) {
                    return ItemStack.field_190927_a;
                }

                return containerStack;
            }
        }

        return containerStack;
    }

    public boolean func_75145_c(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }

    private static enum Slots {
        CRAFTING(0, 8),
        OUTPUT(9),
        TARGET(10),
        PLAYERINV(20, 46),
        HOTBAR(11, 19);

        int x;
        int y;

        private Slots(int x) {
            this(x, x);
        }

        private Slots(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getStart() {
            return this.x;
        }

        public int getEnd() {
            return this.y;
        }

        public static ContainerAutoCrafter.Slots getCatagory(int index) {
            ContainerAutoCrafter.Slots[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ContainerAutoCrafter.Slots s = var1[var3];
                if (s.getStart() <= index && index <= s.getEnd()) {
                    return s;
                }
            }

            return null;
        }
    }
}
