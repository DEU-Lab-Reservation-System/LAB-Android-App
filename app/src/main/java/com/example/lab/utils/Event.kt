package com.example.lab.utils

class Event<T>(private var content: T) {
    private var hasBeenHandled = false

    fun contentIfNotHandled(): T?{
        // 이미 이벤트가 발생했으면 return
        if(hasBeenHandled) return null

        // 처음 발생하는 경우 플래그를 true로 만들고 content를 리턴
        hasBeenHandled = true
        return content
    }

    fun setContent(content: T){
        this.content = content
    }
}