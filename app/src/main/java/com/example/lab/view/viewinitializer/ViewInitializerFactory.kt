package com.example.lab.view.viewinitializer

import com.example.lab.view.viewinitializer.reserv.AdminReservViewInitializer
import com.example.lab.view.viewinitializer.reserv.ProfReservViewInitializer
import com.example.lab.view.viewinitializer.reserv.StuReservViewInitializer

class ViewInitializerFactory {
    private inner class Key(
        private val role:String = "",
        private val view:String = ""
    ){
        override fun equals(other: Any?): Boolean {
            if(this === other) return true
            if(other !is Key) return false

            val key: Key = other as Key

            return this.role == key.role && this.view == key.view
        }

        override fun hashCode(): Int {
            val result = role.hashCode()
            return 31 * result + (view.hashCode())
        }
    }

    private val map:MutableMap<Key, ViewInitializer> = mutableMapOf()

    init {
        map[Key("USER", "RESERVATION")]      = StuReservViewInitializer()
        map[Key("PROFESSOR", "RESERVATION")] = ProfReservViewInitializer()
        map[Key("ADMIN", "RESERVATION")]     = AdminReservViewInitializer()
    }

    fun getInitializer(role:String, view:String): ViewInitializer {
        println("Role = $role, View = $view")
        return this.map[Key(role, view)]!!
    }
}