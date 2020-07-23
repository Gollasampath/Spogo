package com.pss.spogoo.models

import android.content.Context
import android.widget.Toast
import io.paperdb.Paper

class ShoppingCart {

    companion object {

        fun addItem(cartItem: CartItem) {
            val cart = ShoppingCart.getCart()

            val targetItem =
                cart.singleOrNull { it.products_id == cartItem.products_id }

            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {

                targetItem.quantity++
            }
            ShoppingCart.saveCart(cart)

        }


        fun removeItem(cartItem: CartItem, context: Context) {

            val cart = ShoppingCart.getCart()


            val targetItem = cart.singleOrNull { it.products_id == cartItem.products_id }

            if (targetItem != null) {

                if (targetItem.quantity > 0) {

                    Toast.makeText(context, "great quantity", Toast.LENGTH_SHORT).show()
                    targetItem.quantity--
                } else {
                    cart.remove(targetItem)
                }

            }

            ShoppingCart.saveCart(cart)

        }

        fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }

        fun dleteCart(cartItem: CartItem, context: Context) {
            val cart = ShoppingCart.getCart()
            val targetItem = cart.singleOrNull { it.products_id == cartItem.products_id }

            if (targetItem != null) {

                if (targetItem.quantity > 0) {

                    Toast.makeText(context, "delete quantity", Toast.LENGTH_SHORT).show()
                    targetItem.quantity--
                } else {
                    Paper.book().delete(cart.toString())
                }

            }
            ShoppingCart.saveCart(cart)

        }


        fun getCart(): MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())
        }


        fun getShoppingCartSize(): Int {

            var cartSize = 0
            ShoppingCart.getCart().forEach {
                cartSize += it.quantity
            }

            return cartSize
        }
    }


}