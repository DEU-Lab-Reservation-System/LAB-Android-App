package com.example.lab.view.viewinitializer

import com.example.lab.data.enum.Role
import com.example.lab.view.viewinitializer.notify.AdminNotifyViewInitializer
import com.example.lab.view.viewinitializer.notify.ProfNotifyViewInitializer
import com.example.lab.view.viewinitializer.notify.StudentNotifyViewInitializer
import com.example.lab.view.viewinitializer.profile.AdminProfileViewInitializer
import com.example.lab.view.viewinitializer.profile.ProfProfileViewInitializer
import com.example.lab.view.viewinitializer.profile.StudentProfileViewInitializer
import com.example.lab.view.viewinitializer.reserv.AdminReservViewInitializer
import com.example.lab.view.viewinitializer.reserv.ProfReservViewInitializer
import com.example.lab.view.viewinitializer.reserv.StuReservViewInitializer
import com.example.lab.view.viewinitializer.timetable.AdminTimeTableViewInitializer
import com.example.lab.view.viewinitializer.timetable.ProfTimeTableViewInitializer
import com.example.lab.view.viewinitializer.timetable.StudentTimeTableViewInitializer

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
        map[Key("USER",  RESERV)]   = StuReservViewInitializer()
        map[Key("PROF",  RESERV)]   = ProfReservViewInitializer()
        map[Key("ADMIN", RESERV)]   = AdminReservViewInitializer()

        map[Key("USER",  NOTIFY)]   = StudentNotifyViewInitializer()
        map[Key("PROF",  NOTIFY)]   = ProfNotifyViewInitializer()
        map[Key("ADMIN", NOTIFY)]   = AdminNotifyViewInitializer()

        map[Key("USER",  PROFILE)]  = StudentProfileViewInitializer()
        map[Key("PROF",  PROFILE)]  = ProfProfileViewInitializer()
        map[Key("ADMIN", PROFILE)]  = AdminProfileViewInitializer()

        map[Key("USER",  TIMETABLE)]  = StudentTimeTableViewInitializer()
        map[Key("PROF",  TIMETABLE)]  = ProfTimeTableViewInitializer()
        map[Key("ADMIN", TIMETABLE)]  = AdminTimeTableViewInitializer()
    }

    /**
     * role.split("_")[0]하는 이유
     * USER, USER_TAKEOFF, USER_GRADUATE를 split("_")[0]하면
     * 모두 USER이므로 같은 학생으로 취급 가능
     */
    fun getInitializer(role:Role, view:String): ViewInitializer {
        return this.map[Key(role.name.split("_")[0], view)]!!
    }

    companion object{
        private const val RESERV    = "RESERVATION"
        private const val NOTIFY    = "NOTIFICATION"
        private const val PROFILE   = "PROFILE"
        private const val TIMETABLE = "TIMETABLE"
    }
}