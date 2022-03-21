package model

import org.bson.Document

interface Entity {
    fun getDoc(): Document
}