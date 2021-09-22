package com.eloli.inkerbot.api.service

import org.ktorm.database.Database
import javax.inject.Provider

interface DatabaseService : Provider<Database> {

}